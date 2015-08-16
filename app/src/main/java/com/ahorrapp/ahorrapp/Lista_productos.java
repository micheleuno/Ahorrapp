package com.ahorrapp.ahorrapp;

public class Lista_productos {
    private String nombre;
    private String precio;
    private String unidad;

    public Lista_productos (String nombre,String precio, String unidad ) {

        this.nombre = nombre;
        this.precio = precio;
        this.unidad = unidad;
    }

    public String get_nombre() { return nombre;    }

    public String get_precio() { return precio;  }

    public String get_unidad() { return unidad;  }
}
