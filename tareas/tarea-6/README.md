# Tarea 6. Implementación de un servicio web estilo REST para Tomcat

En esta tarea se va a implementar un servicio web estilo REST utilizando JAX-RS (soporte de servicios web REST para Java) sobre el servidor de aplicaciones Tomcat.

Primeramente se instalará Tomcat y las bibliotecas necesarias para la implementación de servicios web estilo REST los cuales podrán acceder una base de datos MySQL.

## Instalación de Tomcat con soporte REST

1. Crear una máquina virtual con Ubuntu con al menos 1GB de memoria RAM. Abrir el puerto 8080 para el protocolo TCP.

2. Instalar JDK8 ejecutando los siguientes comandos en la máquina virtual:

sudo apt update
sudo apt install openjdk-8-jdk-headless 3. Descargar la distribución binaria de Tomcat 8 de la siguiente URL (descargar la opción Core "zip"):
https://tomcat.apache.org/download-80.cgi

4. Copiar a la máquina virtual el archivo ZIP descargado anteriormente y desempacarlo utilizando el comando unzip.

5. Eliminar el directorio webapps el cual se encuentra dentro del directorio de Tomcat. Crear un nuevo directorio webapps y dentro de éste crear el directorio ROOT.

NOTA DE SEGURIDAD: Lo anterior se recomienda debido a que se han detectado vulnerabilidades en algunas aplicaciones que vienen con Tomcat, estas aplicaciones se encuentran originalmente instaladas en los directorios webapps y webapps/ROOT.

6. Descargar la biblioteca "Jersey" de la siguiente URL. Jersey es una implementación de JAX-RS la cual permite ejecutar servicios web estilo REST sobre Tomcat:

https://repo1.maven.org/maven2/org/glassfish/jersey/bundles/jaxrs-ri/2.24/jaxrs-ri-2.24.zip

7. Copiar a la máquina virtual el archivo descargado anteriormente, desempacarlo y copiar todos los archivos con extensión “.jar” los cuales se encuentran en los directorios desempacados, al directorio "lib" de Tomcat.

8. Borrar el archivo javax.servlet-api-3.0.1.jar del directorio "lib" de Tomcat (esto debe hacerse ya que existe una incompatibilidad entre Tomcat y Jersey).

9. Descargar el archivo gson-2.3.1.jar de la URL:

https://repo1.maven.org/maven2/com/google/code/gson/gson/2.3.1/gson-2.3.1.jar

10. Copiar el archivo gson-2.3.1.jar al directorio "lib" de Tomcat.

11. Ahora vamos a instalar el driver de JDBC para MySQL. Ingresar a la siguiente URL:

https://dev.mysql.com/downloads/connector/j/

Seleccionar “Platform independent" y descargar el archivo ZIP.

12. Copiar el archivo descargado a la máquina virtual, desempacarlo y copiar el archivo mysql-connector...jar al directorio "lib" de Tomcat.

## Iniciar/detener el servidor Tomcat

1. Para iniciar el servidor Tomcat es necesario definir las siguientes variables de entorno:

export CATALINA_HOME=aquí va la ruta del directorio de Tomcat 8
export JAVA_HOME=aquí va la ruta del directorio donde está el directorio bin que contiene el programa java 2. Iniciar la ejecución de Tomcat:

sh $CATALINA_HOME/bin/catalina.sh start 3. Para detener Tomcat se deberá ejecutar el siguiente comando:

sh $CATALINA_HOME/bin/catalina.sh stop

Notar que Tomcat se debe ejecutar sin permisos de administrador (no usar "sudo"), lo cual es muy importante para prevenir que algún atacante pueda entrar a nuestro sistema con permisos de super-usuario.

## Iniciar Tomcat cuando encienda la máquina virtual

1. Crear el archivo /etc/rc.local ejecutando el siguiente comando:

sudo vi /etc/rc.local 2. Agregar al archivo lo siguiente:

#!/bin/bash
iptables -t nat -A OUTPUT -o lo -p tcp --dport 80 -j REDIRECT --to-port 8080
runuser -l ubuntu -c 'export JAVA_HOME=/usr;export CATALINA_HOME=/home/ubuntu/apache-tomcat-8.5.63;sh $CATALINA_HOME/bin/catalina.sh start'
exit 0

En este caso:

