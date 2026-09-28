terraform {
  required_version = ">= 1.5"
  required_providers {
    mongodbatlas = {
      source  = "mongodb/mongodbatlas"
      version = "~> 1.21"
    }
  }
}

provider "mongodbatlas" {
  public_key  = var.atlas_public_key
  private_key = var.atlas_private_key
}

# Proyecto dentro de la organización de Atlas
resource "mongodbatlas_project" "franquicias" {
  name   = var.project_name
  org_id = var.atlas_org_id
}

# Cluster gratuito M0 (compartido)
resource "mongodbatlas_cluster" "franquicias" {
  project_id                  = mongodbatlas_project.franquicias.id
  name                        = var.cluster_name
  provider_name               = "TENANT"
  backing_provider_name       = "AWS"
  provider_region_name        = var.region
  provider_instance_size_name = "M0"
}

# Usuario de base de datos para la API
resource "mongodbatlas_database_user" "api" {
  project_id         = mongodbatlas_project.franquicias.id
  username           = var.db_username
  password           = var.db_password
  auth_database_name = "admin"

  roles {
    role_name     = "readWrite"
    database_name = "franquicias"
  }
}

# Acceso de red (restringir en producción)
resource "mongodbatlas_project_ip_access_list" "api" {
  project_id = mongodbatlas_project.franquicias.id
  cidr_block = var.allowed_cidr
  comment    = "Acceso para la API de franquicias"
}
