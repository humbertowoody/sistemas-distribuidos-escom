
/**
 * servidorB.java
 * En este archivo se implementa la definición del servidor B dada por 
 * el profesor.
 * Humberto Alejandro Ortega Alcocer (hortegaa1500@alumno.ipn.mx)
 * 14/03/2023
 */
import java.io.*;
import java.net.*;

public class servidorB {
  private static String[] resultados = new String[3];

  public static void main(String[] args) throws IOException, InterruptedException {
    // Validamos que hayamos recibido los argumentos.
    if (args.length != 4) {
      System.out.println("uso:");
      System.out.println("\tjava servidorB PUERTO PUERTO_SERVA1 PUERTO_SERVA2 PUERTO_SERVA3");
      System.out.println();
      System.out.println("argumentos:");
      System.out.println("\t- PUERTO: puerto dónde se ejecutará este servidorB.");
      System.out.println("\t- PUERTO_SERVA1: puerto dónde se está ejecutando el servidorA #1.");
      System.out.println("\t- PUERTO_SERVA2: puerto dónde se está ejecutando el servidorA #2.");
      System.out.println("\t- PUERTO_SERVA3: puerto dónde se está ejecutando el servidorA #3.");
      System.out.println();
      return;
    }

    // Obtenemos los datos.
    int puerto = Integer.parseInt(args[0]),
        puerto_serva_1 = Integer.parseInt(args[1]),
        puerto_serva_2 = Integer.parseInt(args[2]),
        puerto_serva_3 = Integer.parseInt(args[3]);

    // Creamos el socket del servidor.
    ServerSocket serverSocket = new ServerSocket(puerto);

    // Ciclo infinito principal.
    while (true) {
      // Aceptamos una conexión TCP.
      Socket clientSocket = serverSocket.accept();

      // Creamos los flujos de lectura de datos.
      DataInputStream in = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

      // Leemos el número
      long numero = in.readLong();

      // Calculamos k.
      int k = ((int) numero) / 3;

      // Creamos los intervalos para el número.
      Thread hilo_servidorA_1 = new HiloServidorA(puerto_serva_1, 0, numero, 2, k),
          hilo_servidorA_2 = new HiloServidorA(puerto_serva_2, 1, numero, k + 1, 2 * k),
          hilo_servidorA_3 = new HiloServidorA(puerto_serva_3, 2, numero, (2 * k) + 1, ((int) numero) - 1);

      // Iniciamos los hilos.
      hilo_servidorA_1.start();
      hilo_servidorA_2.start();
      hilo_servidorA_3.start();

      // Esperamos a que finalicen, almacenamos su resultado.
      hilo_servidorA_1.join();
      hilo_servidorA_2.join();
      hilo_servidorA_3.join();

      // Mostramos al mismo tiempo.
      System.out.println("- Número: " + numero);
      System.out.println("\t- ServidorA 1: 2 - " + k + " , resultado: " + resultados[0]);
      System.out.println("\t- ServidorA 2: " + (k + 1) + " - " + (2 * k) + " , resultado: " + resultados[1]);
      System.out.println("\t- ServidorA 3: " + ((2 * k) + 1) + " - " + numero + " , resultado: " + resultados[2]);
      System.out.println("- Resultado: " + ((resultados[0].equals("NO DIVIDE") && resultados[1].equals("NO DIVIDE")
          && resultados[2].equals("NO DIVIDE")) ? "ES PRIMO" : "NO ES PRIMO"));

      // Validamos los datos.
      if (resultados[0].equals("NO DIVIDE") && resultados[1].equals("NO DIVIDE")
          && resultados[2].equals("NO DIVIDE")) {
        // Ningún rango es divisible, así que regresamos que no es divisor.
        out.writeUTF("ES PRIMO");
      } else {
        // Algún rango es divisible, así que es divisor.
        out.writeUTF("NO ES PRIMO");
      }

      // Cerramos el socket.
      clientSocket.close();
    }
  }

  private static class HiloServidorA extends Thread {
    private final int puerto, // El puerto del servidorA.
        lim_inferior, // El límite inferior de búsqueda.
        lim_superior, // El límite superior de búsqueda.
        indice_resultados; // El índice dónde almacenar los resultados.
    private final long numero; // El número en cuestión.

    public HiloServidorA(int puerto, int indice_resultados, long numero, int lim_inferior, int lim_superior) {
      this.puerto = puerto;
      this.numero = numero;
      this.lim_inferior = lim_inferior;
      this.lim_superior = lim_superior;
      this.indice_resultados = indice_resultados;
    }

    @Override
    public void run() {
      try (
          Socket servidorASocket = new Socket("localhost", puerto);
          DataInputStream in = new DataInputStream(servidorASocket.getInputStream());
          DataOutputStream out = new DataOutputStream(servidorASocket.getOutputStream())) {

        // Enviamos los datos para realizar la operación en formato CSV.
        out.writeUTF(numero + "," + lim_inferior + "," + lim_superior);

        // Esperamos los resultados.
        resultados[indice_resultados] = in.readUTF();

        // Cerramos la conexión.
        servidorASocket.close();
      } catch (IOException e) {
        System.err.println("Error conectando con el servidor A: " + e);
      }
    }
  }
}
