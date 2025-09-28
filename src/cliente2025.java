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
                        boolean volverAlMenu = false;
                        boolean volverIntentar = true;
                        while (volverIntentar) {
                            System.out.println("1. Volver a intentar login");
                            System.out.println("2. Regresar");
                            System.out.print("Elige una opción: ");
                            String subopcion = scanner.nextLine();
                            if ("1".equals(subopcion)) {
                                volverIntentar = false;
                            } else if ("2".equals(subopcion)) {
                                volverAlMenu = true;
                                volverIntentar = false;
                            } else {
                                System.out.println("Opción no válida.");
                            }
                        }
                        salida.close();
                        if (volverAlMenu) break;
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
                        System.out.println("8. Bloquear usuario");
                        System.out.println("9. Desbloquear usuario");
                        System.out.println("10. Solicitar ver archivos de otro usuario");
                        System.out.println("11. Leer solicitudes de archivos");
                        System.out.println("12. Ver archivos compartidos");
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
                                System.out.println(lector.readLine());
                                while (true) {
                                    System.out.print("Tu intento: ");
                                    String intento = scanner.nextLine();
                                    escritor.println(intento);
                                    String respServidor = lector.readLine();
                                    System.out.println(respServidor);
                                    if (respServidor.contains("🎉") || respServidor.contains("😢")) {
                                        lector.readLine(); // FIN_JUEGO
                                        break;
                                    }
                                }
                                break;

                            case "3":
                                System.out.print("Destinatario: ");
                                String destinatario = scanner.nextLine();
                                escritor.println(destinatario);
                                String respDest = lector.readLine();
                                if ("NO_USUARIO".equals(respDest)) {
                                    System.out.println("Usuario no registrado, no se puede enviar mensajes.");
                                    break;
                                } else if ("USUARIO_BLOQUEADO".equals(respDest)) {
                                    System.out.println("No puedes enviar mensajes a este usuario, estás bloqueado.");
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
                                String tipo;
                                if ("1".equals(tipoInput)) tipo = "recibido";
                                else if ("2".equals(tipoInput)) tipo = "enviado";
                                else { System.out.println("Opción no válida"); lector.readLine(); break; }
                                escritor.println(tipo);

                                String mensajesServidor = lector.readLine();
                                if ("NO_HAY_MENSAJES".equals(mensajesServidor)) {
                                    System.out.println("No hay mensajes " + tipo);
                                    break;
                                }

                                System.out.println("Tus mensajes:");
                                System.out.println(mensajesServidor);
                                while (!(linea = lector.readLine()).equals("FIN_LISTA")) System.out.println(linea);

                                System.out.print("Número de mensaje a eliminar: ");
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
                                        if (!respuestaa.equalsIgnoreCase("siguiente")) leyendo = false;
                                    }
                                }
                                break;

                            case "6":
                                sesionActiva = false;
                                break;

                            case "7":
                                System.out.print("¿Estás seguro de eliminar tu cuenta? (si/no): ");
                                String confirmar = scanner.nextLine().trim().toLowerCase();
                                escritor.println(confirmar);
                                String respServidor = lector.readLine();
                                if ("ELIMINADO_OK".equals(respServidor)) {
                                    System.out.println("✅ Tu cuenta fue eliminada. Regresando al menú principal...");
                                    sesionActiva = false;
                                } else if ("ELIMINADO_ERROR".equals(respServidor)) {
                                    System.out.println("Error al eliminar tu cuenta.");
                                } else {
                                    System.out.println("Operación cancelada.");
                                }
                                break;

                            case "8":
                                System.out.print("Ingrese usuario a bloquear: ");
                                String bloquear = scanner.nextLine();
                                escritor.println(bloquear);
                                System.out.println(lector.readLine());
                                break;

                            case "9":
                                System.out.print("Ingrese usuario a desbloquear: ");
                                String desbloquear = scanner.nextLine();
                                escritor.println(desbloquear);
                                System.out.println(lector.readLine());
                                break;

                            case "10":
                                System.out.print("Ingrese el usuario al que desea solicitar ver archivos: ");
                                String objetivo = scanner.nextLine();
                                escritor.println(objetivo);
                                System.out.println(lector.readLine());
                                break;

                            case "11":
                                String respSolicitud;
                                while (!(respSolicitud = lector.readLine()).equals("FIN_SOLICITUDES")) {
                                    if (respSolicitud.equals("SOLICITUD_ARCHIVOS")) {
                                        String usuarioSolicitante = lector.readLine();
                                        System.out.println("El usuario '" + usuarioSolicitante + "' quiere ver tus archivos. ¿Deseas compartirlos? (si/no)");
                                        String decision = scanner.nextLine();
                                        escritor.println(decision);

                                        if ("si".equalsIgnoreCase(decision)) {
                                            String sig = lector.readLine(); // ARCHIVOS_COMPARTIDOS
                                            System.out.println(sig);
                                        } else {
                                            System.out.println(lector.readLine()); // NO_COMPARTIDOS
                                        }
                                    }
                                }
                                break;

                            case "12":
                                escritor.println("12");
                                String respArchivos;
                                while (!(respArchivos = lector.readLine()).equals("FIN_LISTA_ARCHIVOS")) {
                                    if ("NO_HAY_ARCHIVOS".equals(respArchivos)) {
                                        System.out.println("No hay archivos compartidos pendientes.");
                                        break;
                                    }
                                    System.out.println(respArchivos);
                                }
                                break;


                            default:
                                System.out.println("Opción no válida.");
                                lector.readLine();
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
