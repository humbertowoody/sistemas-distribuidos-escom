# Jerarquía de Memoria

Contenidos:

- [Jerarquía de Memoria](#jerarquía-de-memoria)
  - [Capas de Memoria](#capas-de-memoria)
  - [Lectura de una variable](#lectura-de-una-variable)
  - [Localidad Espacial](#localidad-espacial)
  - [Localidad Temporal](#localidad-temporal)
  - [Glosario de Términos](#glosario-de-términos)

## Capas de Memoria

Capas dónde se almacena la información en nuestros sistemas:

1. Registros: son pequeños, el acceso a esos datos es sumamente rápido.
2. Caché: es un
3. RAM: la memoria es un almacenamiento más grande que la Caché pero suele ser más lento, es dinámica por lo que si no tiene corriente eléctrica no guardará datos. Cuando suspendemos la computadora la RAM se sigue alimentando, pero cuando hibernamos, se guarda toda la información en un diso duro y se des-energizan todos los componentes.
4. Disco Duro: área de almacenamiento con mayor capacidad, mucho más lento que la RAM. Entre la RAM y el Disco Duro hay una interacción importante que el SWAP.
5. Respaldo

- Si recorremos de abajo hacia arriba:
  - Menor capacidad
  - Mayor velocidad
  - Mayor costo
- Si recorremos de arriba hacia abajo:
  - Mayor capacidad
  - Menor velocidad
  - Menor costo

## Lectura de una variable

Para leer una variable el proceso es el siguiente:

1. El CPU requiere leer la variable, pero la variable no está en caché.
2. Se copia el bloque de memoria de la RAM a la caché. Notar que hay dos copias de la variable, una en la RAM y otra en la caché.
3. El CPU lee la variable de la caché.

> La capacidad de la caché es el número de líneas por su longitud.

> Cuando la caché se llena, se utiliza la _política de reemplazo de línea de caché_ con la cual se busca la línea menos usada y se sustituye.

## Localidad Espacial

Los datos presentan **localidad espacial** si al accedder a un dato existe una elevada probabilidad de que datos cercanos sean accedidos poco tiempo después. En el segundo caso, los expedientes solicitados por el jefe presentan localidad espacial, ya que se encuentran en la misma caja (la misma línea de caché).

## Localidad Temporal

Un dato presenta **localidad temporal** si despue´s de acceder al dato existe una elevada probabilidad de que el mismo dato sea accedido poco tiempo después.

## Glosario de Términos

Política de Reemplazo de Caché
: Normalmente se reemplaza la línea con menor uso.
: Se refiere a la política con la cual se define qué pedazo de la caché deba reemplazarse.

Localidad Espacial
: Es cuando los datos se encuentran cercanos a los siguientes a ser accedidos.

Localidad Temporal
: Es cuando los datos serán accedidos prontamente después.