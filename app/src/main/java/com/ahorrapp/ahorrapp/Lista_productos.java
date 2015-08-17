package com.ahorrapp.ahorrapp;

public class Lista_productos {
    private String nombre;
    private String precio;
    private String unidad;
    private String id;


    public Lista_productos (String nombre,String precio, String unidad,String id ) {

        this.nombre = nombre;
        this.precio = precio;
        this.unidad = unidad;
        this.id = id;
    }

    public String get_nombre() { return nombre;    }

    public String get_precio() { return precio;  }

    public String get_unidad() { return unidad;  }

    public String get_id() { return id;  }
}
