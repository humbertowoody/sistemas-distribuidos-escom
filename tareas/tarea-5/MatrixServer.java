import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MatrixServer {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java MatrixServer <node_id>");
            System.exit(1);
        }

        int nodeId = Integer.parseInt(args[0]);

        try {
            // Crea e instancia el objeto remoto
            MatrixOperationsImpl obj = new MatrixOperationsImpl();
            MatrixOperations stub = (MatrixOperations) UnicastRemoteObject.exportObject(obj, 0);

            // Registra el objeto remoto en el rmiregistry
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("node" + nodeId, stub);

            System.out.println("Servidor RMI (nodo " + nodeId + ") listo...");

        } catch (Exception e) {
            System.err.println("Error en el servidor (nodo " + nodeId + "):");
            e.printStackTrace();
        }
    }
}