ubuntu es un usuario sin permisos de administrador, reemplazar este usuario con el nombre del usuario definido cuando se creó la máquina virtual.
/home/ubuntu/apache-tomcat-8.5.63 es la ruta absoluta del directorio donde se encuentra Tomcat.
/usr es la ruta absoluta donde se encuentra el JDK.3. Guardar el archivo.

4. Ejecutar el siguiente comando para hacer ejecutable el archivo /etc/rc.local:

sudo chmod +x /etc/rc.local
Ahora cada vez que encienda la máquina virtual iniciará automáticamente Tomcat.

## Instalación de MySQL

1. Actualizar los paquetes en la máquina virtual ejecutando el siguiente comando:

sudo apt update

2. Instalar el paquete default de MySQL:

sudo apt install mysql-server

3. Ejecutar el script de seguridad:

sudo mysql_secure_installation
Press y|Y for Yes, any other key for No: N
New password: aquí-va-la-contraseña-de-root-en-mysql
Re-enter new password: aquí-va-la-contraseña-de-root-en-mysql
Remove anonymous users? (Press y|Y for Yes, any other key for No) : Y
Disallow root login remotely? (Press y|Y for Yes, any other key for No) : Y
Remove test database and access to it? (Press y|Y for Yes, any other key for No) : Y
Reload privilege tables now? (Press y|Y for Yes, any other key for No) : Y

4. Ejecutar el monitor de MySQL:

sudo mysql

5. Ejecutar el siguiente comando SQL para modificar la contraseña de root:

ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'aquí-va-la-contraseña-de-root-de-mysql'; 6. Actualizar los privilegios:
FLUSH PRIVILEGES; 7. Ejecutar el siguiente comando para salir del monitor de MySQL:
quit

## Crear un usuario en MySQL

1. Ejecutar el monitor de MySQL:

mysql -u root -p 2. Crear un usuario, por ejemplo "hugo":

create user hugo@localhost identified by 'aquí-va-la-contraseña-del-usuario-hugo'; 3. Otorgar todos los permisos al usuario "hugo" sobre la base de datos "servicio_web":

grant all on servicio_web.\* to hugo@localhost; 4. Ejecutar el siguiente comando para salir del monitor de MySQL:

quit

## Crear la base de datos

1. Ejecutar el monitor de MySQL (notar que ahora se utiliza el usuario "hugo"):

mysql -u hugo -p 2. Crear la base de datos "servicio_web":

create database servicio_web;

3. Conectar a la base de datos creada anteriormente:

use servicio_web;

4. Crear las tablas "usuarios" y "fotos_usuarios", así mismo, se crea una regla de integridad referencial y un índice único:

create table usuarios
(
id_usuario integer auto_increment primary key,
email varchar(100) not null,
nombre varchar(100) not null,
apellido_paterno varchar(100) not null,
apellido_materno varchar(100),
fecha_nacimiento datetime not null,
telefono bigint,
genero char(1)
);
create table fotos_usuarios
(
id_foto integer auto_increment primary key,
foto longblob,
id_usuario integer not null
);
alter table fotos_usuarios add foreign key (id_usuario) references usuarios(id_usuario);
create unique index usuarios_1 on usuarios(email);

5. Salir del monitor de MySQL:

quit

## Compilar, empacar y desplegar el servicio web (versión JSON)

1. Descargar de la plataforma y desempacar el archivo Servicio.zip.

2. Definir la variable de ambiente CATALINA_HOME:

export CATALINA_HOME=aquí va la ruta completa del directorio de Tomcat 8 3. Cambiar al directorio donde se desempacó el archivo Servicio.zip (en ese directorio se encuentra los directorios "servicio_url" y "servicio_json").

4. Compilar la clase Servicio.java:

javac -cp $CATALINA_HOME/lib/javax.ws.rs-api-2.0.1.jar:$CATALINA_HOME/lib/gson-2.3.1.jar:. servicio_json/Servicio.java

5. Editar el archivo "context.xml" que está en el directorio "META-INF" y definir el username de la base de datos y el password correspondiente (el usuario que fue creado en el paso 2 de la sección Crear un usuario en MySQL).

6. Ejecutar los siguientes comandos para crear el servicio web para Tomcat (notar que los servicios web para Tomcat son archivos JAR con la extensión .war):

