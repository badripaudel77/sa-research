# Deploy To AWS

## What Terraform Provisions

The Terraform under `infra/terraform/` is structured to provision:

- VPC, subnets, internet gateway, route tables
- ECS cluster
- Cloud Map namespace
- ECS task definitions and services
- Service Connect configuration
- API Gateway HTTP API and JWT authorizer
- Cognito user pool and app client
- CloudWatch log groups
- IAM roles and policies
- MSK Serverless cluster
- SSM parameters and Secrets Manager placeholders
- a minimal single-AZ PostgreSQL RDS instance

## Prerequisites

- AWS CLI authenticated to the target account
- Terraform installed
- container images pushed to ECR
- a target AWS region selected

## Suggested Deployment Flow

1. Build and push images:

```bash
docker build -t <ecr>/order-service:latest code/order-service
docker build -t <ecr>/inventory-service:latest code/inventory-service
docker build -t <ecr>/notification-service:latest code/notification-service
```

2. Initialize and apply Terraform:

```bash
cd infra/terraform
terraform init
terraform apply \
  -var "aws_region=us-east-1" \
  -var "order_service_image=<ecr>/order-service:latest" \
  -var "inventory_service_image=<ecr>/inventory-service:latest" \
  -var "notification_service_image=<ecr>/notification-service:latest"
```

3. Create at least one Cognito user or enable a hosted UI login flow.
4. Seed the product catalog if the target PostgreSQL database is empty.
5. Confirm ECS tasks are healthy and Cloud Map registrations exist.
6. Invoke the HTTP API URL returned by Terraform outputs with a Cognito token.

## Runtime Configuration Inputs

Set these values through Terraform variables, SSM, or Secrets Manager:

- Cognito issuer URI
- Cognito app client audience
- RDS username and password
- MSK bootstrap brokers if different from Terraform-created outputs
- optional AppConfig application/environment/profile path values

## Sidecars And AWS Integrations

The application code is ready for:

- AWS AppConfig agent on `localhost:2772`
- OTLP trace export to an ADOT collector on `localhost:4317`

The Terraform in this repo provisions the surrounding AWS resources, but you should add the AppConfig agent and ADOT collector sidecars to the ECS task definitions before expecting live AppConfig refreshes or X-Ray export in AWS.

## Manual Checks After Deploy

- API Gateway route integrates to the Cloud Map-backed `order-service`
- `order-service` can resolve `inventory-service` over Service Connect
- `notification-service` consumes from the `orders.created` topic
- CloudWatch log groups receive JSON logs
- X-Ray shows traces from `order-service` and `inventory-service`
