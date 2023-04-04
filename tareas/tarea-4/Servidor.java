import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.*;

public class Servidor {
    private static final int SERVER_PORT = 8443;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.keyStore","../../keystores_java/keystore_servidor.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","1234567");
        try {
            SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(SERVER_PORT);
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            System.out.println("Servidor iniciado en el puerto " + SERVER_PORT);

            while (true) {
                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final SSLSocket clientSocket;

        public ClientHandler(SSLSocket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());
            try (
                InputStream in = clientSocket.getInputStream();
                OutputStream out = clientSocket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            ) {
                String request = reader.readLine();
                String[] parts = request.split(" ", 3);

                switch (parts[0]) {
                    case "GET":
                        handleGetRequest(parts[1], writer);
                        break;
                    case "PUT":
                        handlePutRequest(parts[1], Integer.parseInt(parts[2]), reader, writer);
                        break;
                    default:
                        writer.write("ERROR\r\n");
                        writer.flush();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Cliente desconectado");
        }

        private void handleGetRequest(String fileName, BufferedWriter writer) throws IOException {
            try {
                byte[] fileContent = Files.readAllBytes(Paths.get(fileName));
                writer.write("OK " + fileContent.length + "\r\n");
                writer.flush();
                clientSocket.getOutputStream().write(fileContent);
                clientSocket.getOutputStream().flush();
                System.out.println("Archivo " + fileName + " enviado");
            } catch (IOException e) {
                writer.write("ERROR\r\n");
                writer.flush();
            }
        }

        private void handlePutRequest(String fileName, int fileLength, BufferedReader reader, BufferedWriter writer) throws IOException {
            try {
                byte[] fileContent = new byte[fileLength];
                int bytesRead = clientSocket.getInputStream().read(fileContent);
                if (bytesRead != fileLength) {
                    throw new IOException("No se pudo leer la longitud esperada del archivo");
                }
                Files.write(Paths.get(fileName), fileContent);
                writer.write("OK\r\n");
                writer.flush();
                System.out.println("Archivo " + fileName + " recibido");
            } catch (IOException e) {
                writer.write("ERROR\r\n");
                writer.flush();
            }
        }
    }
}
