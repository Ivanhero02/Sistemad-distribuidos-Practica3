import java.net.*;
import java.io.*;

public class ServidorHilos {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(4444); // Creamos un socket del servidor en el puerto 4444 
        } catch (IOException e) {
            System.err.println("No encuentro a alguien en el puerto: 4444.");
            System.exit(1);
        }

        // Esperamos a que un cliente se conecte al servidor y procesamos su solicitud en un hilo separado
        while (true) {
            Socket clientSocket = null;
            try {
                System.out.println("Esperando un cliente.. ");
                clientSocket = serverSocket.accept(); // Aceptamos la conexión entrante del cliente
                System.out.println("Conectado a: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                System.err.println("Acceptación fallida");
                System.exit(1);
            }

            // Creamos un nuevo hilo para procesar la solicitud del cliente
            Thread thread = new Thread(new ClientHandler(clientSocket));
            thread.start();
        }


    }
}

// Esta clase maneja las solicitudes del cliente en un hilo separado
class ClientHandler implements Runnable {
    private Socket clientSocket = null;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            // Creamos un objeto BufferedReader para leer datos del cliente
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Creamos un objeto PrintWriter para enviar datos al cliente
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) { // Leemos la solicitud del cliente
                String[] input = inputLine.split(","); // Separamos la solicitud del cliente en una matriz de cadenas utilizando la coma como delimitador
                String operation = input[0]; // La primera cadena representa la operación a realizar
                int x = Integer.parseInt(input[1]); // La segunda cadena representa el primer parámetro de la operación (x)
                int y = Integer.parseInt(input[2]); // La tercera cadena representa el segundo parámetro de la operación (y)
                int result = 0;

                // Realizamos la operación solicitada por el cliente y almacenamos el resultado en la variable "result"
                if (operation.equals("suma")) {
                    result = x + y;
                } else if (operation.equals("resta")) {
                    result = x - y;
                } else if (operation.equals("multiplicacion")) {
                    result = x * y;
                } else if (operation.equals("division")) {
                    result = x / y;
                } else {
                    out.println("Operación Invalida."); // Si se proporciona una operación no válida, informamos al cliente
                    continue;
                }

                out.println(result); // Enviamos el resultado de la operación al cliente
            }

            // Cerramos los flujos y el socket del cliente
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
