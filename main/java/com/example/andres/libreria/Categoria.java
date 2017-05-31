package com.example.andres.libreria;

/**
 * Created by andres on 13/may/2017.
 */

public class Categoria {
    private int id;
    private String descripcion;

    public Categoria(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public int getId() {
        return id;
    }


}
