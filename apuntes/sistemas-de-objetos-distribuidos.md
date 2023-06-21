# Sistemas de Objetos Distribuidos

En esta sección vamos a hablar sobre los sistemas que distribuyen funciones a
nivel de capa de aplicación sin llegar a la generalización que ofrecen las
interfaces REST y JSON como esquema de serialización de información; aquí
vamos a un nivel más cercano a la ejecución del proceso.

## Paradigma de paso de mensajes

El paradigma de paso de mensajes es el modelo natural para el desarrollo de
sistemas distribuidos ya que reproduce la comunicación entre personas.

El paradigma de paso de mensajes es orientado a datos.

## Objetos locales

Un objeto encapsula variables (campos) y funciones (métodos). Las variables
van a guardar el estado del objeto y los métodos van a permitir modificar y
acceder a estos datos.

Un objeto local es aquél cuyos métodos son invocados por algún proceso local.

Los objetos locales comparten el espacio de direcciones, en otras palabras,
los objetos locales residen en la misma memoria.

## Objetos remotos

Un objeto remoto es aquel cuyos métodos son invocados por procesos remotos,
es decir procesos que ejecutan en una computadora ajena del proceso inicial.

El paradigma de objetos distribuidos es orientado a la acción ya que se fundamente
en la acción realizada por los métodos remotos.

## Remote Method Invocation (RMI)

En un sistema que utiliza RMI existe un proceso llamado **registry** el cual hace
las funciones de servidor de nombres.

En cada nodo hay un proceso **servidor** el cual registra en el servidor de
nombres los objetos que eportará. Cada objeto exportado por el servidor
será identificado mediante una URL.

Para acceder a un objeto remoto, el proceso **cliente** consulta al servidor
de nombres utilizando la URL, si el objeto es encontrado, entonces el **servidor
de nombres** regresa al cliente una referencia que apunta al objeto remoto.

Entonces el proceso cliente utiliza la referencia para invocar los métodos
del objeto remoto, los cuales se ejecutan en el servidor.
