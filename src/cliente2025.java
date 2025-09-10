import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class cliente2025 {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        Socket salida = new Socket("localhost", 8080);
        PrintWriter escritor = new PrintWriter(salida.getOutputStream(), true);
        BufferedReader lector = new BufferedReader(new InputStreamReader(salida.getInputStream()));

        System.out.println("=== MENÚ CLIENTE ===");
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar sesión");
        System.out.print("Elige una opción: ");
        String opcion = scanner.nextLine();
        escritor.println(opcion);

        if ("1".equals(opcion)) {
            System.out.print("Nuevo usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Nueva contraseña: ");
            String contrasena = scanner.nextLine();
            escritor.println(usuario);
            escritor.println(contrasena);
        } else if ("2".equals(opcion)) {
            System.out.print("Usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine();
            escritor.println(usuario);
            escritor.println(contrasena);
        }

        String respuesta = lector.readLine();
        System.out.println("Servidor dice: " + respuesta);

        // NUEVO: Mostrar menú de usuarios si login fue exitoso
        if (respuesta.startsWith("✅ Bienvenido")) {
            String menu = lector.readLine();

            if ("MENU_USUARIOS".equals(menu)) {
                System.out.println("\n--- MENÚ USUARIOS ---");
                System.out.println("1. Mostrar usuarios registrados");
                System.out.print("Elige una opción: ");
                String accion = scanner.nextLine();
                escritor.println(accion);

                if ("1".equals(accion)) {
                    System.out.println("Usuarios registrados:");
                    String linea;
                    while (!(linea = lector.readLine()).equals("FIN_LISTA")) {
                        System.out.println("- " + linea);
                    }
                }
            }
        }

        scanner.close();
        salida.close();
    }
}
