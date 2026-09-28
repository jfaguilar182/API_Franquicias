API de Franquicias

===================



Esta es una aplicacion web que permite gestionar franquicias, sus sucursales y los productos que se venden en cada una de ellas, junto con la cantidad disponible en inventario.



El sistema utiliza Spring Boot y MongoDB para organizar la informacion.





Estructura de la informacion

\---------------------------

La informacion se organiza de forma jerarquica:



\- Franquicia

&#x20; - Sucursal

&#x20;   - Producto (Nombre y Cantidad en inventario)



Toda la informacion de una franquicia, sus sucursales y sus productos se guarda junta en un solo lugar dentro de la base de datos. Esto permite consultar y actualizar los datos de manera directa y rapida.





Requisitos previos

\------------------

Para ejecutar este proyecto en tu computadora local, necesitas instalar las siguientes herramientas segun el metodo que elijas:



1\. Git (para descargar el proyecto): https://git-scm.com

2\. Docker y Docker Desktop (recomendado): https://www.docker.com/products/docker-desktop/

3\. Java 21 y Maven (solo si prefieres el segundo metodo): https://adoptium.net/ y https://maven.apache.org/





Pasos detallados para ejecutar en local

\-------------------------------------



Opcion 1: Con Docker Desktop (Metodo recomendado y mas sencillo)



1\. Descargar el codigo fuente:

&#x20;  Abre la consola de comandos de tu sistema operativo (Terminal en macOS/Linux o CMD/PowerShell en Windows) y ejecuta:

&#x20;

&#x20;  git clone <url-del-repositorio>

&#x20;  cd franquicias-api



2\. Iniciar Docker Desktop:

&#x20;  Asegurate de que la aplicacion Docker Desktop este abierta y ejecutandose en tu computadora.



3\. Construir e iniciar los servicios:

&#x20;  En la misma consola de comandos, ejecuta el siguiente comando. Este proceso descargara los componentes necesarios y preparara la base de datos y el programa automaticamente:

&#x20;

&#x20;  docker compose up --build



4\. Verificar que esta funcionando:

&#x20;  Una vez que la consola deje de mostrar texto de instalacion y veas mensajes de inicio, el sistema estara listo. Puedes probar abrir un navegador web o enviar peticiones a la siguiente direccion:

&#x20;  http://localhost:8080/api/franchises



5\. Como detener el programa:

&#x20;  Para detener el programa, presiona las teclas "Ctrl + C" en la consola. Si quieres apagar completamente los servicios creados, ejecuta:

&#x20;

&#x20;  docker compose down

&#x20;

&#x20;  (Nota: Si deseas borrar tambien los datos guardados de prueba, agrega -v al final: docker compose down -v)





Opcion 2: Con Java, Maven y MongoDB local



1\. Descargar el codigo fuente:

&#x20;  Abre la consola de comandos y ejecuta:

&#x20;

&#x20;  git clone <url-del-repositorio>

&#x20;  cd franquicias-api



2\. Iniciar solo la base de datos MongoDB:

&#x20;  Ejecuta el siguiente comando para levantar unicamente el servicio de base de datos en segundo plano:

&#x20;

&#x20;  docker compose up -d mongo



3\. Iniciar la aplicacion con Maven:

&#x20;  Con la base de datos ya encendida, ejecuta el siguiente comando para iniciar el programa:

&#x20;

&#x20;  mvn spring-boot:run



&#x20;  Por defecto, la aplicacion se conectara a la base de datos en la direccion mongodb://localhost:27017/franquicias. Si necesitas usar una direccion diferente, puedes definir la variable de entorno MONGODB\_URI antes de ejecutar.





Ejecutar las pruebas del sistema

\--------------------------------

Para verificar que todas las funciones basicas del sistema operen correctamente sin necesidad de conectarse a la base de datos, ejecuta:



mvn test





Lista de acciones disponibles (Rutas de la API)

\-----------------------------------------------

Direccion base: http://localhost:8080/api/franchises



1\. Listar todas las franquicias

&#x20;  - Metodo: GET

&#x20;  - Ruta: /api/franchises



2\. Crear una nueva franquicia

&#x20;  - Metodo: POST

&#x20;  - Ruta: /api/franchises

&#x20;  - Datos a enviar: {"name": "Nombre de la Franquicia"}



3\. Ver el detalle de una franquicia

&#x20;  - Metodo: GET

&#x20;  - Ruta: /api/franchises/{ID\_FRANQUICIA}



4\. Cambiar el nombre de una franquicia

&#x20;  - Metodo: PATCH

&#x20;  - Ruta: /api/franchises/{ID\_FRANQUICIA}

&#x20;  - Datos a enviar: {"name": "Nuevo Nombre"}



5\. Agregar una sucursal a una franquicia

&#x20;  - Metodo: POST

&#x20;  - Ruta: /api/franchises/{ID\_FRANQUICIA}/branches

&#x20;  - Datos a enviar: {"name": "Nombre de la Sucursal"}



