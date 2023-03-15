
/**
 * servidorA.java
 * En este archivo se implementa la definición del servidor A dada por 
 * el profesor.
 * Humberto Alejandro Ortega Alcocer (hortegaa1500@alumno.ipn.mx)
 * 14/03/2023
 */
import java.io.*;
import java.net.*;

public class servidorA {

  public static void main(String[] args) throws IOException {
    // Validamos que hayamos recibido los argumentos.
    if (args.length != 1) {
      System.out.println("uso:");
      System.out.println("\tjava servidorA PUERTO");
      System.out.println();
      System.out.println("argumentos:");
      System.out.println("\t- PUERTO: puerto dónde se ejecutará este servidorA.");
      System.out.println();
      return;
    }

    // Obtenemos los datos.
    int puerto = Integer.parseInt(args[0]);

    // Creamos el socket del servidor.
    ServerSocket serverSocket = new ServerSocket(puerto);

    // Ciclo principal de servidor.
    while (true) {
      // Aceptamos la conexión.
      Socket clientSocket = serverSocket.accept();

      // Creamos e inicializamos el hilo manejador del cliente.
      Thread clientThread = new ClientThread(clientSocket);
      clientThread.start();
    }
  }

  private static class ClientThread extends Thread {
    private final Socket clientSocket;

    public ClientThread(Socket clientSocket) {
      this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
      try (
          DataInputStream in = new DataInputStream(clientSocket.getInputStream());
          DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
        // Recibimos los datos en formato CSV.
        String datos = in.readUTF();

        // Separamos los datos en función del ,
        String tokens[] = datos.split(",");

        // Obtenemos los valores.
        long numero = Long.parseLong(tokens[0]),
            lim_inferior = Long.parseLong(tokens[1]),
            lim_superior = Long.parseLong(tokens[2]);

        // Bandera para saber si fue, o no, divisible.
        Boolean divisible = false;

        // Iteramos entre los límites.
        for (long iterador = lim_inferior; iterador <= lim_superior; iterador++) {
          // Verificamos si es divisible.
          if (numero % iterador == 0) {
            // Marcamos la bandera como verdadera.
            divisible = true;

            // Salimos del ciclo.
            break;
          }
        }

        // Imprimimos los resultados.
        System.out.println("- Número: " + numero + " (" + lim_inferior + "-" + lim_superior + "), resultado: "
            + (divisible ? "DIVIDE" : "NO DIVIDE"));

        // Regresamos el resultado correspondiente.
        out.writeUTF(divisible ? "DIVIDE" : "NO DIVIDE");

        // Cerramos el socket.
        clientSocket.close();
      } catch (IOException e) {
        System.err.println("Error procesando la llamada: " + e);
      }
    }
  }
}
