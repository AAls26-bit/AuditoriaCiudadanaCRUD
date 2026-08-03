package org.example.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

public final class Validaciones {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final Pattern CORREO = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    private Validaciones() {
    }

    public static String textoObligatorio(String valor, String campo, int maximo) {
        String limpio = normalizarEspacios(valor);
        if (limpio.isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        if (limpio.length() > maximo) {
            throw new IllegalArgumentException("El campo " + campo + " no debe pasar de " + maximo + " caracteres.");
        }
        return limpio;
    }

    public static String textoOpcional(String valor, String campo, int maximo) {
        String limpio = normalizarEspacios(valor);
        if (limpio.isEmpty()) {
            return null;
        }
        if (limpio.length() > maximo) {
            throw new IllegalArgumentException("El campo " + campo + " no debe pasar de " + maximo + " caracteres.");
        }
        return limpio;
    }

    public static int enteroPositivo(int valor, String campo) {
        if (valor <= 0) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser mayor a cero.");
        }
        return valor;
    }

    public static int enteroCeroOPositivo(int valor, String campo) {
        if (valor < 0) {
            throw new IllegalArgumentException("El campo " + campo + " no puede ser negativo.");
        }
        return valor;
    }

    public static LocalDate fechaObligatoria(LocalDate fecha, String campo) {
        if (fecha == null) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return fecha;
    }

    public static LocalTime horaObligatoria(LocalTime hora, String campo) {
        if (hora == null) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return hora;
    }

    public static <T> T objetoObligatorio(T valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor;
    }

    public static String correoObligatorio(String correo, String campo, int maximo) {
        String limpio = textoObligatorio(correo, campo, maximo).toLowerCase(Locale.ROOT);
        if (!CORREO.matcher(limpio).matches()) {
            throw new IllegalArgumentException("El correo no tiene un formato valido.");
        }
        return limpio;
    }

    public static String correoOpcional(String correo, String campo, int maximo) {
        String limpio = textoOpcional(correo, campo, maximo);
        if (limpio == null) {
            return null;
        }
        limpio = limpio.toLowerCase(Locale.ROOT);
        if (!CORREO.matcher(limpio).matches()) {
            throw new IllegalArgumentException("El correo no tiene un formato valido.");
        }
        return limpio;
    }

    public static String fechaConFormato(LocalDate fecha) {
        return fecha == null ? "Sin fecha" : fecha.format(FORMATO_FECHA);
    }

    public static String horaConFormato(LocalTime hora) {
        return hora == null ? "Sin hora" : hora.format(FORMATO_HORA);
    }

    public static String capitalizar(String texto) {
        String limpio = normalizarEspacios(texto);
        if (limpio.isEmpty()) {
            return "";
        }

        limpio = limpio.toLowerCase(Locale.ROOT);
        StringBuilder resultado = new StringBuilder();
        boolean siguienteMayuscula = true;

        for (int i = 0; i < limpio.length(); i++) {
            char caracter = limpio.charAt(i);
            if (Character.isLetter(caracter) && siguienteMayuscula) {
                resultado.append(Character.toUpperCase(caracter));
                siguienteMayuscula = false;
            } else {
                resultado.append(caracter);
                siguienteMayuscula = Character.isWhitespace(caracter) || caracter == '-' || caracter == '/';
            }
        }

        return resultado.toString();
    }

    private static String normalizarEspacios(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.trim().replaceAll("\\s+", " ");
    }
}
