# Tarea 5. Multiplicación de matrices utilizando objetos distribuidos

Utilizando ChatGPT se deberá desarrollar un sistema que calcule el producto de dos matrices rectangulares utilizando Java RMI, tal como se explicó en clase.

Sea la matriz A con dimensiones NxM, y la matriz B con dimensiones MxN.

Sea C = AxB.

Se deberán ejecutar dos casos:

- N=9, M=4 se deberá desplegar la matriz C y el checksum de la matriz C.
- N=900, M=400 se deberá desplegar el checksum de la matriz C.
- Los elementos de las matrices A, B y C deberán ser de tipo float y el checksum deberá ser de tipo double.

Se deberá inicializar las matrices A y B de la siguiente manera:

`A[i][j]= 2 * i + 3 * j`

`B[i][j] = 3 * i - 2 * j`

Sea BT la transpuesta de la matriz B.

Se deberá dividir las matrices A y BT en nueve partes iguales, por tanto la matriz C estará dividida en 81 partes:

La multiplicación de las matrices Ai x BTj deberá realizarse renglón por renglón, tal como vimos en la clase "Jerarquía de memoria".

Se deberá crear tres máquinas virtuales con Ubuntu en el mismo grupo de recursos, de acuerdo a la siguiente topología:

La máquina virtual 0 (nodo 0) ejecutará el cliente RMI el cual hará los siguiente:

- Inicializar las matrices A y B.
- Obtener la matriz BT.
- Obtener las matrices Ai (i=1,2,...9) y BTi (i=1,2...9)
- Utilizando RMI obtener las matrices C1, C2, ... C27 invocando el método multiplica_matrices() el cual ejecutará en el nodo 0.
- Utilizando RMI obtener las matrices C28, C29, ... C54 invocando el método multiplica_matrices() el cual ejecutará en el nodo 1.
- Utilizando RMI obtener las matrices C55, C56, ... C81 invocando el método multiplica_matrices() el cual ejecutará en el nodo 2.
- Obtener la matriz C a partir de las matrices C1, C2,...C81.
- Para el caso N=9 y M=4 desplegar la matriz C.
- Calcular y desplegar el checksum de la matriz C.
- Los nodos 0, 1 y 2 ejecutarán el servidor RMI y el rmiregistry.

El cliente RMI deberá utilizar threads para invocar el método remoto multiplica_matrices() de manera que los servidores RMI calculen los productos en paralelo.

Se deberá subir a la plataforma el código fuente del sistema desarrollado (la interface, la clase, el servidor y el cliente) y un reporte de la tarea en formato PDF con portada, desarrollo y conclusiones.

El reporte PDF deberá incluir las capturas de pantalla de la compilación y ejecución del programa, se deberá incluir la captura de pantalla correspondiente a cada paso de la creación de la máquina virtual 0.

No se admitirá la tarea si no incluye las pantallas correspondientes a cada paso del procedimiento de creación de la máquina virtual 0.

El reporte PDF deberá incluir la conversación realizada con ChatGPT.

El nombre de cada máquina virtual deberá ser: "T5-" concatenando el número de boleta del alumno o alumna, un guion y el número de máquina virtual, por ejemplo, si el número de boleta es 12345678, entonces la máquina virtual 0 deberá llamarse: T5-12345678-0, la máquina virtual 1 deberá llamarse T5-12345678-1, y así sucesivamente. No se admitirá la tarea si las máquinas virtuales no se nombran como se indicó anteriormente.

Los checksum deben ser correctos.

Recuerden que deben eliminar las máquinas virtuales cuando no las usen, con la finalidad de ahorrar el saldo de sus cuentas de Azure for Students.
