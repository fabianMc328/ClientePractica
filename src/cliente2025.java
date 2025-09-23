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
                String contraseña;
                do {
                    System.out.print("Nueva contraseña: ");
                    contraseña = scanner.nextLine();
                    if (contraseña.length() < 4) {
                        System.out.println("La contraseña debe tener mínimo 4 caracteres");
                    }
                } while (contraseña.length() < 4);

                escritor.println(usuario);
                escritor.println(contraseña);

                System.out.println("Servidor dice: " + lector.readLine());
                salida.close();

            } else if ("2".equals(opcion)) {
                boolean loginExitoso = false;

                while (!loginExitoso && !salir) {
                    Socket salida = new Socket("localhost", 8080);
                    PrintWriter escritor = new PrintWriter(salida.getOutputStream(), true);
                    BufferedReader lector = new BufferedReader(new InputStreamReader(salida.getInputStream()));

                    escritor.println(opcion);

                    System.out.print("Usuario: ");
                    String usuario = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String contraseña = scanner.nextLine();
                    escritor.println(usuario);
                    escritor.println(contraseña);

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
                        continue;
                    }

                    if ("CERRAR".equals(respuesta)) {
                        salir = true;
                        salida.close();
                        break;
                    }

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
                        System.out.println("4. Eliminar mensaje");
                        System.out.println("5. Leer mensajes");
                        System.out.println("6. Salir");
                        System.out.println("7. Eliminar mi cuenta ");
                        System.out.print("Elige una opción: ");
                        String accion = scanner.nextLine();
                        escritor.println(accion);

                        switch (accion) {
                            case "1":
                                while (!(linea = lector.readLine()).equals("FIN_LISTA")) {
                                    System.out.println("- " + linea);
                                }
                                break;

                            case "2":
                                while (true) {
                                    String mensajeJuego = lector.readLine();
                                    if (mensajeJuego == null) {
                                        System.out.println("Conexión cerrada inesperadamente.");
                                        sesionActiva = false;
                                        break;
                                    }
                                    if ("FIN_JUEGO".equals(mensajeJuego)) break;
                                    System.out.println(mensajeJuego);
                                    System.out.print("Tu intento: ");
                                    String intento = scanner.nextLine();
                                    escritor.println(intento);
                                }
                                break;

                            case "3":
                                System.out.print("Destinatario: ");
                                String destinatario = scanner.nextLine();
                                escritor.println(destinatario);

                                String respDestinatario = lector.readLine();
                                if ("NO_USUARIO".equals(respDestinatario)) {
                                    System.out.println("Usuario no registrado, no se puede enviar mensajes.");
                                    break;
                                }

                                System.out.print("Mensaje: ");
                                String mensaje = scanner.nextLine();
                                escritor.println(mensaje);
                                System.out.println(lector.readLine());
                                break;

                            case "4":
                                System.out.println("¿Quieres eliminar un mensaje recibido o enviado?");
                                System.out.println("1. Mensajes recibidos");
                                System.out.println("2. Mensajes enviados");
                                System.out.print("Elige una opción: ");
                                String tipoInput = scanner.nextLine();
                                String tipo = tipoInput.equals("1") ? "recibido" :
                                        tipoInput.equals("2") ? "enviado" : "";

                                if (tipo.isEmpty()) {
                                    System.out.println("Opción no válida para tipo de mensajes.");
                                    break;
                                }

                                escritor.println(tipo);

                                String mensajeServidor = lector.readLine();
                                if ("NO_HAY_MENSAJES".equals(mensajeServidor)) {
                                    System.out.println("No tienes mensajes " + tipo + " para eliminar.");
                                    break;
                                }

                                System.out.println("Tus mensajes:");
                                System.out.println(mensajeServidor);
                                while (!(linea = lector.readLine()).equals("FIN_LISTA")) {
                                    System.out.println(linea);
                                }

                                System.out.print("Elige el número del mensaje a eliminar: ");
                                String numEliminar = scanner.nextLine();
                                escritor.println(numEliminar);
                                System.out.println(lector.readLine());
                                break;

                            case "5":
                                String mensajeInicio = lector.readLine();
                                if ("NO_HAY_MENSAJES".equals(mensajeInicio)) {
                                    System.out.println("No tienes mensajes recibidos.");
                                    break;
                                }

                                boolean leyendo = true;
                                while (leyendo) {
                                    String lineaMensaje = lector.readLine();

                                    if ("FIN_LISTA".equals(lineaMensaje)) {
                                        leyendo = false;
                                        break;
                                    }

                                    System.out.println(lineaMensaje);

                                    if ("MAS_PAGINAS".equals(lineaMensaje)) {
                                        String pregunta = lector.readLine();
                                        System.out.print(pregunta + " ");
                                        String respuestaa = scanner.nextLine();
                                        escritor.println(respuestaa);

                                        if (!respuestaa.equalsIgnoreCase("siguiente")) {
                                            leyendo = false;
                                        }
                                    }

                                }
                                break;
                            case "7":
                                System.out.print("¿Estás seguro de eliminar tu cuenta? (si/no): ");
                                String confirmar = scanner.nextLine().trim().toLowerCase();
                                if (confirmar.equals("si")) {
                                    escritor.println(confirmar);
                                    String respuestaServidor = lector.readLine();
                                    if ("ELIMINADO_OK".equals(respuestaServidor)) {
                                        System.out.println("✅ Tu cuenta fue eliminada. Regresando al menú principal...");
                                        sesionActiva = false;
                                    } else if ("ELIMINADO_ERROR".equals(respuestaServidor)) {
                                        System.out.println(" Error al eliminar tu cuenta.");
                                    }
                                } else {
                                    escritor.println(confirmar);
                                    lector.readLine();
                                    System.out.println("Operación cancelada. Volviendo al menú cliente.");
                                }
                                break;

                            case "6":
                                sesionActiva = false;
                                break;

                            default:
                                System.out.println("Opción no válida.");
                        }
                    }

                    loginExitoso = true;
                    salida.close();
                }

            } else {
                System.out.println("Opción no válida.");
            }
        }

        scanner.close();
    }
}


















