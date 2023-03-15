
/**
 * cliente.java
 * En este archivo se implementa la definición del cliente dada por 
 * el profesor.
 * Humberto Alejandro Ortega Alcocer (hortegaa1500@alumno.ipn.mx)
 * 14/03/2023
 */
import java.io.*;
import java.net.*;

public class cliente {

  public static void main(String[] args) {
    // Validamos que hayamos recibido los argumentos.
    if (args.length != 2) {
      System.out.println("uso:");
      System.out.println("\tjava cliente PUERTO NUM");
      System.out.println();
      System.out.println("argumentos:");
      System.out.println("\t- PUERTO: puerto dónde se está ejecutando el servidorB.");
      System.out.println("\t- NUM: el número (entero de 64 bits) a determinar si es primo.");
      System.out.println();
      return;
    }

    // Obtenemos los datos.
    int puerto = Integer.parseInt(args[0]);
    long numero = Long.parseLong(args[1]);

    try (
        // Abrimos un socket al puerto especificado por el usuario y creamos los objetos
        // para E/S.
        Socket clientSocket = new Socket("localhost", puerto);
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream())) {
      // Enviamos el número.
      out.writeLong(numero);

      // Recibimos la cadena
      String resultado = in.readUTF();

      // Evaluamos el resultado.
      if (resultado.equals("ES PRIMO")) {
        System.out.println("✅ " + resultado);
      } else {
        System.out.println("⛔️ " + resultado);
      }
    } catch (IOException e) {
      System.err.println("Error en la comunicación con el servidor: " + e);
    }
  }
}