

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SesionUsuario {
    private final Socket socket;
    private final PrintWriter escritor;
    private final BufferedReader lector;
    private final Scanner scanner;

    public SesionUsuario(Socket s, PrintWriter pw, BufferedReader br, Scanner sc) {
        this.socket = s;
        this.escritor = pw;
        this.lector = br;
        this.scanner = sc;
    }

    public void iniciar() throws IOException {
        boolean sesionActiva = true;
        while (sesionActiva) {
            mostrarMenuDeSesion();
            String accion = scanner.nextLine();
            escritor.println(accion);

            if ("6".equals(accion) || "7".equals(accion)) {
                sesionActiva = false;
            }
            procesarAccion(accion);
        }
    }

    private void mostrarMenuDeSesion() {
        System.out.println("\n--- MENÚ ---");
        System.out.println("1. Mostrar usuarios");
        System.out.println("2. Jugar");
        System.out.println("3. Enviar mensaje");
        System.out.println("4. Eliminar mensaje");
        System.out.println("5. Leer mensajes");
        System.out.println("6. Salir");
        System.out.println("7. Eliminar mi cuenta");
        System.out.println("8. Bloquear usuario");
        System.out.println("9. Desbloquear usuario");
        System.out.println("10. Solicitar ver archivos");
        System.out.println("11. Leer solicitudes de archivos");
        System.out.println("12. Ver archivos compartidos");
        System.out.print("Elige una opción: ");
    }

    private void procesarAccion(String accion) throws IOException {
        switch (accion) {
            case "1": mostrarUsuarios(); break;
            case "2": jugarAdivinaElNumero(); break;
            case "3": enviarMensaje(); break;
            case "4": eliminarMensaje(); break;
            case "5": leerMensajes(); break;
            case "6": System.out.println("Cerrando sesión..."); break;
            case "7": eliminarMiCuenta(); break;
            case "8": bloquearUsuario(); break;
            case "9": desbloquearUsuario(); break;
            case "10": solicitarVerArchivos(); break;
            case "11": leerSolicitudesDeArchivos(); break;
            case "12": verYCopiarArchivosCompartidos(); break;
            default: System.out.println(lector.readLine());
        }
    }

    private void leerHasta(String tokenFinal) throws IOException {
        String linea;
        while (!(linea = lector.readLine()).equals(tokenFinal)) {
            System.out.println(linea);
        }
    }

    private void mostrarUsuarios() throws IOException {
        leerHasta("FIN_LISTA");
    }

    private void jugarAdivinaElNumero() throws IOException {
        System.out.println(lector.readLine());

        while (true) {
            System.out.print("Tu intento: ");
            escritor.println(scanner.nextLine());

            String respuestaServidor = lector.readLine();
            System.out.println(respuestaServidor);


            if (respuestaServidor.contains("🎉") || respuestaServidor.contains("😢")) {
                lector.readLine();
                break;
            }
        }
    }

    private void enviarMensaje() throws IOException {
        System.out.print("Destinatario: ");
        escritor.println(scanner.nextLine());
        String respuesta = lector.readLine();
        if ("OK".equals(respuesta)) {
            System.out.print("Mensaje: ");
            escritor.println(scanner.nextLine());
            System.out.println(lector.readLine());
        } else if ("NO_USUARIO".equals(respuesta)) {
            System.out.println("Usuario no registrado.");
        } else if ("USUARIO_BLOQUEADO".equals(respuesta)) {
            System.out.println("Estás bloqueado por este usuario.");
        }
    }

    private void eliminarMensaje() throws IOException {
        System.out.println("Eliminar mensaje:\n1. Recibido\n2. Enviado");
        System.out.print("Opción: ");
        String tipo = "1".equals(scanner.nextLine()) ? "recibido" : "enviado";
        escritor.println(tipo);

        String respuesta = lector.readLine();
        if ("NO_HAY_MENSAJES".equals(respuesta)) {
            System.out.println("No hay mensajes de este tipo.");
            return;
        }
        System.out.println(respuesta);
        leerHasta("FIN_LISTA");

        System.out.print("Número de mensaje a eliminar: ");
        escritor.println(scanner.nextLine());
        System.out.println(lector.readLine());
    }

    private void leerMensajes() throws IOException {
        if ("NO_HAY_MENSAJES".equals(lector.readLine())) {
            System.out.println("No tienes mensajes.");
            return;
        }
        String linea;
        while (!(linea = lector.readLine()).equals("FIN_LISTA")) {
            System.out.println(linea);
            if ("MAS_PAGINAS".equals(linea)) {
                System.out.print(lector.readLine() + " ");
                escritor.println(scanner.nextLine());
            }
        }
    }

    private void eliminarMiCuenta() throws IOException {
        System.out.print("¿Estás seguro de eliminar tu cuenta? (si/no): ");
        escritor.println(scanner.nextLine());
        String respuesta = lector.readLine();
        if ("ELIMINADO_OK".equals(respuesta)) {
            System.out.println("✅ Cuenta eliminada. Adiós.");
        } else {
            System.out.println("Operación cancelada o fallida.");
        }
    }

    private void bloquearUsuario() throws IOException {
        System.out.print("Usuario a bloquear: ");
        escritor.println(scanner.nextLine());
        System.out.println(lector.readLine());
    }

    private void desbloquearUsuario() throws IOException {
        System.out.print("Usuario a desbloquear: ");
        escritor.println(scanner.nextLine());
        System.out.println(lector.readLine());
    }

    private void solicitarVerArchivos() throws IOException {
        System.out.print("Usuario al que solicitar acceso: ");
        escritor.println(scanner.nextLine());
        System.out.println(lector.readLine());
    }

    private void leerSolicitudesDeArchivos() throws IOException {
        String respuesta = lector.readLine();
        if ("SOLICITUD_ARCHIVOS".equals(respuesta)) {
            String solicitante = lector.readLine();
            System.out.println("El usuario '" + solicitante + "' quiere ver tus .txt. ¿Aceptas? (si/no)");
            escritor.println(scanner.nextLine());

            if ("PEDIR_LISTA_Y_CONTENIDO".equals(lector.readLine())) {
                System.out.println("Enviando archivos al servidor...");
                File dir = new File(".");
                File[] archivos = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".txt"));
                if (archivos != null) {
                    for (File f : archivos) {
                        escritor.println(f.getName());
                        try (BufferedReader fr = new BufferedReader(new FileReader(f))) {
                            fr.lines().forEach(escritor::println);
                        }
                        escritor.println("_ENDFILE_");
                    }
                }
                escritor.println("_END_ALL_FILES_");
            }
            System.out.println(lector.readLine());
        } else {
            System.out.println(respuesta);
        }
        lector.readLine();
    }

    private void verYCopiarArchivosCompartidos() throws IOException {
        String encabezado = lector.readLine();
        System.out.println(encabezado);
        if ("No tienes archivos compartidos para ver.".equals(encabezado)) return;

        leerHasta("FIN_LISTA_ARCHIVOS");

        System.out.print("\n¿Copiar algún archivo? (si/no): ");
        if ("si".equalsIgnoreCase(scanner.nextLine())) {
            System.out.print("Nombre exacto del archivo: ");
            String archivoACopiar = scanner.nextLine();
            escritor.println("_REQUEST_COPY_");
            escritor.println(archivoACopiar);

            System.out.println("📥 Recibiendo archivo...");
            try (PrintWriter fw = new PrintWriter("copia_de_" + archivoACopiar)) {
                String linea;
                while (!(linea = lector.readLine()).equals("_ENDFILE_")) {
                    fw.println(linea);
                }
                System.out.println("✅ Archivo guardado como 'copia_de_" + archivoACopiar + "'");
            }
        } else {
            escritor.println("_NO_COPY_");
        }
    }
}
