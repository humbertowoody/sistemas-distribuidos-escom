# Sincronización y Coordinación

- [Sincronización y Coordinación](#sincronización-y-coordinación)
  - [Sincronización de threads en Java](#sincronización-de-threads-en-java)
  - [¿Cuándo se requiere sincronizar?](#cuándo-se-requiere-sincronizar)
  - [Husos Horarios](#husos-horarios)
  - [Sincronización de relojes](#sincronización-de-relojes)
  - [Relojes físicos](#relojes-físicos)
  - [Segundos solares](#segundos-solares)
  - [Segundos atómicos](#segundos-atómicos)
  - [Tiempo atómico internacional](#tiempo-atómico-internacional)
  - [Tiempo universal coordinado UTC](#tiempo-universal-coordinado-utc)
  - [Sincronización de relojes físicos](#sincronización-de-relojes-físicos)
  - [Network Time Protocol (NTP)](#network-time-protocol-ntp)
  - [Algoritmo de Sincronización de Relojes de Berkeley](#algoritmo-de-sincronización-de-relojes-de-berkeley)
  - [Relación Happens-Before](#relación-happens-before)
  - [Reloj lógico](#reloj-lógico)
  - [Algoritmo de sincronización de relojes lógicos de Lamport](#algoritmo-de-sincronización-de-relojes-lógicos-de-lamport)
  - [Tipos de bloqueos](#tipos-de-bloqueos)
    - [Interbloqueo (Deadlock)](#interbloqueo-deadlock)
    - [Algoritmo centralizado de exclusión mutua](#algoritmo-centralizado-de-exclusión-mutua)
    - [Algoritmo descentrealizado para exclusión mutua](#algoritmo-descentrealizado-para-exclusión-mutua)
  - [Algoritmos de Elección](#algoritmos-de-elección)
    - [Algoritmo de elección del abusón (bully)](#algoritmo-de-elección-del-abusón-bully)
    - [Algoritmo de elección en anillo (token ring)](#algoritmo-de-elección-en-anillo-token-ring)

## Sincronización de threads en Java

- La instrucción `synchronized` se utiliza para sincronizar varios hilos entre
  sí.
- El lock (seguro) garantiza que solo un thread utilize este código.
- El bloque de instrucciones que sólamente pueden ser ejecutados por un solo
  hilo se le llama: **sección crítica**.
- Esta instrucción sirve para ordenar o para garantizar el orden en que los
  hilos interactúan entre sí.
- La innanición es una condición en un proceso o un thread (hilo) _nunca_
  obtienen un lock.

Código de ejemplo para esto:

```java
/**
 * Pequeño ejemplo sobre sincronización de hilos en Java.
 */


class A extends Thread
{
  // Variable estática para compartirla entre todas las instancias de la
  // clase.
  static long n;

  static Object obj = new Object();

  /**
   * La función que ejecutará cada hilo.
   */
  public void run()
  {
    for (int i = 0; i < 100000; i++)
      synchronized(obj)
      {
        n++;
      }
  }

  /**
   * Función principal.
   */
  public static void main(String[] args) throws Exception
  {
    A t1 = new A();
    A t2 = new A();

    t1.start();
    t2.start();
    t1.join();
    t2.join();


    System.out.println("El valor de n es " + n);
  }
}
```

## ¿Cuándo se requiere sincronizar?

El tiempo es una referencia que utilizan los sistemas distribuidos en varias
situaciones.

Supongamos una plataforma de comercio electrónico que funciona a nivel global,
en cada país se tiene un servidor con una base de datos dónde se registran las
compras, incluyendo la fecha y hora en la que se realiza cada compra.

Para consolidar las compras a nivel mundial cada servidor debe enviar los datos
a un servidor central.

Sin embargo, no es posible mantener las compras por fecha debido a dos situaciones:

1. Cada compra se ha registrado con la fecha y hora local.
2. No es posible garantizar que los relojes de los servidores funcionen a la misma velocidad.

Para ilustrar este problema, supongamos que un cliente en México realiza una
compra a las 8pm y un cliente en España realiza una compra a las 2AM del día
siguiente.

¿Quién compró primero?

Aparentemente el cliente en México realizó la compra antes que el cliente en
España debido a que la fecha de compra del cliente en Mëxico es un día anterior
a la fecha de la compra del cliente en España.

Sin embargo, en realidad el cliente en España realizó la compra una hora antes
que el cliente en México, debido a que la diferencia horaria entre México y
España es de 7 horas.

Sin embargo, en realidad el cliente en España realizó la compra una hora antes
que el cliente en México, debido a que la diferencia horaria entre México y España
es de 7 horas.

La solución a este problema es registrar en las bases de datos una fecha y hora
global en lugar de una fecha y hora local. Además los servidores deberán sincronizar
sus relojes internos a una misma hora.

Por otra parte, si los servidores no requieren consolidar las compras, tampoco
será necesario que exista un acuerdo en los tiempos que marcan en sus relojes.

El ejemplo anterior ilustra una regla muy importante de los sistemas distribuidos,
la cual podemos enunciar de la siguiente manera, si dos computadoras no están
conectados entonces no requieren sincronizar sus tiempos.

## Husos Horarios

![Imagen de Husos Horarios de Wikipedia][husos-horarios-img]

- México tieen 3 husos horarios.

## Sincronización de relojes

Sincronizar dos o más relojes significa que los servidores se ponen de acuerdo
en una misma hora.

Notar que un grupo de servidores pueden ponerse de acuerdo en una hora y otro
grupo de servidores puede ponerse de acuerdo en otra hora. Solo si ambos grupos
de servidores se conectan entre sí deberán acordar una hora.

Como vimos previamente, el tiempo es una referencia para establecer un orden en
secuencia de eventos (como serían las compras en una plataforma de comercio
electrónico).

Más adelante veremos que este orden puede establecerse utilizando relojes físicos
(mecanismos que marcan el tiempo real).

## Relojes físicos

En los sistemas digitales, un reloj físico es un circuito que genera pulsos con
un periodo "constante".

En una computadora cada pulso de reloj produce una interrupción en el CPU para
que se actualice un contador de ticks.

Dado que el pulso tiene un periodo "constante", el número de ticks es una medida
del tiempo transcurrido desde que se encendió la computadora.

El siguiente diagrama muestra un reloj físico el cual genera pulsos.

![Imagen pulsos de reloj][imagen-pulsos-reloj]

Cuando la señal cambia de 0 volts a 5 volts se produce una interrupción en el
CPU, entonces se invoca una rutina llamada manejador de interrupción (handler)
la cual incrementa el contador de "ticks".

El contador de ticks de una computadora no es un reloj preciso, dado que:

- Los relojes físicos se construyen utilizando cristales de cuarzo con la
  finalidad de tener un periodo de oscilación constante, sin embargo los cambios
  en la temperatura modifican el periodo del pulso, lo que ocasiona que el reloj
  se adelante o se atrase.
- Cuando se produce la interrupción al CPU, el sistema podría estar ejecutando
  una rutina de mayor prioridad, por tanto la rutina que incrementa los "ticks"
  se bloquea lo que provoca que algunos pulsos de reloj no incrementen la cuenta
  de "ticks".

## Segundos solares

El concepto de tiempo que utilizamos en la práctica se basa en la percepción que
tenemos del día.

Un día es un periodo de luz y obscuridad debido a la rotación de la tierra sobre
su eje.

Dividimos convencionalmente el día en 24 horas, cada hora en 60 minutos y cada
minuto en 60 segundos.

Por tanto, la tierra tarda 86,400 segundos en dar una vuelta sobre su eje, en
términos de velocidad angular estamos hablando de $360/86,400 = 0.00416 \frac{\mathrm{degrees}}{\mathrm{seconds}}$.

A la fracción 1/86400 de día le llamamos **segundo solar**.

Sin embargo, la velocidad angular de la tierra no es constante, debido a que la
rotación de la tierra se está deteniendo muy lentamente.

## Segundos atómicos

- A la frecuencia que produce más cambios de estado en los electrones del átomo
  de Cesio 133 se le llama frecuencia natural de resonancia.
- La frecuencia natural de resonancia del cesio 133 es de 9,192,631,770 ciclos/segundo.
- Entonces se define el segundo atómico como el recíproco de la frecuencia
  natural de resonancia del Cesio 133.
- Los relojes atómicos de Cesio 133 se adelantan o atrasan un segundo cada 300
  millones de años.

Los relojes atómicos de Cesio 133 son extremadamente precisos ya que
independientemente de las condiciones ambientales (temperatura, presión, etc),
se adelantan o atrasan un segundo cada 300 millones de años.

Los relojes atómicos son tan precisos que se han utilizado para probar los
postulados de la teoría general de la relatividad, la cual predice la dilatación
del tiempo debidos a la distorisión que causa la gravedad al espacio-tiempo.

Por ejemplo, utilizando un reloj atómica de Cesio 133 se ha demostrado que el
tiempo no transcurre a la misma velocidad a diferentes altitudes, ya que al
nivel del mar, dónde la gravedad es mayor el tiempo se dilata (transcurre más
lentamente) con respecto.

## Tiempo atómico internacional

- Se define el tiemop atómico internacional (TAI) como el promedio de los segundos atómicos transcurridos desde el 1 de enero de 1958, obtenido de casi 70 relojes de Cesio 133 alrededor del mundo.

## Tiempo universal coordinado UTC

- El tiempo universal coordinado UTC (Coordinated Universal Time), es el estándar
  de tiempo que regula actualmente el tiempo de los relojes a nivel internacional.
- El tiempo UTC ha reemplazado el tiempo medio de Greenwich (GMT).
- El tiempo GMT toma como referencia la posición del sol a medio día.
- Tanto el tiempo GMT como el tiempo UTC consieran el día solar compuesto por
  86,400 segundos.
- Una vez al año, dado al desfase que existe entre los segundos solares y los
  segundos atómicos, UTC se ajusta saltándose (**adelantándose**) un segundo.

## Sincronización de relojes físicos

En los sistemas centralizados el tiempo se obtieen del reloj central, por tanto todos los procesos se sincronizan mediante un solo reloj.

A la diferencia de valores de tiempo de un conjunto de computadoras se le llama distorsión del reloj.

## Network Time Protocol (NTP)

Es el protocolo estándar a ser utilizado para la sincronización del tiempo en
computadoras. Con NTP podemos realizar la sincronización del reloj de nuestra
computadora con un servidor central de NTP, ahí podemos usar una gran variedad
de servidores NTP, algunos son:

- Relojes atómicos
- Grandes empresas
- Gobiernos

## Algoritmo de Sincronización de Relojes de Berkeley

- Se tienen 3 servidores: A, B y C.
- El algoritmo primero envía la hora de A a todos los cliente (B y C).
- Los clientes retornan la diferencia de la hora de A vs. sus horas internas.
- Entonces A calcula el promedio de las horas y diferencias y suma el promedio.
- Así, el servidor A envía de nuevo su hora y todos los demás copian la hora de A.

## Relación Happens-Before

Leslie Lamport define la relación $A \rightarrow B$ como:

1. Si $A$ y $B$ son eventos del mismo proceso y $A$ ocurre antes que $B$, entonces
   $A \rightarrow B$.
2. Si $A$ es el envío de un mensaje y $B$ es la recepción del mismo mensaje, entonces
   $A \rightarrow B$.

La relación **happens-before** tiene las siguientes propiedades:

- **Transitiva**: Si $A \rightarrow B$ y $B \rightarrow C$, entonces $A \rightarrow C$.
- **Anti-simétrica**: Si $A \rightarrow B$ entonces no $B \rightarrow A$.
- **Irreflexiva**: No $A \rightarrow A$. para cada evento $A$.

> Todas las funciones son relaciones pero no todas las relaciones son funciones.

## Reloj lógico

Se define un reloj lógico $C_i$ para un procesador $P_i$ como unfa función $C_i(A)$
la cual asigna un número al evento $A$.

Un reloj lógico se implementa como un contador sin una relación directa con un
reloj físico, como es el caso de los contadores de "ticks" de las comopoutadoras
digitales.

Dados los eventos $A$ y $B$, si el evento $A$ ocure antes que el evento $B$,
entonces $C_i(A) < C_i(B)$.

Esto significa que si $A$ _happens-before_ $B$ entonces el evento $A$ ocurre
en un tiempo lógico menor al tiempo lógico en que ocurre el evento $B$.

## Algoritmo de sincronización de relojes lógicos de Lamport

- Cuando enviamos un mensaje, el mensaje siempre debe incluir la hora local del
  reloj dónde se originó el mensaje.
- El que recibe el mensaje, si su reloj está desfasado, ajusta su reloj y envía
  un mensaje de confirmación.

## Tipos de bloqueos

Existen dos tipos de bloqueos, los bloqueos exclusivos y bloqueos compartidos.

Dependiendo del recurso en particular, se puede utilizar solo bloqueos exclusivos
o bien, bloques exclusivos y compartidos.

Si un procesador bloquea un recurso de manera exclusiva, ningún procesador puede
bloquear el recurso de manera exclusiva o compartida.

Si un procesador bloquea un recurso de manera compartida, otros procesadores
pueden bloquear el mismo recurso de manera compartida, es decir, múltiples
bloqueos compartidos sobre el mismo recurso pueden co-existir.

Si un recurso tiene uno o más bloqueos compartidos, ningún procesador puede
obtener un bloqueo exclusivo sobre el mismo recurso.

Para el acceso a dispositivos de almacenamiento (memorias, discos, etc), los
bloqueos exclusivos se utilizan para proteger operaciones de escritura,
mientras que los bloqueos compartidos se utilizan para proteger lecturas.

Por ejemplo, un **bloqueo compartido** sobre un archivo en el disco permite que
varios procesadorespuedan leer el archivo, pero no permite que ningún procesador
escriba el archivo.

Por otra parte, un **bloqueo esclusivo** sobre el mismo archivo garantiza que
solo un procesador pueda escribir y ningún otro procesador puede leer o escribir
el archivo.

### Interbloqueo (Deadlock)

En un sistema distribuido las computadoras compiten por adquirir el bloqueo sube
un recurso.

En una situación de competencia existe la posibilidad de que una o varias
computadoras nunca adquieran el recurso debido a deficiencias en el algoritmo
de exclusión.

Cuando una computadora no puede adquirir el recurso, se dice que se encuentra
en un estado de **bloqueo**.

El interbloqueo ocurre cuando dos procesadores se bloquean mutuamente, es decir,
cuando dos procesadores se bloquean en orden inverso.

Por ejemplo, si el procesador $P_1$ bloquea un recurso $R_1$ y luego el procesador
$P_2$ bloquea el recurso $R_2$, y luego el procesador $P_1$ intenta bloquear el
recurso $R_2$ y el procesador $P_2$ intenta bloquear el recurso $R_1$, entonces
ambos procesadores se bloquean mutuamente.

### Algoritmo centralizado de exclusión mutua

El algoritmo centralizado de exclusión mutua consiste en un proceso central
que se encarga de otorgar los bloqueos.

Este algoritmo implementa una cola tipo FIFO, por lo que los nodos que solicitan
el bloqueo se encolan y el proceso central otorga el bloqueo a los nodos en el
orden en que fueron encolados.

El algoritmo centralizado de exclusión mutua tiene las siguientes ventajas:

- Es fácil de implementar.
- Es fácil de entender.
- Es fácil de depurar.
- Es fácil de extender.

El algoritmo centralizado de exclusión mutua tiene las siguientes desventajas:

- Es un punto de falla.
- Es un cuello de botella.
- Es un único punto de control.
- Es un único punto de coordinación.

Para que este algoritmo falle, o caiga en una situación de interbloqueo, es
necesario que el proceso central falle o que el nodo que solicitó el bloqueo
falle.

### Algoritmo descentrealizado para exclusión mutua

La desventaja del algoritmo centralizado de exclusión mutua es que es el coordinador
puede saturarse si recibe muchas peticiones, por esta razón es mejor implementar un algoritmo descentralizado.

## Algoritmos de Elección

Son los algoritmos que se emplean para determinar el coordinador de nuestro
conjunto de nodos.

### Algoritmo de elección del abusón (bully)

El algoritmo supone que los nodos están ordenados por número de nodo.

Es un algoritmo recursivo.

El nodo que inicia el proceso de elección es aquel que se da cuenta que el
coordinador no responde.

El profesor nos hizo una analogía con un barco dónde el marinero con menor
grado no se vuelve coordinador (admirante) de golpe, sino que se releva
la decisión al superior. Por ejemplo, el 1 manda mensaje y se da cuenta que
el coordinador no está respondiendo, por lo que entonces envía mensaje al 2 y
al 3, de los cuales el dos responde, ok, yo me encargo, entonces el 2 envía un
mensaje solicitando ser el coordinador y, como nadie responde, entonces el dos
se convierte en el coordinador.

El mensaje de elección se manda a los que están arriba.

El mensaje de coordinador se envía a los que están abajo.

### Algoritmo de elección en anillo (token ring)

El proceso de elección inicia el nodo que se da cuenta que el coordinador no
responde.

Los nodos al recibir la lista, se busca a sí mismo, y si es el nodo más grande
de la lista, él se convierte en el coordinador.

Si un nodo se encuentra en una lista y no es el nodo mayor, ignora la lista y
deja de reenviarlo.

Si se encuentra quiere decir que ya dió la vuelta completa por el anillo.

[husos-horarios-img]: https://upload.wikimedia.org/wikipedia/commons/thumb/8/88/World_Time_Zones_Map.png/1000px-World_Time_Zones_Map.png
[imagen-pulsos-reloj]: https://learn.circuitverse.org/assets/images/clock_signal.jpg
