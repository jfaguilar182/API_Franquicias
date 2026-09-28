output "connection_string" {
  description = "Cadena SRV del cluster (agregar usuario, clave y base de datos)"
  value       = mongodbatlas_cluster.franquicias.connection_strings[0].standard_srv
}

output "mongodb_uri_example" {
  description = "Formato de MONGODB_URI para la API"
  value       = "mongodb+srv://${var.db_username}:<password>@${replace(mongodbatlas_cluster.franquicias.connection_strings[0].standard_srv, "mongodb+srv://", "")}/franquicias"
}
