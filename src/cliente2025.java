import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class cliente2025 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader lectorServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("=== CLIENTE ===");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Iniciar sesión");
            System.out.print("Elige una opción: ");
            String opcion = scanner.nextLine();
            escritor.println(opcion);

            if ("1".equals(opcion)) {
                System.out.print("Usuario: ");
                String usuario = scanner.nextLine();
                System.out.print("Contraseña: ");
                String contrasena = scanner.nextLine();
                escritor.println(usuario);
                escritor.println(contrasena);

                System.out.println(lectorServidor.readLine());
            } else if ("2".equals(opcion)) {
                System.out.print("Usuario: ");
                String usuario = scanner.nextLine();
                System.out.print("Contraseña: ");
                String contrasena = scanner.nextLine();
                escritor.println(usuario);
                escritor.println(contrasena);

                String linea;
                while ((linea = lectorServidor.readLine()) != null) {
                    if ("MENU_OPCIONES".equals(linea)) {
                        System.out.print("Opción elegida: ");
                        String accion = scanner.nextLine();
                        escritor.println(accion);
                    } else if ("FIN_LISTA".equals(linea)) {
                        System.out.println("=== Fin de usuarios ===");
                    } else if ("FIN_JUEGO".equals(linea)) {
                        System.out.println("=== Fin del juego ===");
                    } else {
                        System.out.println(linea);
                        if (linea.contains("Usuario destinatario:")) {
                            String destinatario = scanner.nextLine();
                            escritor.println(destinatario);
                        } else if (linea.contains("Escribe tu mensaje:")) {
                            String mensaje = scanner.nextLine();
                            escritor.println(mensaje);
                        } else if (linea.startsWith("Adivina")) {
                            for (int i = 0; i < 3; i++) {
                                System.out.print("Tu intento: ");
                                String intento = scanner.nextLine();
                                escritor.println(intento);
                                String respuesta = lectorServidor.readLine();
                                System.out.println(respuesta);
                                if ("🎉 Adivinaste el número.".equals(respuesta)) break;
                                if (respuesta.contains("😢")) break;
                            }
                        }
                    }
                }
            } else {
                System.out.println("Opción no válida en cliente.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



