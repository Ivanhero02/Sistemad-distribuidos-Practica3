import java.io.*;
import java.net.*;

public class ClienteHilos {
    public static void main(String[] args) throws IOException {
        // Creamos un socket para conectarnos al servidor en el puerto 4444
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            socket = new Socket("localhost", 4444);
            out = new PrintWriter(socket.getOutputStream(), true); // Creamos un objeto PrintWriter para enviar datos al servidor
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Creamos un objeto BufferedReader para leer datos del servidor
        } catch (UnknownHostException e) {
            System.err.println("No sé sobre el host: localhost.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("No se pudo obtener E/S para la conexión a: localhost.");
            System.exit(1);
        }

        // Creamos un objeto BufferedReader para leer datos desde la consola
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        System.out.println("Escribe la operación a realizar");
        while ((userInput = stdIn.readLine()) != null) { // Leemos la entrada del usuario desde la consola
            String[] input = userInput.split(","); // Separamos la entrada del usuario en una matriz de cadenas utilizando la coma como delimitador
            String operation = input[0]; // La primera cadena representa la operación a realizar
            int x = Integer.parseInt(input[1]); // La segunda cadena representa el primer parámetro de la operación (x)
            int y = Integer.parseInt(input[2]); // La tercera cadena representa el segundo parámetro de la operación (y)

            out.println(operation + "," + x + "," + y); // Enviamos la solicitud al servidor en el formato "operación, x, y"
            System.out.println("Resultado: " + in.readLine()); // Leemos la respuesta del servidor y la mostramos en la consola
        }

        // Cerramos los flujos y el socket
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
}