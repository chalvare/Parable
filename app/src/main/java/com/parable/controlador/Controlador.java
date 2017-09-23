package com.parable.controlador;

import com.parable.comando.IDEventos;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public abstract class Controlador {

    private static Controlador controladorAplicacionInstancia;


    public static Controlador getInstancia() {
        createApplicationController();
        return controladorAplicacionInstancia;
    }

    private synchronized static void createApplicationController() {
        if (controladorAplicacionInstancia == null)
            controladorAplicacionInstancia = new ControladorImp();
    }


    public abstract void handleRequest(IDEventos evento, Object datos);

}
