import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class MatrixClient {

    public static void main(String[] args) {
        try {
            // Cambiar localhost por las IP's corresondientes a cada nodo.
            String[] nodes = { "rmi://localhost/node0", "rmi://localhost/node1", "rmi://localhost/node2" };

            // Inicializa las matrices A y B
            int N = 9;
            int M = 4;
            float[][] A = initializeMatrixA(N, M);
            float[][] B = initializeMatrixB(M, N);
            float[][] BT = transposeMatrix(B);

            // Divide las matrices A y BT en 9 partes iguales
            List<float[][]> A_parts = divideMatrix(A, 9);
            List<float[][]> BT_parts = divideMatrix(BT, 9);

            // Realiza las multiplicaciones utilizando RMI y threads
            float[][][] C_parts = new float[81][][];
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int index = i * 9 + j;
                    int nodeIndex = index / 27;
                    MatrixOperations matrixOperations = (MatrixOperations) Naming.lookup(nodes[nodeIndex]);
                    final int i_final = i;
                    final int j_final = j;
                    Runnable task = () -> {
                        try {
                            C_parts[index] = matrixOperations.multiplyMatrices(A_parts.get(i_final),
                                    BT_parts.get(j_final));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    };
                    Thread thread = new Thread(task);
                    threads.add(thread);
                    thread.start();
                }
            }

            // Espera a que todos los threads terminen
            for (Thread thread : threads) {
                thread.join();
            }

            // Combina las matrices C_parts en la matriz C
            float[][] C = combineMatrix(C_parts, 9, N);

            // Despliega la matriz C y su checksum
            if (N == 9 && M == 4) {
                printMatrix(C);
            }
            double checksum = calculateChecksum(C);
            System.out.println("Checksum: " + checksum);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Métodos auxiliares para inicializar matrices, dividir, transponer, combinar y
    // calcular el checksum aquí

    private static float[][] initializeMatrixA(int N, int M) {
        float[][] A = new float[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                A[i][j] = 2 * i + 3 * j;
            }
        }
        return A;
    }

    private static float[][] initializeMatrixB(int M, int N) {
        float[][] B = new float[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                B[i][j] = 3 * i - 2 * j;
            }
        }
        return B;
    }

    private static float[][] transposeMatrix(float[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        float[][] transposed = new float[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                transposed[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }

    // private static List<float[][]> divideMatrix(float[][] matrix, int parts) {
    // printMatrix(matrix);
    // System.out.println("lol");
    // List<float[][]> dividedMatrix = new ArrayList<>();
    // int rows = matrix.length;
    // int columns = matrix[0].length;
    // int blockRows = rows / parts;
    // int blockColumns = columns / parts;

    // for (int i = 0; i < parts; i++) {
    // for (int j = 0; j < parts; j++) {
    // float[][] block = new float[blockRows][blockColumns];
    // for (int k = 0; k < blockRows; k++) {
    // System.arraycopy(matrix[i * blockRows + k], j * blockColumns, block[k], 0,
    // blockColumns);
    // }
    // dividedMatrix.add(block);
    // }
    // }
    // return dividedMatrix;
    // }

    private static List<float[][]> divideMatrix(float[][] matrix, int parts) {
        List<float[][]> dividedMatrix = new ArrayList<>();
        int rows = matrix.length;
        int columns = matrix[0].length;
        int blockRows = rows / (int) Math.sqrt(parts);
        int blockColumns = columns / (int) Math.sqrt(parts);

        for (int i = 0; i < rows; i += blockRows) {
            for (int j = 0; j < columns; j += blockColumns) {
                int maxRow = Math.min(i + blockRows, rows);
                int maxColumn = Math.min(j + blockColumns, columns);
                float[][] block = new float[maxRow - i][maxColumn - j];
                for (int k = i; k < maxRow; k++) {
                    System.arraycopy(matrix[k], j, block[k - i], 0, maxColumn - j);
                }
                dividedMatrix.add(block);
            }
        }
        return dividedMatrix;
    }

    private static float[][] combineMatrix(float[][][] C_parts, int parts, int N) {
        int blockRows = N / parts;
        int blockColumns = N / parts;
        float[][] C = new float[N][N];

        for (int i = 0; i < parts; i++) {
            for (int j = 0; j < parts; j++) {
                float[][] block = C_parts[i * parts + j];
                for (int k = 0; k < blockRows; k++) {
                    System.arraycopy(block[k], 0, C[i * blockRows + k], j * blockColumns, blockColumns);
                }
            }
        }

        return C;
    }

    private static double calculateChecksum(float[][] matrix) {
        double checksum = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                checksum += matrix[i][j];
            }
        }
        return checksum;
    }

    private static void printMatrix(float[][] matrix) {
        for (float[] row : matrix) {
            for (float value : row) {
                System.out.printf("%.2f ", value);
            }
            System.out.println();
        }
    }

}
