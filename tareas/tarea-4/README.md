# Tarea 4. Transferencia de archivos utilizando sockets seguros

Desarrollar los siguientes programas en lenguaje Java utilizando ChatGPT:

1. Servidor multi-thread con sockets seguros, el cual recibirá del cliente dos tipos de peticiones:

1.1 La petición GET seguida del nombre de un archivo (sin directorios). El servidor deberá leer el archivo del disco local, si el archivo se pudo leer el servidor deberá enviar al cliente OK, la longitud del archivo y el contenido archivo, de otra manera deberá enviar al cliente ERROR.

1.2 La petición PUT seguida del nombre de un archivo (sin directorios), la longitud del archivo y el contenido del archivo. El servidor deberá escribir el archivo en el disco local, si el archivo se pudo escribir en el disco el servidor enviará al cliente OK, de otra manera enviará ERROR.

2. Cliente PUT. Cliente con sockets seguros, el cual deberá recibir como parámetros la IP del servidor, el puerto que escucha el servidor y el nombre del archivo a enviar.

Entonces el cliente leerá el archivo del disco local, si puede leer el archivo se conectará al servidor y enviará una petición PUT, el nombre del archivo a enviar, la longitud del archivo y el contenido del archivo. Si el cliente no puede leer el archivo del disco local desplegará un mensaje de error indicando esta situación.

El cliente deberá esperar la respuesta del servidor, si el servidor responde OK el cliente deberá desplegar un mensaje indicando que el archivo fue recibido por el servidor con éxito, de otra manera desplegará un mensaje de error indicando que el servidor no pudo escribir el archivo en el disco local.

3. Cliente GET. Cliente con sockets seguros, el cual deberá recibir como parámetros la IP del servidor, el puerto que escucha el servidor y el nombre del programa a recibir.

Entonces el cliente se conectará al servidor y enviará la petición GET y el nombre del archivo.

El cliente deberá esperar la respuesta del servidor, si el servidor responde OK el cliente deberá recibir la longitud del archivo y el contenido del archivo. Entonces el cliente deberá escribir el archivo en el disco local, si puede escribir el archivo deberá desplegar un mensaje indicando que el archivo se recibió con éxito, de otra manera deberá desplegar un mensaje de error.

El servidor deberá ejecutar en una máquina virtual con Ubuntu en Azure.

El nombre de la máquina virtual deberá ser: "T4-" concatenando el número de boleta del alumno o alumna, por ejemplo, si el número de boleta es 12345678, entonces la máquina virtual deberá llamarse: T4-12345678. No se admitirá la tarea si los nodos no se nombran como se indicó anteriormente.

Recuerden que deben eliminar la máquina virtual cuando no la usen, con la finalidad de ahorrar el saldo de sus cuentas de Azure.

Se deberá subir a Moodle el código fuente (en formato texto) de los clientes y el código fuente del servidor.

Se deberá subir a Moodle un reporte en formato PDF incluyendo portada, las capturas de pantalla de la compilación y ejecución de los clientes y el servidor. Así mismo, el reporte deberá incluir las conclusiones.

Se deberá probar el envío de un archivo mediante el cliente PUT y la recepción de un archivo mediante el cliente GET.

El reporte deberá incluir captura de pantalla de cada paso correspondiente a la creación de la máquina virtual. No se admitirá la tarea si no incluye todas las capturas de pantalla correspondientes a la creación de la máquina virtual.

El reporte deberá incluir la conversación sostenida con ChatGPT.
Valor de la tarea: 25% (1.75 puntos de la primera evaluación parcial)
