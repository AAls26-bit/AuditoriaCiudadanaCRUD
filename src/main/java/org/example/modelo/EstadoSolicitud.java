package org.example.modelo;

public enum EstadoSolicitud {
    EN_PROCESO("En proceso"),
    RESPONDIDA("Respondida"),
    SIN_RESPUESTA("Sin respuesta"),
    AMPARO("Amparo"),
    FINALIZADA("Finalizada");

    private final String valorBD;

    EstadoSolicitud(String valorBD) {
        this.valorBD = valorBD;
    }

    public String getValorBD() {
        return valorBD;
    }

    public static EstadoSolicitud desdeValorBD(String valorBD) {
        for (EstadoSolicitud estado : values()) {
            if (estado.valorBD.equalsIgnoreCase(valorBD)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de solicitud no valido: " + valorBD);
    }

    public static EstadoSolicitud desdeNumero(int opcion) {
        EstadoSolicitud[] estados = values();
        if (opcion < 1 || opcion > estados.length) {
            throw new IllegalArgumentException("Opcion de estado no valida.");
        }
        return estados[opcion - 1];
    }

    public static String opciones() {
        StringBuilder opciones = new StringBuilder();
        EstadoSolicitud[] estados = values();
        for (int i = 0; i < estados.length; i++) {
            opciones.append(i + 1).append(". ").append(estados[i].getValorBD()).append(System.lineSeparator());
        }
        return opciones.toString();
    }

    @Override
    public String toString() {
        return valorBD;
    }
}
