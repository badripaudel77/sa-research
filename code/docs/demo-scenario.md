# Demo Scenario

## Goal

Show authenticated edge access, internal service discovery, Kafka messaging, and circuit-breaker fallback in one concise demo.

## AWS Demo Path

1. Authenticate against Cognito and obtain a JWT for the configured app client.
2. Call API Gateway:

```http
POST https://{http-api-id}.execute-api.{region}.amazonaws.com/orders
Authorization: Bearer <jwt>
Content-Type: application/json

{"productNumber":"PROD001","quantity":2}
```

3. Confirm the response includes:

- `orderId`
- `status`
- `correlationId`
- `createdAt`

4. Query the created order:

```http
GET https://{http-api-id}.execute-api.{region}.amazonaws.com/orders/{orderId}
Authorization: Bearer <jwt>
```

## What The Demo Proves

### Internal REST call

- `order-service` resolves `inventory-service` through ECS Service Connect / Cloud Map
- `inventory-service` returns the reservation result

### Kafka event publication and consumption

- `order-service` publishes `OrderCreatedEvent`
- `notification-service` consumes the event from MSK Serverless
- CloudWatch Logs show a notification log entry for the same `orderId`

### Circuit breaker / fallback

1. Stop or fail `inventory-service`
2. Repeat the `POST /orders` call
3. Observe:
   - response status is `PENDING_INVENTORY`
   - response message explains the fallback
   - the event still appears in `notification-service` logs

## Visibility Points

- API Gateway access logs and metrics
- `order-service`, `inventory-service`, and `notification-service` CloudWatch log groups
- X-Ray traces for the request path
- Kafka consumer activity in `notification-service` logs
