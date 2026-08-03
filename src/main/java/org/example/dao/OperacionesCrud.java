package org.example.dao;

import java.util.ArrayList;

public interface OperacionesCrud<T, K> {
    boolean agregar(T entidad);

    boolean editar(T entidad);

    boolean eliminar(K id);

    ArrayList<T> listar();

    T buscarPorId(K id);

    ArrayList<T> filtrar(String texto);
}
