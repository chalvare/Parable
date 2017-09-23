package com.parable.controlador;

import com.parable.comando.Comando;
import com.parable.comando.IDEventos;
import com.parable.comandoFactoria.ComandoFactoria;
import com.parable.comandoFactoria.ComandoResponse;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public class ControladorImp extends Controlador{

    public void handleRequest(IDEventos evento, Object datos) {
        Comando c = ComandoFactoria.getInstancia().nuevoComando(evento);
        ComandoResponse rc = c.ejecutar(datos);
        Dispatcher.getInstancia().redirect(rc);
    }

}
