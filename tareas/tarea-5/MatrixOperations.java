import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MatrixOperations extends Remote {
    float[][] multiplyMatrices(float[][] A, float[][] B) throws RemoteException;
}

