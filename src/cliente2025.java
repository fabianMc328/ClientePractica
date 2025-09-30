
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class cliente2025 {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String HOST = "localhost";
    private static final int PUERTO = 8080;

    public static void main(String[] args) {
        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1": procesarRegistro(); break;
                case "2": procesarLogin(); break;
                case "3": salir = true; System.out.println("Cliente cerrado."); break;
                default: System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ CLIENTE ===");
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar sesión");
        System.out.println("3. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void procesarRegistro() {
        try (Socket socket = new Socket(HOST, PUERTO);
             PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            escritor.println("1");
            System.out.print("Nuevo usuario: ");
            escritor.println(scanner.nextLine());

            String contrasena;
            do {
                System.out.print("Nueva contraseña (mínimo 4 caracteres): ");
                contrasena = scanner.nextLine();
            } while (contrasena.length() < 4);
            escritor.println(contrasena);

            System.out.println("Servidor dice: " + lector.readLine());

        } catch (IOException e) {
            System.out.println("Error de conexión con el servidor: " + e.getMessage());
        }
    }

    private static void procesarLogin() {
        try (Socket socket = new Socket(HOST, PUERTO);
             PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            escritor.println("2");
            System.out.print("Usuario: ");
            escritor.println(scanner.nextLine());
            System.out.print("Contraseña: ");
            escritor.println(scanner.nextLine());

            String respuesta = lector.readLine();
            if ("LOGIN_ERROR".equals(respuesta)) {
                System.out.println("Usuario o contraseña incorrectos.");
            } else {
                System.out.println(respuesta);
                lector.readLine();

                SesionUsuario sesion = new SesionUsuario(socket, escritor, lector, scanner);
                sesion.iniciar();
            }

        } catch (IOException e) {
            System.out.println("Error de conexión con el servidor: " + e.getMessage());
        }
    }
}