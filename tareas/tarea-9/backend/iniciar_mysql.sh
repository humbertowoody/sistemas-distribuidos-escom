# Script para correr un MySQL local usando Docker.
# Para correr el script, ejecutar el siguiente comando:
# $ sh start_mysql.sh
# El script asume que se tiene instalado Docker en el sistema.
# Humberto Alejandro Ortega Alcocer.

# Detener y eliminar el contenedor si existe.
docker stop mysql_server >/dev/null 2>&1 && docker rm mysql_server >/dev/null 2>&1

# Iniciar la ejecución del contenedor.
docker run -d -p 3306:3306 --name mysql_server \
  -e MYUSQL_USER="root" \
  -e MYSQL_ROOT_PASSWORD="root" \
  -e MYSQL_PASSWORD="root" \
  -e MYSQL_DATABASE="tarea9" \
  -v ./base_de_datos.sql:/docker-entrypoint-initdb.d/init_script.sql \
  mysql:latest

