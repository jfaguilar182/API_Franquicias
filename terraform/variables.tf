variable "atlas_public_key" {
  description = "API public key de MongoDB Atlas"
  type        = string
}

variable "atlas_private_key" {
  description = "API private key de MongoDB Atlas"
  type        = string
  sensitive   = true
}

variable "atlas_org_id" {
  description = "ID de la organización en MongoDB Atlas"
  type        = string
}

variable "project_name" {
  type    = string
  default = "franquicias"
}

variable "cluster_name" {
  type    = string
  default = "franquicias-cluster"
}

variable "region" {
  description = "Región de AWS en formato Atlas (ej. US_EAST_1)"
  type        = string
  default     = "US_EAST_1"
}

variable "db_username" {
  type    = string
  default = "franquicias_api"
}

variable "db_password" {
  type      = string
  sensitive = true
}

variable "allowed_cidr" {
  description = "Rango de IPs con acceso al cluster"
  type        = string
  default     = "0.0.0.0/0"
}
