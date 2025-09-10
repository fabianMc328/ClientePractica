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

        if (respuesta.startsWith("✅ Bienvenido")) {
            String menu = lector.readLine();

            if ("MENU_OPCIONES".equals(menu)) {
                System.out.println("\n--- MENÚ ---");
                System.out.println("1. Mostrar usuarios registrados");
                System.out.println("2. Jugar un juego");
                System.out.print("Elige una opción: ");
                String accion = scanner.nextLine();
                escritor.println(accion);

                switch (accion) {
                    case "1":
                        System.out.println("\nUsuarios registrados:");
                        String linea;
                        while (!(linea = lector.readLine()).equals("FIN_LISTA")) {
                            System.out.println("- " + linea);
                        }
                        break;
                    case "2":
                        System.out.println("\n🎮 Comienza el juego:");
                        while (true) {
                            String mensaje = lector.readLine();
                            System.out.println(mensaje);
                            if (mensaje.startsWith("🎉") || mensaje.startsWith("😢")) {
                                break;
                            }
                            System.out.print("Tu intento: ");
                            String intento = scanner.nextLine();
                            escritor.println(intento);
                        }
                        break;
                    default:
                        System.out.println("❌ Opción no válida.");
                }
            }
        }

        scanner.close();
        salida.close();
    }
}

