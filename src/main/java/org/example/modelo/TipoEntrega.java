package org.example.modelo;

public enum TipoEntrega {
    DIGITAL("Digital"),
    IMPRESA("Impresa");

    private final String valorBD;

    TipoEntrega(String valorBD) {
        this.valorBD = valorBD;
    }

    public String getValorBD() {
        return valorBD;
    }

    public static TipoEntrega desdeValorBD(String valorBD) {
        for (TipoEntrega tipo : values()) {
            if (tipo.valorBD.equalsIgnoreCase(valorBD)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de entrega no valido: " + valorBD);
    }

    public static TipoEntrega desdeNumero(int opcion) {
        TipoEntrega[] tipos = values();
        if (opcion < 1 || opcion > tipos.length) {
            throw new IllegalArgumentException("Opcion de tipo de entrega no valida.");
        }
        return tipos[opcion - 1];
    }

    public static String opciones() {
        StringBuilder opciones = new StringBuilder();
        TipoEntrega[] tipos = values();
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
