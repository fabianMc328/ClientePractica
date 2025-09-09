import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class cliente2025 {
    static  Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        Socket salida  = new Socket ("localhost", 8080);
        PrintWriter escritor= new PrintWriter(salida.getOutputStream(), true);

        BufferedReader lector  = new BufferedReader(new InputStreamReader(salida.getInputStream()));
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        String mensaje;


        System.out.println("=== MENÚ CLIENTE ===");
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar sesión");
        System.out.print("Elige una opción: ");

        String opcion = scanner.nextLine();
        escritor.println(opcion);

        if ("1".equals(opcion)) {
            // Registro
            System.out.print("Nuevo usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Nueva contraseña: ");
            String contrasena = scanner.nextLine();

            escritor.println(usuario);
            escritor.println(contrasena);
        } else if ("2".equals(opcion)) {
            // Login
            System.out.print("Usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine();

            escritor.println(usuario);
            escritor.println(contrasena);
        }

        // Respuesta del servidor
        String respuesta = lector.readLine();
        System.out.println("Servidor dice: " + respuesta);


/*
        String cadena = teclado.readLine();

        while (!cadena.equalsIgnoreCase("FIN")) {
            escritor.println(cadena);
            mensaje = lector.readLine();
         System.out.println(mensaje);

            if (mensaje.equalsIgnoreCase("fin")) {
                break;
            }
            if (mensaje.equalsIgnoreCase("no pudiste adivinar el numero") ||
                    mensaje.equalsIgnoreCase("ese es el numero felicidades")) {
                break;
            }


            cadena = teclado.readLine();
        }
*/      scanner.close();
        salida.close();
    }
}
