import javax.net.ssl.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientePUT {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Uso: java ClientePUT <IP del servidor> <puerto del servidor> <nombre del archivo>");
            return;
        }

        System.setProperty("javax.net.ssl.trustStore","../../keystores_java/keystore_cliente.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","1234567");
        String serverIp = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String fileName = args[2];

        try {
            // Leer el archivo del disco local
            byte[] fileContent = Files.readAllBytes(Paths.get(fileName));

            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) socketFactory.createSocket(serverIp, serverPort);

            try (
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            ) {
                // Enviar la petición PUT
                writer.write("PUT " + fileName + " " + fileContent.length + "\r\n");
                writer.flush();

                // Enviar el contenido del archivo
                out.write(fileContent);
                out.flush();

                // Leer la respuesta del servidor
                String response = reader.readLine();

                if ("OK".equals(response)) {
                    System.out.println("El archivo fue recibido por el servidor con éxito");
                } else {
                    System.err.println("El servidor no pudo escribir el archivo en el disco local");
                }
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
