locals {
  name_prefix = var.project_name

  common_tags = merge(var.tags, {
    project = var.project_name
  })

  public_subnets = {
    a = {
      cidr = cidrsubnet(var.vpc_cidr, 8, 1)
      az   = "${var.aws_region}a"
    }
    b = {
      cidr = cidrsubnet(var.vpc_cidr, 8, 2)
      az   = "${var.aws_region}b"
    }
  }

  private_subnets = {
    a = {
      cidr = cidrsubnet(var.vpc_cidr, 8, 101)
      az   = "${var.aws_region}a"
    }
    b = {
      cidr = cidrsubnet(var.vpc_cidr, 8, 102)
      az   = "${var.aws_region}b"
    }
  }

  cognito_issuer_uri   = "https://cognito-idp.${var.aws_region}.amazonaws.com/${aws_cognito_user_pool.order_users.id}"
  kafka_bootstrap      = var.msk_bootstrap_brokers != "" ? var.msk_bootstrap_brokers : "REPLACE_WITH_MSK_BOOTSTRAP_BROKERS"
  appconfig_path       = "/applications/${aws_appconfig_application.runtime.name}/environments/${aws_appconfig_environment.demo.name}/configurations/${aws_appconfig_configuration_profile.runtime.name}"
  cloud_map_namespace  = "${var.project_name}.local"
  appconfig_json       = jsonencode({
    inventoryBaseUrl = "http://inventory-service:8080"
    orderCreatedTopic = var.order_created_topic
  })
}
