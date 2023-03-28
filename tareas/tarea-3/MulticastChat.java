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

        while (true) {
          byte[] buffer = new byte[MESSAGE_LENGTH];
          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          socket.receive(packet);
          String message = new String(packet.getData()).trim();
          String senderUsername = message.substring(0, message.indexOf("--->"));
          if (!senderUsername.equals(username)) {
            System.out.println(message);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    receiveThread.start();

    try {
      MulticastSocket socket = new MulticastSocket();
      InetAddress group = InetAddress.getByName(MULTICAST_IP);
      Scanner scanner = new Scanner(System.in);

      while (true) {
        System.out.print("Escribe tu mensaje: ");
        String input = scanner.nextLine();
        String message = username + "--->" + input;
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
