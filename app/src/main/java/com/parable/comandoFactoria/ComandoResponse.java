package com.parable.comandoFactoria;

import com.parable.comando.IDEventos;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public class ComandoResponse {


    private IDEventos evento;
    private Object datos;


    public IDEventos getEvento() {

        return evento;

    }


    public void setEvento(IDEventos evento) {

        this.evento = evento;

    }


    public Object getDatos() {

        return datos;

    }


    public void setDatos(Object datos) {

        this.datos=datos;

    }

}
