package org.example.vista;

import org.example.dao.OperacionesCrud;
import org.example.dao.SolicitudDAO;
import org.example.dao.UsuarioDAO;
import org.example.modelo.Administrador;
import org.example.modelo.EstadoSolicitud;
import org.example.modelo.EvidenciaEntrega;
import org.example.modelo.Solicitud;
import org.example.modelo.TipoEntrega;
import org.example.modelo.TipoSolicitud;
import org.example.modelo.UsuarioConsulta;
import org.example.modelo.UsuarioSistema;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Menu {
    private final BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
    private final OperacionesCrud<Solicitud, String> solicitudDAO = new SolicitudDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private UsuarioSistema administrador;
    private UsuarioSistema usuarioConsulta;

    public void iniciar() {
        prepararUsuariosDemo();

        int opcion = 0;
        do {
            imprimirMenuInicio();
            try {
                opcion = leerEntero("Elige una opcion: ");
                switch (opcion) {
                    case 1:
                        if (administrador.puedeAdministrarSolicitudes()) {
                            menuAdministrador();
                        }
                        break;
                    case 2:
                        System.out.println("El usuario de consulta ya esta modelado, pero su modulo queda pendiente.");
                        System.out.println(usuarioConsulta);
                        break;
                    case 3:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                        break;
                }
            } catch (IOException | IllegalArgumentException err) {
                System.out.println("No se pudo continuar: " + err.getMessage());
            }
        } while (opcion != 3);
    }

    private void prepararUsuariosDemo() {
        administrador = new Administrador(0, "Administrador General", "admin@voztestimonio.com", true);
        usuarioConsulta = new UsuarioConsulta(0, "Usuario Consulta", "consulta@voztestimonio.com", true);

        administrador = usuarioDAO.obtenerOCrearUsuarioDemo(administrador, "admin_demo_hash");
        usuarioConsulta = usuarioDAO.obtenerOCrearUsuarioDemo(usuarioConsulta, "consulta_demo_hash");
    }

    private void imprimirMenuInicio() {
        System.out.println("\n========== VYT SYSTEMS ==========");
        System.out.println("1. Entrar como administrador");
        System.out.println("2. Entrar como usuario de consulta");
        System.out.println("3. Salir");
        System.out.println("=================================");
    }

    private void menuAdministrador() throws IOException {
        int opcion = 0;
        do {
            imprimirMenuAdministrador();
            try {
                opcion = leerEntero("Elige una opcion: ");
                switch (opcion) {
                    case 1:
                        agregarSolicitud();
                        break;
                    case 2:
                        editarSolicitud();
                        break;
                    case 3:
                        eliminarSolicitud();
                        break;
                    case 4:
                        mostrarTodasLasSolicitudes();
                        break;
                    case 5:
                        filtrarSolicitudes();
                        break;
                    case 6:
                        mostrarUsuarios();
                        break;
                    case 7:
                        System.out.println("Regresando al menu inicial...");
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                        break;
                }
            } catch (IllegalArgumentException err) {
                System.out.println("Datos invalidos: " + err.getMessage());
            }
        } while (opcion != 7);
    }

    private void imprimirMenuAdministrador() {
        System.out.println("\n========== MENU ADMINISTRADOR ==========");
        System.out.println("1. Agregar nueva solicitud");
        System.out.println("2. Editar solicitud");
        System.out.println("3. Eliminar solicitud");
        System.out.println("4. Visualizar todas las solicitudes");
        System.out.println("5. Filtrar solicitudes");
        System.out.println("6. Mostrar usuarios creados");
        System.out.println("7. Volver");
        System.out.println("========================================");
    }

    private void agregarSolicitud() throws IOException {
        System.out.println("\n--- Nueva solicitud ---");
        Solicitud solicitud = leerSolicitudNueva();
        solicitudDAO.agregar(solicitud);
    }

    private void editarSolicitud() throws IOException {
        System.out.println("\n--- Editar solicitud ---");
        String folio = leerTexto("Folio de la solicitud a editar: ");
        Solicitud actual = solicitudDAO.buscarPorId(folio);

        if (actual == null) {
            System.out.println("No existe una solicitud con ese folio.");
            return;
        }

        System.out.println("Solicitud actual:");
        System.out.println(actual);
        System.out.println("Escribe el nuevo valor o presiona Enter para conservar el dato actual.");

        Solicitud editada = leerSolicitudEditada(actual);
        solicitudDAO.editar(editada);
    }

    private void eliminarSolicitud() throws IOException {
        System.out.println("\n--- Eliminar solicitud ---");
        String folio = leerTexto("Folio de la solicitud a eliminar: ");
        System.out.print("Confirma escribiendo SI: ");
        String confirmacion = leer.readLine();

        if ("SI".equalsIgnoreCase(confirmacion)) {
            solicitudDAO.eliminar(folio);
        } else {
            System.out.println("Eliminacion cancelada.");
        }
    }

    private void mostrarTodasLasSolicitudes() {
        ArrayList<Solicitud> solicitudes = solicitudDAO.listar();
        imprimirSolicitudes(solicitudes);
    }

    private void filtrarSolicitudes() throws IOException {
        System.out.println("\nPuedes filtrar por folio, lugar, asunto, tipo, estado, entrega, area o evidencia.");
        String texto = leerTexto("Texto a buscar: ");
        ArrayList<Solicitud> solicitudes = solicitudDAO.filtrar(texto);
        imprimirSolicitudes(solicitudes);
    }

    private void mostrarUsuarios() {
        ArrayList<UsuarioSistema> usuarios = new ArrayList<>();
        usuarios.add(administrador);
        usuarios.add(usuarioConsulta);

        System.out.println("\n========== USUARIOS DEL SISTEMA ==========");
        for (UsuarioSistema usuario : usuarios) {
            System.out.println(usuario);
            System.out.println("------------------------------------------");
        }
    }

    private Solicitud leerSolicitudNueva() throws IOException {
        String folio = leerTexto("Folio de solicitud: ");
        LocalDate fechaFolio = leerFecha("Fecha de solicitud (yyyy-MM-dd): ");
        String lugar = leerTexto("Lugar de solicitud: ");
        String asunto = leerTexto("Asunto corto: ");
        TipoSolicitud tipoSolicitud = leerTipoSolicitud("Tipo de solicitud:");
        EstadoSolicitud estadoSolicitud = leerEstadoSolicitud("Estado de solicitud:");
        int idUsuario = obtenerIdUsuarioAdministrador();
        TipoEntrega tipoEntrega = leerTipoEntrega("Tipo de entrega:");
        LocalDate fechaEntrega = leerFecha("Fecha de entrega (yyyy-MM-dd): ");
        LocalTime horaEntrega = leerHora("Hora de entrega (HH:mm): ");
        String areaRecepcion = leerTexto("Autoridad o area receptora: ");
        String cargoRecepcion = leerTexto("Cargo de quien recibe: ");
        String correoEntrega = leerTextoOpcional("Correo de entrega (opcional): ", null);
        EvidenciaEntrega evidenciaEntrega = leerEvidenciaEntrega("Evidencia de entrega:");
        LocalDate fechaVencimiento = leerFecha("Fecha de vencimiento (yyyy-MM-dd): ");

        return new Solicitud(
                folio,
                fechaFolio,
                lugar,
                asunto,
                tipoSolicitud,
                estadoSolicitud,
                idUsuario,
                tipoEntrega,
                fechaEntrega,
                horaEntrega,
                areaRecepcion,
                cargoRecepcion,
                correoEntrega,
                evidenciaEntrega,
                fechaVencimiento
        );
    }

    private Solicitud leerSolicitudEditada(Solicitud actual) throws IOException {
        LocalDate fechaFolio = leerFechaOpcional("Fecha de solicitud (" + actual.getFechaFolio() + "): ", actual.getFechaFolioValor());
        String lugar = leerTextoOpcional("Lugar (" + actual.getLugarSolicitud() + "): ", actual.getLugarSolicitudValor());
        String asunto = leerTextoOpcional("Asunto (" + actual.getAsuntoSolicitud() + "): ", actual.getAsuntoSolicitudValor());
        TipoSolicitud tipoSolicitud = leerTipoSolicitudOpcional(actual.getTipoSolicitudEnum());
        EstadoSolicitud estadoSolicitud = leerEstadoSolicitudOpcional(actual.getEstadoSolicitudEnum());
        int idUsuario = actual.getIdUsuario();
        TipoEntrega tipoEntrega = leerTipoEntregaOpcional(actual.getTipoEntregaEnum());
        LocalDate fechaEntrega = leerFechaOpcional("Fecha de entrega (" + actual.getFechaEntrega() + "): ", actual.getFechaEntregaValor());
        LocalTime horaEntrega = leerHoraOpcional("Hora de entrega (" + actual.getHoraEntrega() + "): ", actual.getHoraEntregaValor());
        String areaRecepcion = leerTextoOpcional("Area receptora (" + actual.getAreaRecepcion() + "): ", actual.getAreaRecepcionValor());
        String cargoRecepcion = leerTextoOpcional("Cargo recepcion (" + actual.getCargoRecepcion() + "): ", actual.getCargoRecepcionValor());
        String correoEntrega = leerTextoOpcional("Correo entrega (" + actual.getCorreoEntrega() + "): ", actual.getCorreoEntregaValor());
        EvidenciaEntrega evidenciaEntrega = leerEvidenciaEntregaOpcional(actual.getEvidenciaEntregaEnum());
        LocalDate fechaVencimiento = leerFechaOpcional("Fecha de vencimiento (" + actual.getFechaVencimiento() + "): ", actual.getFechaVencimientoValor());

        return new Solicitud(
                actual.getFolioSolicitud(),
                fechaFolio,
                lugar,
                asunto,
                tipoSolicitud,
                estadoSolicitud,
                idUsuario,
                tipoEntrega,
                fechaEntrega,
                horaEntrega,
                areaRecepcion,
                cargoRecepcion,
                correoEntrega,
                evidenciaEntrega,
                fechaVencimiento
        );
    }

    private int obtenerIdUsuarioAdministrador() throws IOException {
        if (administrador.getIdUsuario() > 0) {
            return administrador.getIdUsuario();
        }
        return leerEntero("IdUsuario administrador existente en la BD: ");
    }

    private void imprimirSolicitudes(ArrayList<Solicitud> solicitudes) {
        System.out.println("\n========== SOLICITUDES ==========");
        if (solicitudes.isEmpty()) {
            System.out.println("No hay solicitudes para mostrar.");
            return;
        }

        for (Solicitud solicitud : solicitudes) {
            System.out.println(solicitud);
        }
    }

    private String leerTexto(String mensaje) throws IOException {
        System.out.print(mensaje);
        String valor = leer.readLine();
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El dato no puede estar vacio.");
        }
        return valor.trim();
    }

    private String leerTextoOpcional(String mensaje, String actual) throws IOException {
        System.out.print(mensaje);
        String valor = leer.readLine();
        if (valor == null || valor.isBlank()) {
            return actual;
        }
        return valor.trim();
    }

    private int leerEntero(String mensaje) throws IOException {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(leer.readLine());
            } catch (NumberFormatException err) {
                System.out.println("Debes escribir un numero entero.");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) throws IOException {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalDate.parse(leer.readLine());
            } catch (DateTimeParseException err) {
                System.out.println("Formato incorrecto. Usa yyyy-MM-dd.");
            }
        }
    }

    private LocalDate leerFechaOpcional(String mensaje, LocalDate actual) throws IOException {
        while (true) {
            System.out.print(mensaje);
            String valor = leer.readLine();
            if (valor == null || valor.isBlank()) {
                return actual;
            }
            try {
                return LocalDate.parse(valor);
            } catch (DateTimeParseException err) {
                System.out.println("Formato incorrecto. Usa yyyy-MM-dd.");
            }
        }
    }

    private LocalTime leerHora(String mensaje) throws IOException {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalTime.parse(leer.readLine());
            } catch (DateTimeParseException err) {
                System.out.println("Formato incorrecto. Usa HH:mm.");
            }
        }
    }

    private LocalTime leerHoraOpcional(String mensaje, LocalTime actual) throws IOException {
        while (true) {
            System.out.print(mensaje);
            String valor = leer.readLine();
            if (valor == null || valor.isBlank()) {
                return actual;
            }
            try {
                return LocalTime.parse(valor);
            } catch (DateTimeParseException err) {
                System.out.println("Formato incorrecto. Usa HH:mm.");
            }
        }
    }

    private TipoSolicitud leerTipoSolicitud(String titulo) throws IOException {
        while (true) {
            System.out.println(titulo);
            System.out.print(TipoSolicitud.opciones());
            try {
                return TipoSolicitud.desdeNumero(leerEntero("Opcion: "));
            } catch (IllegalArgumentException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    private TipoSolicitud leerTipoSolicitudOpcional(TipoSolicitud actual) throws IOException {
        while (true) {
            System.out.println("Tipo de solicitud actual: " + actual.getValorBD());
            System.out.print(TipoSolicitud.opciones());
            System.out.println("0. Conservar actual");
            int opcion = leerEntero("Opcion: ");
            if (opcion == 0) {
                return actual;
            }
            try {
                return TipoSolicitud.desdeNumero(opcion);
            } catch (IllegalArgumentException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    private EstadoSolicitud leerEstadoSolicitud(String titulo) throws IOException {
        while (true) {
            System.out.println(titulo);
            System.out.print(EstadoSolicitud.opciones());
            try {
                return EstadoSolicitud.desdeNumero(leerEntero("Opcion: "));
            } catch (IllegalArgumentException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    private EstadoSolicitud leerEstadoSolicitudOpcional(EstadoSolicitud actual) throws IOException {
        while (true) {
            System.out.println("Estado actual: " + actual.getValorBD());
            System.out.print(EstadoSolicitud.opciones());
            System.out.println("0. Conservar actual");
            int opcion = leerEntero("Opcion: ");
            if (opcion == 0) {
                return actual;
            }
            try {
                return EstadoSolicitud.desdeNumero(opcion);
            } catch (IllegalArgumentException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    private TipoEntrega leerTipoEntrega(String titulo) throws IOException {
        while (true) {
            System.out.println(titulo);
            System.out.print(TipoEntrega.opciones());
            try {
                return TipoEntrega.desdeNumero(leerEntero("Opcion: "));
            } catch (IllegalArgumentException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    private TipoEntrega leerTipoEntregaOpcional(TipoEntrega actual) throws IOException {
        while (true) {
            System.out.println("Tipo de entrega actual: " + actual.getValorBD());
            System.out.print(TipoEntrega.opciones());
            System.out.println("0. Conservar actual");
            int opcion = leerEntero("Opcion: ");
            if (opcion == 0) {
                return actual;
            }
            try {
                return TipoEntrega.desdeNumero(opcion);
            } catch (IllegalArgumentException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    private EvidenciaEntrega leerEvidenciaEntrega(String titulo) throws IOException {
        while (true) {
            System.out.println(titulo);
            System.out.print(EvidenciaEntrega.opciones());
            try {
                return EvidenciaEntrega.desdeNumero(leerEntero("Opcion: "));
            } catch (IllegalArgumentException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    private EvidenciaEntrega leerEvidenciaEntregaOpcional(EvidenciaEntrega actual) throws IOException {
        while (true) {
            System.out.println("Evidencia actual: " + actual.getValorBD());
            System.out.print(EvidenciaEntrega.opciones());
            System.out.println("0. Conservar actual");
            int opcion = leerEntero("Opcion: ");
            if (opcion == 0) {
                return actual;
            }
            try {
                return EvidenciaEntrega.desdeNumero(opcion);
            } catch (IllegalArgumentException err) {
                System.out.println(err.getMessage());
            }
        }
    }
}
