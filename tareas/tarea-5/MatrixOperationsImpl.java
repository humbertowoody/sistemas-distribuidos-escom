import java.rmi.RemoteException;

public class MatrixOperationsImpl implements MatrixOperations {

    public MatrixOperationsImpl() throws RemoteException {
        super();
    }

    @Override
    public float[][] multiplyMatrices(float[][] A, float[][] B) throws RemoteException {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Filas: " + aColumns + " no coincide con B:Columnas: " + bRows + ".");
        }

        float[][] C = new float[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                C[i][j] = 0.0f;
                for (int k = 0; k < aColumns; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }
}
