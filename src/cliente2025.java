import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class cliente2025 {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        boolean salir = false;

        while (!salir) {
            System.out.println("=== MENÚ CLIENTE ===");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Salir");
            System.out.print("Elige una opción: ");
            String opcion = scanner.nextLine();

            if ("3".equals(opcion)) {
                salir = true;
                System.out.println("Cliente cerrado.");
                break;
            }

            if ("1".equals(opcion)) {
                Socket salida = new Socket("localhost", 8080);
                PrintWriter escritor = new PrintWriter(salida.getOutputStream(), true);
                BufferedReader lector = new BufferedReader(new InputStreamReader(salida.getInputStream()));

                escritor.println(opcion);
                System.out.print("Nuevo usuario: ");
                String usuario = scanner.nextLine();
                System.out.print("Nueva contraseña: ");
                String contrasena = scanner.nextLine();
                escritor.println(usuario);
                escritor.println(contrasena);

                String respuesta = lector.readLine();
                System.out.println("Servidor dice: " + respuesta);

                salida.close();
            }

            else if ("2".equals(opcion)) {
                boolean loginExitoso = false;

                while (!loginExitoso && !salir) {
                    Socket salida = new Socket("localhost", 8080);
                    PrintWriter escritor = new PrintWriter(salida.getOutputStream(), true);
                    BufferedReader lector = new BufferedReader(new InputStreamReader(salida.getInputStream()));

                    escritor.println(opcion);

                    System.out.print("Usuario: ");
                    String usuario = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String contrasena = scanner.nextLine();
                    escritor.println(usuario);
                    escritor.println(contrasena);

                    String respuesta = lector.readLine();

                    if ("LOGIN_ERROR".equals(respuesta)) {
                        System.out.println("Usuario o contraseña incorrectos.");
                        boolean volverIntentar = true;
                        while (volverIntentar) {
                            System.out.println("1. Volver a intentar login");
                            System.out.println("2. Salir");
                            System.out.print("Elige una opción: ");
                            String subopcion = scanner.nextLine();
                            if ("1".equals(subopcion)) {
                                volverIntentar = false;
                            } else if ("2".equals(subopcion)) {
                                volverIntentar = false;
                                salir = true;
                            } else {
                                System.out.println("Opción no válida.");
                            }
                        }
                        salida.close();
                    }

                    else if ("CERRAR".equals(respuesta)) {
                        salir = true;
                        salida.close();
                        break;
                    }

                    else {
                        System.out.println(respuesta);

                        String linea;
                        while ((linea = lector.readLine()) != null) {
                            if (linea.equals("MENU_OPCIONES")) break;
                            System.out.println(linea);
                        }

                        boolean sesionActiva = true;
                        while (sesionActiva && !salir) {
                            System.out.println("\n--- MENÚ ---");
                            System.out.println("1. Mostrar usuarios registrados");
                            System.out.println("2. Jugar un juego");
                            System.out.println("3. Enviar mensaje");
                            System.out.println("4. Salir");
                            System.out.print("Elige una opción: ");
                            String accion = scanner.nextLine();
                            escritor.println(accion);

                            switch (accion) {
                                case "1":
                                    System.out.println("Usuarios registrados:");
                                    while (!(linea = lector.readLine()).equals("FIN_LISTA")) {
                                        System.out.println("- " + linea);
                                    }
                                    break;
                                case "2":
                                    System.out.println("Comienza el juego:");
                                    while (true) {
                                        String mensajeJuego = lector.readLine();
                                        if (mensajeJuego == null) {
                                            System.out.println("Conexión cerrada inesperadamente.");
                                            sesionActiva = false;
                                            break;
                                        }
                                        System.out.println(mensajeJuego);
                                        if (mensajeJuego.equals("FIN_JUEGO") ||
                                                mensajeJuego.startsWith("🎉") ||
                                                mensajeJuego.startsWith("😢") ||
                                                mensajeJuego.startsWith("Se acabaron")) {
                                            break;
                                        }
                                        System.out.print("Tu intento: ");
                                        String intento = scanner.nextLine();
                                        escritor.println(intento);
                                    }
                                    break;
                                case "3":
                                    System.out.print("Destinatario: ");
                                    String destinatario = scanner.nextLine();
                                    escritor.println(destinatario);
                                    System.out.print("Mensaje: ");
                                    String mensaje = scanner.nextLine();
                                    escritor.println(mensaje);
                                    String confirmacion = lector.readLine();
                                    System.out.println(confirmacion);
                                    break;
                                case "4":
                                    sesionActiva = false;
                                    break;
                                default:
                                    System.out.println("Opción no válida.");
                            }
                        }

                        loginExitoso = true;
                        salida.close();
                    }
                }
            }

            else {
                System.out.println("Opción no válida.");
            }
        }

        scanner.close();
    }
}