6\. Cambiar el nombre de una sucursal

&#x20;  - Metodo: PATCH

&#x20;  - Ruta: /api/franchises/{ID\_FRANQUICIA}/branches/{ID\_SUCURSAL}

&#x20;  - Datos a enviar: {"name": "Nuevo Nombre"}



7\. Agregar un producto a una sucursal

&#x20;  - Metodo: POST

&#x20;  - Ruta: /api/franchises/{ID\_FRANQUICIA}/branches/{ID\_SUCURSAL}/products

&#x20;  - Datos a enviar: {"name": "Nombre Producto", "stock": 10}



8\. Cambiar el nombre de un producto

&#x20;  - Metodo: PATCH

&#x20;  - Ruta: /api/franchises/{ID\_FRANQUICIA}/branches/{ID\_SUCURSAL}/products/{ID\_PRODUCTO}

&#x20;  - Datos a enviar: {"name": "Nuevo Nombre"}



9\. Cambiar la cantidad disponible (stock) de un producto

&#x20;  - Metodo: PATCH

&#x20;  - Ruta: /api/franchises/{ID\_FRANQUICIA}/branches/{ID\_SUCURSAL}/products/{ID\_PRODUCTO}/stock

&#x20;  - Datos a enviar: {"stock": 25}



10\. Eliminar un producto de una sucursal

&#x20;   - Metodo: DELETE

&#x20;   - Ruta: /api/franchises/{ID\_FRANQUICIA}/branches/{ID\_SUCURSAL}/products/{ID\_PRODUCTO}



11\. Consultar el producto con mayor cantidad por sucursal

&#x20;   - Metodo: GET

&#x20;   - Ruta: /api/franchises/{ID\_FRANQUICIA}/top-stock-products





Respuestas del sistema

\----------------------

\- 200 / 201: La operacion se realizo con exito.

\- 400: Los datos enviados no son validos (por ejemplo, enviar un valor de inventario negativo).

\- 404: No se encontro la franquicia, sucursal o producto solicitado.

\- 409: Ya existe una franquicia registrada con ese mismo nombre.





Ejemplo paso a paso para probar el programa desde la consola

\-----------------------------------------------------------



1\. Crear una franquicia:

curl -s -X POST localhost:8080/api/franchises -H "Content-Type: application/json" -d "{\\"name\\":\\"Restaurantes El Sol\\"}"



2\. Agregar una sucursal (reemplaza <ID\_FRANQUICIA> por el ID recibido en el paso 1):

curl -s -X POST localhost:8080/api/franchises/<ID\_FRANQUICIA>/branches -H "Content-Type: application/json" -d "{\\"name\\":\\"Sucursal Norte\\"}"



3\. Agregar un producto (reemplaza <ID\_SUCURSAL> por el ID recibido):

curl -s -X POST localhost:8080/api/franchises/<ID\_FRANQUICIA>/branches/<ID\_SUCURSAL>/products -H "Content-Type: application/json" -d "{\\"name\\":\\"Bebida 500ml\\",\\"stock\\":50}"



4\. Modificar la cantidad disponible:

curl -s -X PATCH localhost:8080/api/franchises/<ID\_FRANQUICIA>/branches/<ID\_SUCURSAL>/products/<ID\_PRODUCTO>/stock -H "Content-Type: application/json" -d "{\\"stock\\":20}"



5\. Consultar los productos con mas unidades por sucursal:

curl -s localhost:8080/api/franchises/<ID\_FRANQUICIA>/top-stock-products



6\. Eliminar un producto:

curl -s -X DELETE localhost:8080/api/franchises/<ID\_FRANQUICIA>/branches/<ID\_SUCURSAL>/products/<ID\_PRODUCTO>





Configuracion en la nube (Opcional con Terraform)

\------------------------------------------------

Si deseas desplegar la base de datos en la nube utilizando MongoDB Atlas:



1\. Entra a la carpeta de infraestructura:

&#x20;  cd terraform



2\. Copia el archivo de configuracion de ejemplo y coloca tus credenciales:

&#x20;  cp terraform.tfvars.example terraform.tfvars



3\. Prepara e inicia la configuracion:

&#x20;  terraform init

&#x20;  terraform apply



4\. Una vez completado, copia la direccion generada e inicia el programa apuntando a esa base de datos:

&#x20;  MONGODB\_URI="tu\_direccion\_de\_mongodb\_atlas" docker compose up --build api





Organizacion de los archivos del proyecto

\-----------------------------------------

src/main/java/com/franquicias/api

\- controller: Recibe las peticiones de los usuarios.

\- service: Contiene las reglas y procesos del sistema.

\- repository: Enlace directo con la base de datos.

\- domain: Define la estructura de Franquicia, Sucursal y Producto.

\- dto: Formatos para enviar y recibir informacion.

\- exception: Gestion y control de errores.

terraform/: Configuracion automatica para la nube.

Dockerfile: Instrucciones para empaquetar la aplicacion.

docker-compose.yml: Configuracion de los servicios en Docker.

