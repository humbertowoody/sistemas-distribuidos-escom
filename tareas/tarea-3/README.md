# Tarea 3. Chat multicast

Utilizando ChatGPT desarrollar un solo programa en Java en modo consola que implemente un chat utilizando comunicación multicast mediante datagramas.

Se deberá ejecutar el programa en una máquina virtual con Windows Server 2012 en Azure.

Se deberá pasar como parámetro al programa el nombre del usuario que va escribir en el chat.

El programa deberá hacer lo siguiente:

El programa creará un thread el cual recibirá los mensajes del resto de los nodos. Cada mensaje recibido será desplegado en la pantalla. El thread desplegará el mensaje que envía el mismo nodo.
En el método main(), dentro de un ciclo infinito:
Se desplegará el siguiente prompt: "Escribe tu mensaje: " (sin las comillas), entonces se leerá del teclado el mensaje.
Se deberá enviar el mensaje a los nodos que pertenecen al grupo identificado por la IP 239.0.0.0 a través del puerto 50000. El mensaje a enviar en el datagrama deberá tener la siguiente forma:
         nombre_usuario--->mensaje_ingresado

Dónde nombre_usuario es el nombre del usuario que pasó como parámetro al programa, "--->" es un separador y mensaje_ingresado es el mensaje que el usuario ingresó por el teclado.

Debido a que el protocolo UDP no garantiza el orden en que se reciben los mensajes, no se deberá enviar la longitud del mensaje y luego el mensaje; se deberá utilizar una longitud de mensaje fija. Al desplegar el mensaje recibido se deberán eliminar los espacios adicionales a la derecha (si los hay).

Para demostrar el programa se deberá utilizar los siguientes usuarios: Hugo, Paco y Luis.

Se deberá ejecutar la siguiente conversación (la cual aparece en negritas) en tres ventanas de comandos (cmd) en la máquina virtual con Windows Server 2012. En la primera ventana escribirá Hugo, en la segunda ventana escribirá Paco y en la tercera ventana escribirá Luis:

Hugo escribirá:
hola a todos

Paco escribirá:
hola Hugo

Luis escribirá:
hola Paco
hola Hugo

Paco escribirá:
¿alguien sabe dónde será el concierto?

Hugo escribirá:
será en la plaza central

Paco escribirá:
¿a qué hora?

Luis escribirá:
a las 8 PM

Paco escribirá:
gracias, adiós

Luis debe escribir:
adiós Paco

Los signos de interrogación y las letras acentuadas deberán desplegarse correctamente en la ventana de comandos de Windows.

Se deberá subir a la plataforma:

Un archivo texto con el código fuente del programa desarrollado
Un reporte de la tarea en formato PDF con portada, desarrollo y conclusiones. El reporte deberá incluir el texto (no capturas de pantalla) de la conversación sostenida con ChatGPT.
El reporte deberá incluir las capturas de pantalla de la ejecución del programa, se deberá incluir la captura de pantalla correspondiente a cada paso del procedimiento de creación de la máquina virtual.
No se admitirá la tarea si el reporte no incluye las pantallas correspondientes a cada paso del procedimiento de creación de la máquina virtual.

El nombre de la máquina virtual deberá ser: "T3-" concatenando el número de boleta del alumno o alumna, por ejemplo, si el número de boleta es 12345678, entonces la máquina virtual deberá llamarse: T3-12345678. No se admitirá la tarea si la máquina virtual no se nombre como se indicó anteriormente.
Recuerden que se debe eliminar la máquina virtual cuando ya no se use, con la finalidad de ahorrar el saldo de sus cuentas de Azure.

Valor de la tarea: 25% (1.75 puntos de la primera evaluación parcial)