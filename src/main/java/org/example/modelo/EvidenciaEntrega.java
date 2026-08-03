package org.example.modelo;

public enum EvidenciaEntrega {
    SELLO_Y_FIRMA("Sello y firma"),
    ACUSE_ELECTRONICO("Acuse electrónico");

    private final String valorBD;

    EvidenciaEntrega(String valorBD) {
        this.valorBD = valorBD;
    }

    public String getValorBD() {
        return valorBD;
    }

    public static EvidenciaEntrega desdeValorBD(String valorBD) {
        for (EvidenciaEntrega evidencia : values()) {
            if (evidencia.valorBD.equalsIgnoreCase(valorBD)) {
                return evidencia;
            }
        }
        throw new IllegalArgumentException("Evidencia de entrega no valida: " + valorBD);
    }

    public static EvidenciaEntrega desdeNumero(int opcion) {
        EvidenciaEntrega[] evidencias = values();
        if (opcion < 1 || opcion > evidencias.length) {
            throw new IllegalArgumentException("Opcion de evidencia no valida.");
        }
        return evidencias[opcion - 1];
    }

    public static String opciones() {
        StringBuilder opciones = new StringBuilder();
        EvidenciaEntrega[] evidencias = values();
        for (int i = 0; i < evidencias.length; i++) {
            opciones.append(i + 1).append(". ").append(evidencias[i].getValorBD()).append(System.lineSeparator());
        }
        return opciones.toString();
    }

    @Override
    public String toString() {
        return valorBD;
    }
}
