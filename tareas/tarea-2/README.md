# Tarea 2. Multiplicación de matrices distribuida utilizando paso de mensajes

En esta tarea se deberá desarrollar un solo programa en Java el cual calculará el producto de dos matrices cuadradas en forma distribuida utilizando tres máquinas virtuales de Azure.

Se deberá escribir el programa en su totalidad utilizando ChatGPT, o bien, se podrá escribir partes del programa utilizando ChatGPT y después integrar las partes manualmente.

Sean A, B y C matrices cuadradas con elementos de tipo double, la dimensión de las matrices es N. C=AxB

Se deberá ejecutar dos casos:

N=12, desplegar las matrices A, B y C y el checksum de la matriz C.
N=3000, desplegar el checksum de la matriz C.
El checksum de la matriz C se calculará como la suma de todos elementos de la matriz. El checksum deberá ser de tipo double.

Se deberá inicializar las matrices de la siguiente manera:
A[i][j]= 2*i+j
B[i][j] = 3*i-j

Donde A[i][j] y B[i][j] son los elementos Ai,j y Bi,j respectivamente.

El programa deberá ejecutar en una computadora local (nodo 0) con Windows, Linux o MacOS y en tres máquinas virtuales con Ubuntu (nodos 1, 2 y 3).

El nombre de cada máquina virtual deberá ser: "T2-" concatenando el número de boleta del alumno o alumna, un guion y el número de nodo, por ejemplo, si el número de boleta es 12345678, entonces el nodo 1 deberá llamarse: T2-12345678-1, el nodo 2 deberá llamarse T2-12345678-2, y el nodo 3 deberá llamarse T2-12345678-3. No se admitirá la tarea si los nodos no se nombran como se indicó anteriormente.

Recuerden que deben eliminar las máquinas virtuales cuando no las usen, con la finalidad de ahorrar el saldo de sus cuentas de Azure.

¿Cómo realizar la multiplicación de matrices en forma distribuida?

Sea BT la transpuesta de la matriz B. Se deberá dividir las matrices A y BT en tres partes de igual tamaño, por tanto la matriz C estará dividida en 9 partes tal como se muestra en la siguiente figura:



Debido a que las matrices se guardan por renglones en la memoria, para aprovechar la localidad espacial el producto se realizará renglón por renglón tal como vimos en clase. Entonces las matrices Ci se obtienen de la siguiente manera:

C1=A1xBT1
C2=A1xBT2
C3=A1xBT3
C4=A2xBT1
C5=A2xBT2
C6=A2xBT3
C7=A3xBT1
C8=A3xBT2
C9=A3xBT3

Considere la siguiente topología:




El nodo 0 será la computadora local. El nodo 0 inicializará las matrices A y B, obtendrá la transpuesta de la matriz B y enviará las matrices Ai y BTj a los nodos 1, 2 y 3.

El nodo 1 calculará los productos C1, C2 y C3; el nodo 2 calculará los productos C4, C5 y C6; el nodo 3 calculará los productos C7, C8 y C9.

Se deberá pasar como parámetro al programa el número de nodo.

Se deberá implementar los siguientes algoritmos:

Nodo 0:

1. Inicializar las matrices A y B.
2. Transponer la matriz B. Dejar la transpuesta en la misma matriz B.
3. Enviará la matriz A1 al nodo 1.
4. Enviará las matrices BT1, BT2 y BT3 al nodo 1.
5. Enviará la matriz A2 al nodo 2.
6. Enviará las matrices BT1, BT2 y BT3 al nodo 2.
7. Enviará la matriz A3 al nodo 3.
8. Enviará las matrices BT1, BT2 y BT3 al nodo 3.
9. Recibirá las matrices C1, C2 y C3 del nodo 1.
10. Recibirá las matrices C4, C5 y C6 del nodo 2.
11. Recibirá las matrices C7, C8 y C9 del nodo 3.
12. Calcular el checksum de la matriz C.
13. Desplegar el checksum de la matriz C.
14. Si N=12 entonces desplegar las matrices A, B y C.

Nodo 1:

1. Recibirá la matriz A1 del nodo 0.
2. Recibirá las matrices BT1, BT2 y BT3 del nodo 0.
3. Calcular las matrices C1, C2 y C3.
4. Enviará las matrices C1, C2 y C3 al nodo 0.

Nodo 2:

1. Recibirá la matriz A2 del nodo 0.
2. Recibirá las matrices BT1, BT2 y BT3 del nodo 0.
3. Calcular las matrices C4, C5 y C6.
4. Enviará las matrices C4, C5 y C6 al nodo 0.

Nodo 3:

1. Recibirá la matriz A3 del nodo 0.
2. Recibirá las matrices BT1, BT2 y BT3 del nodo 0.
3. Calcular las matrices C7, C8 y C9.
4. Enviará las matrices C7, C8 y C9 al nodo 0.