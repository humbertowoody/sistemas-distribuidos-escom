# Conceptos Fundamentales.

A continuación se describen algunos conceptos que se utilizarán a lo largo del
semestre.

## Comunicación Cliente - Servidor.

- Comunicación Unicast: El que inicia la comunicación en Unicast es el cliente.
- Comunicación Broadcast: El broadcast es cuando una computadora le envía el
  mismo mensaje a todas las computadoras que están en su red.
- Comunicación Multicast: Es cuando una computadora envía el mismo mensaje a
  una o varias computadoras dentro de la misma red. El broadcast es una situación
  particular de Multicast.

## Socket, dirección IP y puerto.

- IP: Una dirección IP versión 4 es un número de 32 bits dividio en cuatro
  bytes, cada byte puede tener un valor entre 0 y 255.
- Puerto: es un número entre 0 y 65535. En Unix (Linux) los puertos del
  1 al 1023 solo pueden ser abiertos utilizando permisos de super usuario.
- Socket: Un socket es un punto final (endpoint) de un enlace de dos vías que
  comunica dos procesos que ejecutan en la red. Un endopint es la combinación
  de una dirección IP y un puerto.
