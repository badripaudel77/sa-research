output "api_gateway_endpoint" {
  description = "Invoke URL for the HTTP API"
  value       = aws_apigatewayv2_api.http.api_endpoint
}

output "cognito_user_pool_id" {
  description = "Cognito user pool ID"
  value       = aws_cognito_user_pool.order_users.id
}

output "cognito_app_client_id" {
  description = "Cognito app client ID"
  value       = aws_cognito_user_pool_client.order_api.id
}

output "cloud_map_namespace_name" {
  description = "Cloud Map namespace used by ECS Service Connect"
  value       = aws_service_discovery_private_dns_namespace.services.name
}

output "order_service_discovery_arn" {
  description = "Cloud Map service ARN for order-service"
  value       = aws_service_discovery_service.order.arn
}

output "appconfig_application_name" {
  description = "AppConfig application name"
  value       = aws_appconfig_application.runtime.name
}

output "appconfig_runtime_path" {
  description = "AppConfig agent resource path used by order-service"
  value       = local.appconfig_path
}

output "db_endpoint" {
  description = "RDS endpoint for order-service"
  value       = aws_db_instance.order.address
}

output "msk_cluster_arn" {
  description = "MSK Serverless cluster ARN"
  value       = aws_msk_serverless_cluster.events.arn
}

output "msk_bootstrap_brokers_note" {
  description = "Update the msk_bootstrap_brokers variable with the broker string after cluster creation if needed"
  value       = local.kafka_bootstrap
}
