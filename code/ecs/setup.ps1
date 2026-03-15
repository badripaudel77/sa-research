# ============================================================
# ECS Setup Script
# Prerequisites: AWS CLI installed and configured (aws configure)
# Replace all <PLACEHOLDER> values before running
# ============================================================

$REGION       = "us-east-1"
$CLUSTER      = "sa-cluster"
$ACCOUNT_ID   = "<ACCOUNT_ID>"        # e.g. 123456789012
$VPC_ID       = "<VPC_ID>"            # e.g. vpc-0abc123
$SUBNET_1     = "<SUBNET_ID_1>"       # private subnet (AZ-a)
$SUBNET_2     = "<SUBNET_ID_2>"       # private subnet (AZ-b)
$SG_ECS       = "<SECURITY_GROUP_ID>" # allow inbound 8900, 8902 within VPC

# ── Patch account ID into task definitions ────────────────────
(Get-Content .\task-definitions\stock-service-task.json)   -replace '<ACCOUNT_ID>', $ACCOUNT_ID | Set-Content .\task-definitions\stock-service-task.json
(Get-Content .\task-definitions\product-service-task.json) -replace '<ACCOUNT_ID>', $ACCOUNT_ID | Set-Content .\task-definitions\product-service-task.json

# ── 1. Create ECS cluster (Fargate) ──────────────────────────
Write-Host "Creating ECS cluster..."
aws ecs create-cluster `
  --cluster-name $CLUSTER `
  --capacity-providers FARGATE `
  --default-capacity-provider-strategy capacityProvider=FARGATE,weight=1 `
  --region $REGION `
  --service-connect-defaults namespace=sa-namespace

# ── 2. Create CloudWatch log groups ──────────────────────────
Write-Host "Creating CloudWatch log groups..."
aws logs create-log-group --log-group-name /ecs/stock-service   --region $REGION
aws logs create-log-group --log-group-name /ecs/product-service --region $REGION

# ── 3. Register task definitions ─────────────────────────────
Write-Host "Registering task definitions..."
aws ecs register-task-definition `
  --cli-input-json file://task-definitions/stock-service-task.json `
  --region $REGION

aws ecs register-task-definition `
  --cli-input-json file://task-definitions/product-service-task.json `
  --region $REGION

# ── 4. Create stock-service ECS service (Service Connect) ────
Write-Host "Creating stock-service ECS service..."
aws ecs create-service `
  --cluster $CLUSTER `
  --service-name stock-service `
  --task-definition stock-service `
  --desired-count 1 `
  --launch-type FARGATE `
  --network-configuration "awsvpcConfiguration={subnets=[$SUBNET_1,$SUBNET_2],securityGroups=[$SG_ECS],assignPublicIp=DISABLED}" `
  --service-connect-configuration "{
    enabled: true,
    namespace: 'sa-namespace',
    services: [{
      portName: 'stock-service-port',
      clientAliases: [{ port: 8900, dnsName: 'stock-service' }]
    }]
  }" `
  --region $REGION

# ── 5. Create product-service ECS service (Service Connect) ──
# NOTE: fill in <RDS_ENDPOINT> and <RDS_PASSWORD> in the task
#       definition before registering, or use Secrets Manager.
Write-Host "Creating product-service ECS service..."
aws ecs create-service `
  --cluster $CLUSTER `
  --service-name product-service `
  --task-definition product-service `
  --desired-count 1 `
  --launch-type FARGATE `
  --network-configuration "awsvpcConfiguration={subnets=[$SUBNET_1,$SUBNET_2],securityGroups=[$SG_ECS],assignPublicIp=DISABLED}" `
  --service-connect-configuration "{
    enabled: true,
    namespace: 'sa-namespace',
    services: [{
      portName: 'product-service-port',
      clientAliases: [{ port: 8902, dnsName: 'product-service' }]
    }]
  }" `
  --region $REGION

Write-Host ""
Write-Host "Done. Next steps:"
Write-Host "  1. Create an RDS PostgreSQL instance in the same VPC"
Write-Host "     and update <RDS_ENDPOINT> + <RDS_PASSWORD> in product-service-task.json"
Write-Host "  2. Create an Application Load Balancer (ALB) in public subnets"
Write-Host "     targeting product-service on port 8902"
Write-Host "  3. Create AWS API Gateway HTTP API with a VPC Link to the ALB"
