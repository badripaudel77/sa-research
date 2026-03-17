# API Gateway
        - AWS
# Product Service
Product-Service
        - Name : product-service
        - Port : 8902
        - Host : 
        
# Stock Service
Stock-Service
        - Name : stock-service
        - Port : 8900
        - Host : http://localhost:8900

# Registry
        - AWS Cloud Map 

# Docker Hub
Images:
    - product-service:1.0.0
    - stock-service:1.0.0
    - notification-service:1.0.0

###  TODO
IAM Role: myResearchECSTaskExecutionRole

Cloudwatch:
        - 1st log group: /ecs/stock-service
        - 2nd log group: /ecs/product-service
        - 3rd log group: /ecs/notification-service
        - 4th log group: /kafka/logs

ECS:
    Cluster: research-sa-cluster
    
    Tasks definitions:
     1. stock-service
        image uri: badripaudel77/stock-service:1.0.0
     2. product-service
        image uri: badripaudel77/product-service:1.0.0

Target Group (EC2):
 type : IP
 port : 8900 (port of stock-service)

 same for product-service
 type: IP
 port : 8902

Security group (EC2 > SG):
 name : alb-sg
 allow : *

 ECS sg: 
        - ecs-sg on both port (8900, 8902)

Load balancer:
      EC2 > ALB
      Internet facing
      alg-sg
      forward to product-tg


Add stock path rule on the ALB listener:
        Open EC2 -> Load Balancers -> sa-alb.
        Go to Listeners tab.
        Select HTTP:80 listener.
        Click View/edit rules.
        Add rule with:
        Condition: Path is /api/stock/*
        Action: Forward to stock-tg
        Priority: 10
        Save rules. 



### Min and max number of tasks

### Environment Variables

##### 

ALB URL: sa-alb-1415317104.us-east-1.elb.amazonaws.com

-------------------------
MSK:::
Topic : product.accessed
-------------------------

SPRING_SQL_INIT_MODE = never

KAFKA_BOOTSTRAP_SERVERS: 

KAFKA_BOOTSTRAP_SERVERS -> valueFrom   = /microservices/kafka/url
SPRING_DATASOURCE_PASSWORD -> valueFrom = /microservices/db/password
SPRING_DATASOURCE_URL = /microservices/db/url
STOCK_SERVICE_URL = /microservices/stockservice/url


###### SERVICES USED
1. ECS (Elastic Container Service)
2. AWS System Manager - Parameter Store
3. AWS MSK (Managed Streaming For Apache Kafka), Create Topic : product.accessed if not already.
4. ALB (Application Load Balancer)
5. Target Group
6. AWS RDS (PostgreSQL)
7. AWS Cloud Map (service discovery)
8. Resilience 4J 