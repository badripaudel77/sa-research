# API Gateway
API-Gateway 
        - Name : api-gateway
        - Port : 8080
        - Host : http://localhost:8080

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

# Consul
Consul
        - Name : consul
        - Port : 8500   
        - Host : http://localhost:8500


# Docker Hub
Image : product-service:1.0.0
Image : stock-service:1.0.0


###  TODO
IAM Role: myResearchECSTaskExecutionRole

Cloudwatch:
        - 1st log group: /ecs/stock-service
        - 2nd log group: /ecs/product-service

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

##### sa-alb-1415317104.us-east-1.elb.amazonaws.com
STOCK_SERVICE_URL = http://YOUR_ALB_DNS
SPRING_AUTOCONFIGURE_EXCLUDE = org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration

SPRING_SQL_INIT_MODE = never

SPRING_DATASOURCE_URL=<DB_URL>
SPRING_DATASOURCE_USERNAME=<DB_USERNAME>
SPRING_DATASOURCE_PASSWORD=<DB_PASSWORD>

ALB DNS: sa-alb-1415317104.us-east-1.elb.amazonaws.com
DB: sa-database.c8x2essweygr.us-east-1.rds.amazonaws.com

KAFKA_BOOTSTRAP_SERVERS: Needs to change (override)


        