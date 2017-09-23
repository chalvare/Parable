package com.parable.controlador;

import com.parable.comandoFactoria.ComandoResponse;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public abstract class Dispatcher {


    private static Dispatcher dispatcherInstancia;

    public abstract void redirect(ComandoResponse respuestacomando);

    private synchronized static void createDispatcher() {
        if (dispatcherInstancia == null)
            dispatcherInstancia = new DispatcherImp();
    }


    public static Dispatcher getInstancia() {
        createDispatcher();
        return dispatcherInstancia;

    }

}
