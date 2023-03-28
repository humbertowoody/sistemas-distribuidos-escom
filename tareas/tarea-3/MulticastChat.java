import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class MulticastChat {
  private static final String MULTICAST_IP = "239.0.0.0";
  private static final int PORT = 50000;
  private static final int MESSAGE_LENGTH = 256;

  private static String username;

  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Debe especificar el nombre de usuario como argumento.");
      return;
    }

    username = args[0];

    Thread receiveThread = new Thread(() -> {
      try {
        MulticastSocket socket = new MulticastSocket(PORT);
        InetAddress group = InetAddress.getByName(MULTICAST_IP);
        socket.joinGroup(group);
        String message;
        do {
          byte[] buffer = new byte[MESSAGE_LENGTH];
          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          socket.receive(packet);
          message = new String(packet.getData()).trim();
          String senderUsername = message.substring(0, message.indexOf("--->"));
          if (!senderUsername.equals(username)) {
            String chat = message.substring(message.indexOf("--->") + 4, message.length());
            if (chat.equalsIgnoreCase("salir")) {
              System.out.println("\n" + senderUsername + " ha salido del grupo.");
              System.out.print("Escribe tu mensaje: ");
            } else {
              System.out.println(
                  "\n(" + senderUsername + "): " + chat);
              System.out.print("Escribe tu mensaje: ");
            }
          }
        } while (!message.equalsIgnoreCase(username + "--->salir"));

        System.out.println("Saliendo del grupo...");

        socket.leaveGroup(group);
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    receiveThread.start();

    try {
      MulticastSocket socket = new MulticastSocket();
      InetAddress group = InetAddress.getByName(MULTICAST_IP);
      Scanner scanner = new Scanner(System.in);
      String message;

      do {
        System.out.print("Escribe tu mensaje: ");
        String input = scanner.nextLine();
        message = username + "--->" + input;
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
      } while (!message.equalsIgnoreCase(username + "--->salir"));

      scanner.close();

      socket.close();

      receiveThread.join();

      System.out.println("Saliendo del chat...");
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
