# Conceptos Básicos

En esta sección veremos algunos de los conceptos básicos de la materia. 

- [Conceptos Básicos](#conceptos-básicos)
  - [Sistema Centralizado](#sistema-centralizado)
    - [Ventajas](#ventajas)
    - [Desventajas](#desventajas)
    - [Ejemplos](#ejemplos)
  - [Sistema Distribuído](#sistema-distribuído)
    - [Ventajas](#ventajas-1)
  - [Glosario de términos](#glosario-de-términos)


## Sistema Centralizado

- Es aquél dónde el código y los datos residen en una computadora.
- Terminales Tontas: sólamente reciben datos y los envían sin cifrar.
- Los sistemas centralizados evolucionaron a usar una red.
- Son muy antiguos por lo general.

### Ventajas

- **Facilidad de programación**: Los sistemas centralizados son fáciles de programar ya que no existe el problema de comunicar diferentes procesos en diferentes plataformas, tampoco es un problema la consistencia de los datos debido a que todos los procesos se ejecutan en una misma computadora con una sola memoria.
- **Facilidad de instalación**: es fácil de instalar un sistema centralizado. Basta con instalar un solo site el cual va a requerir una acomedida de energía eléctrica, un sistema de enfriamente (generalmente por agua), conexión a la red de datos y comunicación por voz. Más adelante en el curso veremos cómo el cómputo en la nube está cambiando la idea de instalación física en pos de sistemas virtuales en la nube.
- **Facilidad de operación**: Es fácil operar un sistema central, ya que la administración la realiza un solo equipo de operadores, incluyyendo las tareas de respaldos, mantenimiento preventivo y correctivo, actualización de versiones entre otras.
- **Seguridad**: es fácil asegurar la seguridad física y lógica de un sistema centralizado. La seguridad física se implementa mediante sistemas *CCTV*, controles de cerraduras electrónicas, biométricos, etc. La seguridad lógica se implementa mediante un essquema de permisos a los diferentes recursos como son el sistema operativo, los archivos, las bases de datos.
- **Bajo costo**: dados los factores anteriores, instalar un sistema centralizado resulta más barato que un sistema distribuido ya que solo se pagan licencias para un servidor, sólo se instala un site, se tiene un solo equipo de operadores.

### Desventajas

- **Procesamiento limitado**: El sistema centralizado cuenta con un número limitado de procesadores por tanto a medida que incrementamos el número de procesos en ejecución, cada proceso ejecutará más lentamente. Por ejemplo, en Windows podemos ejecutar el Administrador de Tareas para ver el porcentaje de CPU que utiliza cada proceso en ejecución si la computadora ha llegado a su límite, entonces veremos que el porcentaje de uso de CPU es 100%.
- **Almacenamiento es limitado**: un sistema centralizado cuenta con un número limitado de unidades de almacenamiento (discos duros). Cuando un sistema llega al límite, el almacenamiento se detiene, ya que no es posible agregar datos a los archivos ni realizar swap.
- **Ancho de banda limitado**: un sistema centralizado puede llegar al límite en el ancho de banda de entrada y/o de salida, en estas condiciones la comunicación con los usuarios se va a alentar.
- **Número de usuarios limitado**: un sistema centralizado tiene un máximo de usuarios que se pueden conectar o que pueden consumir los servicios. Por ejemplo, por razones de licenciamiento los manejadores de bases de datos tienen un máximo de usuarios que pueden conectarse, así mismo, el sistema operativo tiene un límite en el número de descriptores de archivos que puede crear. Recordemos que cada vez que se abre un archivo y cada vez que se crea un socket se ocupa un descriptor de archivo.
- **Baja tolerancia a fallas**: En un sistema centralizado una falla suele ser catastrófica, ya que sólo se tiene una computadora y una memoria. Cualquier falla susle producir la inhabilitación del sistema completo.

### Ejemplos

- Un servicio web centralizado.
- Un DBMS centralizado.
- Una computadora _stand-alone_.

## Sistema Distribuído

> "Un sistema distribuido es una colección de computadoras independientes que dan al usuario la impresión de constituir un único sistema coherente."
> Andrew S. Tanenbaum

- Los sistemas de nómina son fácilmente paralelizables puesto que el cálculo de una nómina no depende de otros datos. No obstante, este **no es el caso para todos los sistemas**.

### Ventajas

- **Procesamiento es (casi) ilimitado**: Un sistema distribuido  puede tener un número casi ilimitado de CPUs ya que siempre será posible agregar más servicores, por tanto a medida que incrementamos el número de CPUs podemos esperar que los procesos ejecuten más rápido ebido a que los procesos ejecutarán en paralelo en diferentes CPUs. El límite del paralelismo queda definido por la ley de Amdal.
- **Almacenamiento es (casi) ilimitado**: un sistema distribuido cuenta con un número casi ilimitado de unidades de almacenamiento (discos duros). Siempre es posible conectar más servidores de almacenamiento.
- **Ancho de banda es (casi) ilimitado**: en un sistema distribuido cada computadora aporta su ancho de banda, esto es, en la medida que agregamos servidores podemos enviar y rrecibir una mayor cantidad de datos por unidad de tiempo (es decir, aumentamos el ancho de banda).

## Glosario de términos

Sistema Centralizado
: Es aquél dónde el **código** y los **datos** residen en una computadora.

Sistema Distribuido
: Ese aquél dónde los códigos, procesos y/o datos residen en múltiples computadoras.