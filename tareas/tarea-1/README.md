# Tarea 1. Sistema distribuido que verifica si un número es primo

Desarrollar un sistema distribuido en Java de acuerdo a las siguientes especificaciones:

## Servidor A

1. Desarrollar un servidor TCP (servidor A) multithread el cual recibirá tres números enteros de 64 bits (NÚMERO, NÚMERO_INICIAL y NÚMERO_FINAL).

El servidor A deberá dividir el NÚMERO entre cada número n desde NÚMERO_INICIAL hasta NÚMERO_FINAL, si hay un número n que divide a NÚMERO, entonces el servidor A regresará al cliente la palabra "DIVIDE", de otra manera regresará al cliente "NO DIVIDE".

Se sugiere utilizar el operador módulo % para determinar si un número n divide a NÚMERO, esto es:

Si NÚMERO%n==0 entonces n divide a NÚMERO

## Servidor B

2. Desarrollar un servidor TCP (servidor B) el cual brindará el servicio de determinar si un número entero mayor que 1 es primo o no.

Utilizando el programa cliente que se describe en el inciso 3, se enviará al servidor B un número entero de 64 bits.

Para determinar si NÚMERO es primo, el servidor B dividirá el intervalo [2,NÚMERO-1] en tres intervalos, entonces el servidor B se conectará a tres instancias del servidor A. El servidor B enviará, a cada instancia del servidor A, NÚMERO y el intervalo [NÚMERO_INICIAL, NÚMERO_FINAL] correspondiente.

Cada instancia del servidor A deberá ejecutar en una ventana independiente de Windows o Linux.

Si las tres instancias del servidor A regresan "NO DIVIDE" al servidor B, entonces el servidor B regresará al cliente las palabras "ES PRIMO". Si alguna instancia del servidor A regresa "DIVIDE", entonces el servidor B regresará al cliente las palabras "NO ES PRIMO".

Por ejemplo, si el servidor B recibe el número 1234567811, entonces el servidor B enviará los siguientes números a las instancias:

Instancia 1:

1234567811,2,411522603
Instancia 2:
1234567811,411522604,823045206
Instancia 3:
1234567811,823045207,1234567810
Entonces cada servidor A regresará "NO DIVIDE", por tanto, el servidor B regresará al cliente "ES PRIMO".

Se sugiere utilizar las siguientes fórmulas para obtener los tres intervalos.

Sea K=NÚMERO/3 (la división es entera), entonces:

Intervalo	NÚMERO INICIAL	NÚMERO FINAL
1	2	K
2	K+1	2*K
3	2*K+1	NÚMERO-1

## Cliente

3. Desarrollar un cliente TCP que reciba como parámetro un número entero de 64 bits y lo envíe al servidor B, entonces el cliente deberá recibir del servidor B una string. El cliente desplegará la string que recibió del servidor B.