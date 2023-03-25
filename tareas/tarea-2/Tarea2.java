
/**
 * Tarea 2
 * Desarrollo de Sistemas Distribuidos
 * Humberto Alejandro Ortega Alcocer
 * 24 de Marzo del 2023
 */
import java.io.*;
import java.net.*;

public class Tarea2 {
  // Direcciones IP de los nodos.
  public static final String IP_NODO_1 = "74.235.12.118",
      IP_NODO_2 = "20.25.1.241",
      IP_NODO_3 = "172.173.139.232";

  // Puertos de los nodos.
  public static final int PUERTO_NODO_1 = 8080,
      PUERTO_NODO_2 = 8081,
      PUERTO_NODO_3 = 8082;

  public static void main(String[] args) {
    // Obtenemos el número de nodo.
    int nodo = Integer.parseInt(args[0]);

    if (nodo == 0) {
      // Extraemos la dimensión de las matrices.
      int N = Integer.parseInt(args[1]);

      // Ejecutamos el nodo 0 (maestro).
      nodoMaestro(N);
    } else {
      nodoEsclavo(nodo);
    }
  }

  public static void nodoMaestro(int N) {
    // Inicializar matrices A y B
    double[][] A = inicializarMatrizA(N),
        B = inicializarMatrizB(N);

    // Transponer matriz B
    double[][] BT = transponerMatriz(B);

    // Dividir matrices A y BT en partes iguales
    double[][][] submatricesA = new double[3][N / 3][N],
        submatricesBT = new double[3][N / 3][N];

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        if (i < N / 3) {
          submatricesA[0][i][j] = A[i][j];
          submatricesBT[0][i][j] = BT[i][j];
        } else if (i < 2 * N / 3) {
          submatricesA[1][i - N / 3][j] = A[i][j];
          submatricesBT[1][i - N / 3][j] = BT[i][j];
        } else {
          submatricesA[2][i - 2 * N / 3][j] = A[i][j];
          submatricesBT[2][i - 2 * N / 3][j] = BT[i][j];
        }
      }
    }

    // Enviar matrices A1, A2, A3, BT1, BT2, BT3 a nodos 1, 2 y 3
    // utilizando sockets y recibir matrices C1, C2, C3, C4, C5, C6, C7, C8, C9
    // del nodo 1, 2 y 3 utilizando sockets
    try {
      Socket socket1 = new Socket(IP_NODO_1, PUERTO_NODO_1);
      Socket socket2 = new Socket(IP_NODO_2, PUERTO_NODO_2);
      Socket socket3 = new Socket(IP_NODO_3, PUERTO_NODO_3);

      ObjectOutputStream out1 = new ObjectOutputStream(socket1.getOutputStream());
      ObjectOutputStream out2 = new ObjectOutputStream(socket2.getOutputStream());
      ObjectOutputStream out3 = new ObjectOutputStream(socket3.getOutputStream());

      out1.writeObject(submatricesA[0]);
      out1.writeObject(submatricesBT[0]);
      out1.writeObject(submatricesBT[1]);
      out1.writeObject(submatricesBT[2]);

      out2.writeObject(submatricesA[1]);
      out2.writeObject(submatricesBT[0]);
      out2.writeObject(submatricesBT[1]);
      out2.writeObject(submatricesBT[2]);

      out3.writeObject(submatricesA[2]);
      out3.writeObject(submatricesBT[0]);
      out3.writeObject(submatricesBT[1]);
      out3.writeObject(submatricesBT[2]);

      ObjectInputStream in1 = new ObjectInputStream(socket1.getInputStream());
      ObjectInputStream in2 = new ObjectInputStream(socket2.getInputStream());
      ObjectInputStream in3 = new ObjectInputStream(socket3.getInputStream());

      double[][][] matrices = new double[9][N / 3][N];

      matrices[0] = (double[][]) in1.readObject();
      matrices[1] = (double[][]) in1.readObject();
      matrices[2] = (double[][]) in1.readObject();
      matrices[3] = (double[][]) in2.readObject();
      matrices[4] = (double[][]) in2.readObject();
      matrices[5] = (double[][]) in2.readObject();
      matrices[6] = (double[][]) in3.readObject();
      matrices[7] = (double[][]) in3.readObject();
      matrices[8] = (double[][]) in3.readObject();

      double[][] C = combinarSubmatrices(matrices, N);

      // Imprimir matrices A, B y C
      System.out.println("Matriz A:");
      imprimirMatriz(A);
      System.out.println();

      System.out.println("Matriz B:");
      imprimirMatriz(B);
      System.out.println();

      System.out.println("Matriz C:");
      imprimirMatriz(C);
      System.out.println();

      // Mostrar checksum
      System.out.println("Checksum: " + calcularChecksum(C));

      in1.close();
      in2.close();
      in3.close();

      out1.close();
      out2.close();
      out3.close();

      socket1.close();
      socket2.close();
      socket3.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void nodoEsclavo(int nodo) {
    // Recibir matrices A1, BT1, BT2, BT3 de nodo 0 utilizando sockets
    try {
      ServerSocket serverSocket;

      // Usamoe el puerto en cuestión.
      if (nodo == 1) {
        serverSocket = new ServerSocket(PUERTO_NODO_1);
      } else if (nodo == 2) {
        serverSocket = new ServerSocket(PUERTO_NODO_2);
      } else {
        serverSocket = new ServerSocket(PUERTO_NODO_3);
      }

      System.out.println("Nodo #" + nodo + " esperando conexión en el puerto " + serverSocket.getLocalPort() + "...");

      Socket socket = serverSocket.accept();

      ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

      double[][] A1 = (double[][]) in.readObject();
      double[][] BT1 = (double[][]) in.readObject();
      double[][] BT2 = (double[][]) in.readObject();
      double[][] BT3 = (double[][]) in.readObject();

      // Calcular matrices C1, C2, C3
      double[][] C1 = multiplicarMatrices(A1, BT1);
      double[][] C2 = multiplicarMatrices(A1, BT2);
      double[][] C3 = multiplicarMatrices(A1, BT3);

      // Enviar matrices C1, C2, C3 al nodo 0 utilizando sockets
      ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

      out.writeObject(C1);
      out.writeObject(C2);
      out.writeObject(C3);

      // Imprimimos matrices A1, BT1, BT2, BT3 y C1, C2, C3
      System.out.println("Matriz A1:");
      imprimirMatriz(A1);
      System.out.println();

      System.out.println("Matriz BT1:");
      imprimirMatriz(BT1);
      System.out.println();

      System.out.println("Matriz BT2:");
      imprimirMatriz(BT2);
      System.out.println();

      System.out.println("Matriz BT3:");
      imprimirMatriz(BT3);
      System.out.println();

      System.out.println("Matriz C1:");
      imprimirMatriz(C1);
      System.out.println();

      System.out.println("Matriz C2:");
      imprimirMatriz(C2);
      System.out.println();

      System.out.println("Matriz C3:");
      imprimirMatriz(C3);
      System.out.println();

      // Cerramos los flujos y sockets.
      out.close();
      in.close();
      socket.close();
      serverSocket.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  // Métodos adicionales para operaciones con matrices (inicialización,
  // transposición, división, multiplicación)

  public static double[][] inicializarMatrizA(int N) {
    double[][] A = new double[N][N];
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        A[i][j] = 2 * i + j;
      }
    }
    return A;
  }

  public static double[][] inicializarMatrizB(int N) {
    double[][] B = new double[N][N];
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        B[i][j] = 3 * i - j;
      }
    }
    return B;
  }

  public static double[][] transponerMatriz(double[][] A) {
    int N = A.length;
    double[][] AT = new double[N][N];
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        AT[i][j] = A[j][i];
      }
    }
    return AT;
  }

  public static double[][] combinarSubmatrices(double[][][] submatricesC, int N) {
    double[][] C = new double[N][N];

    for (int n = 0; n < 9; n++) {
      int rowOffset = (n / 3) * (N / 3);
      int colOffset = (n % 3) * (N / 3);

      for (int i = 0; i < N / 3; i++) {
        for (int j = 0; j < N / 3; j++) {
          C[rowOffset + i][colOffset + j] = submatricesC[n][i][j];
        }
      }
    }

    return C;
  }

  public static double calcularChecksum(double[][] C) {
    double checksum = 0.0;

    for (int i = 0; i < C.length; i++) {
      for (int j = 0; j < C[i].length; j++) {
        checksum += C[i][j];
      }
    }

    return checksum;
  }

  public static double[][][] calcularSubMatrices(double[][] A, double[][][] BT, int N) {
    double[][][] submatricesC = new double[3][N / 3][N];

    for (int n = 0; n < 3; n++) {
      for (int i = 0; i < N / 3; i++) {
        for (int j = 0; j < N / 3; j++) {
          for (int k = 0; k < N; k++) {
            submatricesC[n][i][j] += A[i][k] * BT[n][j][k];
          }
        }
      }
    }

    return submatricesC;
  }

  public static void imprimirMatriz(double[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        System.out.print(matrix[i][j] + " ");
      }
      System.out.println();
    }
  }

  public static double[][] multiplicarMatrices(double[][] A, double[][] B) {
    int N = A.length;
    double[][] C = new double[N][N];

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        for (int k = 0; k < N; k++) {
          C[i][j] += A[i][k] * B[j][k];
        }
      }
    }

    return C;
  }
}
