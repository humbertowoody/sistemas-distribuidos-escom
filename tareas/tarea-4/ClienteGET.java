import javax.net.ssl.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClienteGET {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Uso: java ClienteGET <IP del servidor> <puerto del servidor> <nombre del archivo>");
            return;
        }
        System.setProperty("javax.net.ssl.trustStore","../../keystores_java/keystore_cliente.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","1234567");
        String serverIp = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String fileName = args[2];

        try {
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) socketFactory.createSocket(serverIp, serverPort);

            try (
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            ) {
                // Enviar la petición GET
                writer.write("GET " + fileName + "\r\n");
                writer.flush();

                // Leer la respuesta del servidor
                String response = reader.readLine();
                String[] responseParts = response.split(" ", 2);

                if ("OK".equals(responseParts[0])) {
                    // Leer el archivo del servidor
                    int fileLength = Integer.parseInt(responseParts[1]);
                    byte[] fileContent = new byte[fileLength];
                    int bytesRead = in.read(fileContent);

                    if (bytesRead != fileLength) {
                        throw new IOException("No se pudo leer la longitud esperada del archivo");
                    }

                    // Escribir el archivo en el disco local
                    Files.write(Paths.get(fileName), fileContent);
                    System.out.println("El archivo se recibió con éxito");
                } else {
                    System.err.println("El servidor no pudo enviar el archivo");
                }
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