rm WEB-INF/classes/servicio*json/*
rm WEB-INF/classes/servicio*url/*
cp servicio_json/\*.class WEB-INF/classes/servicio_json/.
jar cvf Servicio.war WEB-INF META-INF 7. Para desplegar (deploy) el servicio web, copiar el archivo Servicio.war al directorio "webapps" de Tomcat:

cp Servicio.war $CATALINA_HOME/webapps/.
Notar que Tomcat desempaca automáticamente los archivos con extensión .war que se encuentran en el directorio webapps de Tomcat.

Para eliminar el servicio web se deberá eliminar el archivo "Servicio.war" y el directorio "Servicio", en éste orden:

rm -rf $CATALINA_HOME/webapps/Servicio.war $CATALINA_HOME/webapps/Servicio
Cada vez que se modifique el archivo Servicio.java se deberá compilar, generar el archivo Servicio.war, borrar el archivo Servicio.war y el directorio Servicio del directorio webapps de Tomcat, y copiar nuevamente el archivo Servicio.war al directorio webapps de Tomcat.

## Publicar el cliente en Tomcat

1. Copiar el archivo usuario_sin_foto.png al subdirectorio webapps/ROOT de Tomcat.

Notar que todos los archivos que se encuentran en el directorio webapps/ROOT de Tomcat son accesibles públicamente.

Para probar que Tomcat esté en línea y el puerto 8080 esté abierto, ingresar la siguiente URL en un navegador:

http://ip-de-la-máquina-virtual:8080/usuario_sin_foto.png 2. Copiar el archivo WSClient.js al directorio webapps/ROOT de Tomcat.

3. Copiar los archivos prueba_json.html y prueba_url.html al directorio webapps/ROOT de Tomcat.

Probar el servicio web utilizando el cliente HTML-Javascript

1. Utilizando un teléfono inteligente o una tableta, ingresar la siguiente URL en un navegador:

http://ip-de-la-máquina-virtual:8080/prueba_json.html 2. Dar clic en el botón “Alta usuario” para dar de alta un nuevo usuario. Capturar los campos y dar clic en el botón “Alta”. No ingresar datos personales.

3. Intentar dar de alta otro usuario con el mismo email (se deberá mostrar una ventana de error indicando que el email ya existe).

4. Dar clic en el botón “Consulta usuario” para consultar el usuario dado de alta en el paso 2. Capturar el email y dar clic en el botón “Consulta”.

5. Modificar algún dato del usuario y dar clic en el botón “Modifica”.

6. Recargar la página actual y consultar el usuario modificado, para verificar que la modificación se realizó.

7. Dar clic en el botón “Borra usuario” para borrar el usuario. Capturar el email del usuario borrado y dar clic en el botón “Consulta”.

## Compilar, empacar y desplegar el servicio web (versión URL)

Anteriormente se implementó el servicio web que consume JSON, ahora se implementará y probará la versión del servicio web que consume URL.

1. Cambiar al directorio donde se encuentran los subdirectorios servicio_url y servicio_json.

2. Desinstalar el servicio web ejecutando el siguiente comando:

rm -rf $CATALINA_HOME/webapps/Servicio.war $CATALINA_HOME/webapps/Servicio 3. Compilar el servicio web:

javac -cp $CATALINA_HOME/lib/javax.ws.rs-api-2.0.1.jar:$CATALINA_HOME/lib/gson-2.3.1.jar:. servicio_url/Servicio.java 4. Crear el archivo Servicio.war:

rm WEB-INF/classes/servicio*url/*
rm WEB-INF/classes/servicio*json/*
cp servicio_url/\*.class WEB-INF/classes/servicio_url/.
jar cvf Servicio.war WEB-INF META-INF 5. Copiar el archivo Servicio.war al directorio webapps de Tomcat:

cp Servicio.war $CATALINA_HOME/webapps/. 6. Utilizando un teléfono inteligente o una tableta, ingresar la siguiente URL en un navegador:

http://ip-de-la-máquina-virtual:8080/prueba_url.html 7. Repetir las pruebas realizadas en la sección Probar el servicio web utilizando el cliente HTML-Javascript.

8. Crear una imagen de la máquina virtual conservando el usuario y posteriormente eliminar la máquina virtual y los recursos asociados (no borrar la imagen y ni el grupo de recursos que contiene la imagen). La imagen se utilizará para realizar otras tareas.
