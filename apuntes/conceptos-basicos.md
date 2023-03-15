# Conceptos Básicos

En esta sección veremos algunos de los conceptos básicos de la materia. 

- [Conceptos Básicos](#conceptos-básicos)
  - [Sistema Centralizado](#sistema-centralizado)
    - [Ventajas](#ventajas)
    - [Desventajas](#desventajas)
    - [Ejemplos](#ejemplos)
  - [Sistema Distribuído](#sistema-distribuído)
    - [Ventajas](#ventajas-1)
  - [Objetivos de los sistemas distribuídos](#objetivos-de-los-sistemas-distribuídos)
    - [1. Facilidad en el acceso a los recursos](#1-facilidad-en-el-acceso-a-los-recursos)
    - [2. Transparencia](#2-transparencia)
      - [2.1. Transparencia en el acceso a los datos](#21-transparencia-en-el-acceso-a-los-datos)
      - [2.2. Transparencia en la ubicación](#22-transparencia-en-la-ubicación)
      - [2.3. Transparencia de migración](#23-transparencia-de-migración)
      - [2.4. Transparencia de reubicación](#24-transparencia-de-reubicación)
      - [2.5. Transparencia de replicación](#25-transparencia-de-replicación)
      - [2.6. Transparencia de concurrencia](#26-transparencia-de-concurrencia)
      - [2.7. Transparencia ante las fallas](#27-transparencia-ante-las-fallas)
    - [3. Apertura](#3-apertura)
      - [Características de los sistemas abiertos](#características-de-los-sistemas-abiertos)
    - [4. Escalabilidad](#4-escalabilidad)
      - [4.1. Escalar en tamaño](#41-escalar-en-tamaño)
      - [4.2. Escalar geográficamente](#42-escalar-geográficamente)
      - [4.3. Escalar la administración](#43-escalar-la-administración)
  - [Técnicas de escalamiento](#técnicas-de-escalamiento)
    - [1. Ocultar la latencia en las comunicaciones](#1-ocultar-la-latencia-en-las-comunicaciones)
    - [2. Distribución](#2-distribución)
    - [3. Replicación](#3-replicación)
    - [4. Elasticidad](#4-elasticidad)
  - [Requisitos de diseño de los sistemas distribuidos](#requisitos-de-diseño-de-los-sistemas-distribuidos)
    - [Calidad del Servicio (QoS)](#calidad-del-servicio-qos)
    - [Balance de carga](#balance-de-carga)
    - [Tolerancia a fallas](#tolerancia-a-fallas)
    - [Seguridad](#seguridad)
  - [Tipos de sistemas distribuidos](#tipos-de-sistemas-distribuidos)
    - [Cluster](#cluster)
    - [Malla](#malla)
      - [Arquitectura de Malla](#arquitectura-de-malla)
        - [Capa de Fabricación](#capa-de-fabricación)
        - [Capa de Recursos](#capa-de-recursos)
        - [Capa colectiva](#capa-colectiva)
        - [Capa de aplicaciones](#capa-de-aplicaciones)
    - [Middleware](#middleware)
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


## Objetivos de los sistemas distribuídos

Como vmios la clase anterior, los sistemas distribuidos tienen grandes ventajas sobre los sistemas centralizados.

Sin embargo, los sistemas distribuidos también tienen algunas desventajas que podemos resumir en su alta complejidad y costo.

En general un sistema distribuído deberá cumplir los siguientes objetivos:

1. [Facilidad en el acceso de los recursos](#1-facilidad-en-el-acceso-a-los-recursos).
2. [Transparencia](#2-transparencia).
3. Apertura
4. Escalabilidad

### 1. Facilidad en el acceso a los recursos

Es de la mayor importancia en un sistema distribuido facilitar a los usuarios y a las aplicaciones el acceso a los recursos remotos.

Entendemos como recurso el CPU, la memoria RAM, las unidades de almacenamiento, las impresoras, los DBMS, los archivos o cualquier otra entidad lógica o física que preste un servicio en el sistema distribuído.

En un sistema distribuido se comparten los recursos por razones técnicas y por razones económicas.

En el primer caso se comparten recursos por **razones técnicas** cuando tenemos procesoso que ejecutan en forma distribuida utilizando datos que se encuentran distribuidos geográficamente o bien, procesos que requieren la distribución del cálculo en diferentes CPU etc.

En el segundo caso se comparten recursos por **razones económicas** debido a su alto costo.

Por ejemplo, la *virtualización* nos permite compartir los recursos de una computadora como son el CPU, la memoria, y las unidades de almacenamiento creando entornos de ejecución llamados máquinas virtuales.

La virtualización aumenta el porcentaje de utilización de los recursos de la computadora y con ellos se obtiene un mayor beneficio dado el costo de los recursos.

Sin embargo, compartir recursos conlleva un compromiso en la seguridad ya que será necesario implementar mecanismos de comunicación segura (SSL, TLS, HTTPS), esquemas para la confirmación de la identidad (autenticación) y esquemas de permisos para el acceso a los recursos (autorización).

### 2. Transparencia

La transparencia es la capacidad de un sistema distribuido de presentarse ante los usuarios y aplicaciones como una sola computadora.

Tipos de transparencia:

1. [Transparencia en el acceso a los datos](#21-transparencia-en-el-acceso-a-los-datos).
2. [Transparencia de ubicación](#22-transparencia-en-la-ubicación).
3. [Transparencia de migración](#23-transparencia-de-migración).
4. [Transparencia de reubicación](#24-transparencia-de-reubicación).
5. [Transparencia de replicación](#25-transparencia-de-replicación).
6. [Transparencia de concurrencia](#26-transparencia-de-concurrencia).
7. [Transparencia de tolerancia a fallas](#27-transparencia-ante-las-fallas).

#### 2.1. Transparencia en el acceso a los datos

Un sistema distribuido deberá proveer de una capa que permita a los usuarios y aplicaciones acceder a los datos de manera estandarizada.

Por ejemplo, un servidor web que permite acceder a los archivos que residen en computadoras con diferentes sistemas operativos mediante nombres estandarizados independientemente del tipo de nomenclatura (la forma de nombrar los archivos) implementada en cada sistema operativo.

#### 2.2. Transparencia en la ubicación

En un sistema distribuido de usuarios acceden a los recursos independientemente de su localización física.

Por ejemplo, una URL identifica un recurso en la Web de manera única independientemente de su localización física.

En el caso de la URL: `https://m4gm.com/moodle/curso.txt` el archivo `curso.txt` está localizado en el directorio `/moodle` de la computadora cuyo dominio es `m4gm.com`.

#### 2.3. Transparencia de migración

En algunos sistemas distribuidos es posible migrar recursos de un sitio a otro.

Si la migración del recurso no afecta la forma en que se acceda al recurso, se dice que el sistema soporta la transparencia de migración.

Por ejemplo, un sistema que implementa la migración de datos de una computadora a otra de manera transparente, como es el caso de una caché distribuida.


#### 2.4. Transparencia de reubicación

La transparencia de reubicación se refiere a la capacidad del sistema distribuido de cambiar la ubicación de un recurso mientras está en uso, sin que el usuario que accede al recurso se vea afectado.

En UNIX para cambiar la ubicación de un proceso en ejecución  primero se le envía al proceso un signal `SIGSTOP` en la ubicación de origen, el proceso se migra a la ubicación de destino, finalmente se envía al proceso un `SIGCONT` en la ubicación de destino, entonces el proceso sigue ejecutando desde el punto en que se quedó.

#### 2.5. Transparencia de replicación

Es la capacidad del sistema distribuido de ocultar la existencia de recursos replicados.

Por ejemplo, la aplicación de los datos como una estrategia que permite aumentar la confiabilidad y el rendimiento en los sistemas distribuídos.

#### 2.6. Transparencia de concurrencia

En las computadoras modernas todos los recursos son compartidos.

La transparencia de concurrencia se refiere a la capacidad de un sistema de ocultar el hecho de que varios usuarios y procesos comparten los diferentes recursos de manera concurrente.

Por ejemplo, un sistema operativo multitarea oculta el hecho de que varios procesos utilizan de manera concurrente el CPU, la memoria, los discos duros, etc.

Por otra parte, un sistema operativo multiusuario oculta el hecho de que la computadora es utilizada por múltiples usuarios de manera concurrente.

#### 2.7. Transparencia ante las fallas

Es la capacidad del sistema distribuido de ocultar una falla.

Como vimos anteriormente, la distribución del procesamiento permite implementar sistemas tolerantes a fallas.

Por ejemplo, si un sistema que se encuentra totalmente replicado, cuando el sistema principal falla entonces el usuario accederá de manera transparente a la réplica del sistema.

Más adelante en el curso veremos cómo replicar un sistema completo en la nube utilizando un administrador de tráfico de red y cómo implementar sistemas tolerantes a fallas en la nube mediante balance de carga.

### 3. Apertura

Un sistema abierto es aquel que ofrece servicios a través de reglas de sintaxis y semántica estándares.

Las reglas de sintaxis generalmente se definen mediante un lenguaje de definición de interfaz en el cual se especifica los nombres de las operaciones del servicio, nombre y tipo de los parámetros, valores de retorno, posibles excepciones, entre otros elementos, que sean de utilidad para automatizar la comunicación entre el cliente del servicio y el servicio.

Las **reglas de semántica** (funcionalidad) de las operaciones de un servicio generalmente se define de manera informal utilizando lenguaje natural.

#### Características de los sistemas abiertos

Los sistemas abiertos exhiben tres características que los hacen más populares que los sistemas propietarios, estas características son: interoperabilidad, portabilidad y extensibilidad.

La definición de las reglas de sintaxis estándares permite que diferentes sistemas puedan interaccionar.

A la capacidad de sistemas diferentes de trabajar de manera interactiva se le llama interoperabilidad.

Por ejemplo, un servicio web escrito en Java, Python o en C# puede ser utilizado indistintamente por un cliente escrito en JavaScript, Java, Python, o C#.

La portabilidad (cross-platform) de un programa se refiere a la posibilidad de ejecutar el programa en diferentes plataformas sin la necesidad de hacer cambios al programa.

Por ejemplo un programa escrito en Java puede ser ejecutado sin cambios en cualquier plataforma que tenga instalado el JRE (Java Runtime Environment).

En 1995 Sun Microsystems explicó la portabilidad de los programas escritos en Java con la siguiente frase: "Write once, run everywhere".

La extensibilidad se refiere a la capacidad de los sistemas de poder crecer mediante la incorporación de componentes fáciles de reemplazar y adaptar, como sería el caso de sistemas basados en OOP (programación orientada a objetos) donde es posible extender la funcionalidad de una clase mediante la herencia.

### 4. Escalabilidad

La escalabilidad es la capacidad de un sistema de crecer sin reducir su calidad de servicio.

Un sistema puede escalar en tres aspectos principales: tamaño, geografía y administración.

#### 4.1. Escalar en tamaño

Cuando un sistema requiere atender más usuarios o ejecutar procesos más demandantes, es necesario agregar más CPUs, más memoria, más unidades de almacenamiento e incrementar el ancho de banda de la red.

Es decir, el sistema requiere escalar en tamaño (escalamiento vertical).

En relativamente sencillo escalar el tamaño de un sistema distribuido agregando más servidores, en cambio un sistema centralizado solo puede crecer hasta alcanzar el número máximo de CPUs que soporta la computadora, la cantidad máxima de memoria RAM, el número máximo de controladores de disco duro, el número máximo de controladores de red, etc.

#### 4.2. Escalar geográficamente

En la actualidad las empresas globales requieren operar sus sistemas en múltiples regiones geográficas.

Si la empresa cuenta solamente con un sistema central, los usuarios tendrán que conectarse desde ubicaciones remotas por lo que se incrementará los tiempos de respuesta debido a la latencia de la red.

Entonces surge la necesidad de escalar geográficamente los sistemas (escalamiento horizontal), por tanto será necesario instalar servidores en diferentes ubicaciones estratégicamente localizadas con el fin de reducir los tiempos de respuesta.

Por ejemplo, una empresa global puede instalar un centro de datos en cada región geográfica (América del Norte, América del Sur, Europa, Asia, África).

Si la región es de alta demanda (como es el caso de América del Norte y Europa) la empresa puede instalar más centros de datos en la misma región.

#### 4.3. Escalar la administración

Cuando un sistema crece en tamaño y geografía, también aumenta la complejidad en la administración del sistema.

Un sistema más grande implica más computadoras, más CPUs, más tarjetas de memoria RAM, más unidades de almacenamiento, más concentradores de red, es decir, más componentes que pueden fallar, más información que se tiene que respaldar, más usuarios, más permisos que controlar, etc.

En resumen, para crecer el sistema se requiere escalar también la administración.


## Técnicas de escalamiento

Ahora veremos algunas técnicas para escalar los sistemas:

1. Ocultar la latencia en las comunicaciones.
2. Distribución.
3. Replicación.
4. Elasticidad.

### 1. Ocultar la latencia en las comunicaciones

La latencia en las comunicaciones es el tiempo que tarda un mensaje en ir del origen al destino. 

Existen múltiples factores que influyen en la latencia de las comunicaciones, como son el tamaño de los mensajes, la forma en que se enrutan los mensajes, la distancia, la hora del día, la época del año, etc.

La latencia en las comunicaciones aumenta el tiempo de espera cuando se hace una petición a un servidor remoto.

Una estrategia que se utiliza para ocultar la latencia en las comunicaciones, es el uso de peticiones asíncronas.

Supongamos que una aplicación realiza una petición a un servidor cuándo el usuario presiona un botón, si la petición es síncrona el usuario debe esperar a que el servidor envíe la respuesta, ya que la aplicación no puede ejecutar otra tarea mientras espera.

Por otra parte, si la petición es asíncrona, la aplicación puede ejecutar otras tareas.

Por ejemplo, en Android todas las peticiones que se realizan a los servidores deben ser asíncronas (utilizando tareas o threads), lo cual garantiza que las aplicaciones seguirán respondiendo al usuario mientras esperan la respuesta del servidor.

### 2. Distribución

Una técnica muy utilizada para escalar un sistema es la distribución.

Para distribuir un sistema se divide en partes más pequeñas las cuales se ejecutan en diferentes servidores.

Por ejemplo, supongamos que una empresa tiene una plataforma de comercio electrónico en la Web, cuando la empresa comienza a tener operaciones globales surge la necesidad de escalar la plataforma de comercio electrónico, para ello se puede distribuir el sistema en distintos servidores.

### 3. Replicación


Otra técnica utilizada para escalar un sistema es la replicación de los procesos y de los datos.

Replicar los procesos en diferentes computadoras permite liberar de trabajo las computadoras más saturadas, es decir, balancear la carga en el sistema.

Replicar los datos en diferentes computadoras permite acceder a los datos más rápidamente, debido a que con ello se evitan los cuellos de botella en los servidores.

Para replicar los datos se puede utilizar caches que aprovechen la localidad espacial y temporal de los datos.

Por ejemplo, si un archivo se utiliza con frecuencia (exhibe localidad temporal), es conveniente tener una copia en una cache local.

En el caso de que el archivo sea modificado en el servidor, entonces el servidor enviará un mensaje de invalidación de cache, lo que significa que el archivo deberá ser eliminado de la cache local.

Si posteriormente el cliente requiere el archivo, deberá solicitarlo al servidor y con ello contará con el archivo actualizado.

### 4. Elasticidad

Posiblemente la técnica más interesante para escalar un sistema es la elasticidad en la nube.

La elasticidad es la adaptación a los cambios en la carga mediante aprovisionamiento y desaprovisionamiento de recursos en forma automática.

El aprovisionamiento es la creación del recurso (por ejemplo una máquina virtual), y el desaprovisionamiento es la eliminación del recurso.

Supongamos un servicio de streaming bajo demanda, como es el caso de Netflix.

En este tipo de servicio la demanda crece los fines de semana y decrece los días entre semana.

Si el proveedor de servicio no aprovisiona los recursos suficientes para atender la demanda del fin de semana, entonces muchos usuarios se quedarán sin servicio.

Por otra parte, si el proveedor del servicio aprovisiona los recursos necesarios para atender a sus usuarios el fin de semana, estos recursos estarán subutilizados los días entre semana, lo cual resulta en pérdidas económicas.

Entonces la solución es utilizar la posibilidad que les ofrece la nube para crecer y decrecer los recursos aprovisionados en forma dinámica.

Más adelante en el curso veremos cómo utilizar la elasticidad en la nube.

## Requisitos de diseño de los sistemas distribuidos

El diseño de un sistema consiste en la definición de la arquitectura del sistema, la espe ificación detallada de sus componentes y la especificación del entorno tecnológica que soportará el sistema.

### Calidad del Servicio (QoS)

Los requisitos de calidad de servicio son aquellos que describen las características de calidad que los servidores deben cumplir, como son:

- Tiempos de respuesta
- Tasa de errores permitida
- Disponibilidad del servicio (SLA)
- Volumen de peticiones
- Seguridad

### Balance de carga

Los sistemas distribuidos distribuyen procesamiento y datos.

Para que un sistema distribuido sea eficiente es necesario balancear la carga del procesamiento y del acceso a los datos, con la finalidad de evitar que uno o más computadoras se conviertan en un cuello de botella que ralentice el sistema completo.

Por tanto, es importante definir los requisitos de balance de carga del sistema, esto es definir los criterios que se utilizarán para balancear la carga de procesamiento y de acceso a los datos.

Más adelante en el curso veremos cómo veremos cómo implementar el balance de carga en la nube.

### Tolerancia a fallas

El sistema si falla se recupera automáticamente.

### Seguridad

Uno de los requisitos de diseño más importantes es la seguridad, debido a que las amenazas a las que se expone un sistema que se conecta a internet.

Además de las vulnerabilidades del sistema operativo y del hardware, los sistemas introducen vulnerabilidades propias.

Por tanto, es muy importante definir los requisitos de seguridad para el sistema, entre otros:

- Seguridad física del sistema.
- Comunicación segura (confidencialidad, integridad, autenticación).
- Utilización de usuarios no administrativos.
- Configuración detallada de los permisos.
- Programar para la prevención de ataques (p.e. SQL Injection).
- Seguridad en el proceso de desarrollo.

## Tipos de sistemas distribuidos

Los sistemas distribuidos distribuyen  el procesamiento (cómputo) y sistemas que distribuyen los datos.

Los sistemas que distribuyen el cómputo pueden a la vez.

### Cluster

Un cluster es un conjunto de computadoras homogéneas con el mismo sistema operativo conectadas mediante una red local (LAN) de alta velocidad.

Los clusters se utilizan para el cómputo de alto rendimiento, dónde los programas se distribuyen entre los diferentes nodos del cluster.

### Malla

Una malla es un conjunto de computadoras generalmente heterogéneas (hardware, sistema operativo, redes, etc) agrupadas en organizaciones virtuales.

Una organización virtual es un conjunto de recursos (servidores, clusters, bases de datos, etc), y los usuarios que los utilizan.

En el Top500 (2021), 474 sistemas son clusters, mientras que 40 sistemas son MPP (Massiveley Parallel Processing).

#### Arquitectura de Malla

Se tienen cuatro niveles:

- Aplicaciones
- Capa colectiva
- Capa de Conectividad y Capa de Recursos
- Capa de Fabricación

##### Capa de Fabricación

La capa de fabricación está constituida por interfaces para los recursos locales de una ubicación.

En esta capa se implementan funciones que permiten el intercamio de recursos dentro de la organización virtual tales como consulta del estado ddl recurso y demás.

##### Capa de Recursos 

La capa de recursos permite administrar recursos individuales incluyendo el control de acceso a los recursos (autorización).

##### Capa colectiva

La capa colectiva permite el acceso a múltiples recursos, incluyendo el descubrimiento de recursos, ubicación de recursos, planificación de tareas en los recursos y protocolos especializados.

##### Capa de aplicaciones

La capa de aplicaciones está compuesta por las aplicaciones que ejecutan dentro de la organización virtual.

### Middleware



## Glosario de términos

Sistema Centralizado
: Es aquél dónde el **código** y los **datos** residen en una computadora.

Sistema Distribuido
: Ese aquél dónde los códigos, procesos y/o datos residen en múltiples computadoras.

Autenticación
: Es la identificación del usuario.

Autorización
: Son los permisos con los que cuenta el usuario dentro del sistema.