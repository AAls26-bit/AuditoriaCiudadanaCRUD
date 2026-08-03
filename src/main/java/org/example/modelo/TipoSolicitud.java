package org.example.modelo;

public enum TipoSolicitud {
    ESTRUCTURA_ADMINISTRATIVA("Estructura Administrativa"),
    FINANCIERA("Financiera"),
    OBRA_PUBLICA("Obra Publica"),
    PROGRAMAS("Programas"),
    CONTRATOS("Contratos");

    private final String valorBD;

    TipoSolicitud(String valorBD) {
        this.valorBD = valorBD;
    }

    public String getValorBD() {
        return valorBD;
    }

    public static TipoSolicitud desdeValorBD(String valorBD) {
        for (TipoSolicitud tipo : values()) {
            if (tipo.valorBD.equalsIgnoreCase(valorBD)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de solicitud no valido: " + valorBD);
    }

    public static TipoSolicitud desdeNumero(int opcion) {
        TipoSolicitud[] tipos = values();
        if (opcion < 1 || opcion > tipos.length) {
            throw new IllegalArgumentException("Opcion de tipo de solicitud no valida.");
        }
        return tipos[opcion - 1];
    }

    public static String opciones() {
        StringBuilder opciones = new StringBuilder();
        TipoSolicitud[] tipos = values();
        for (int i = 0; i < tipos.length; i++) {
            opciones.append(i + 1).append(". ").append(tipos[i].getValorBD()).append(System.lineSeparator());
        }
        return opciones.toString();
    }

    @Override
    public String toString() {
        return valorBD;
    }
}
