# Sincronización y Coordinación

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

