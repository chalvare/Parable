package com.parable.controlador;

import com.parable.comando.IDEventos;
import com.parable.comandoFactoria.ComandoResponse;
import com.parable.parableGUI.ParableGUI;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public class DispatcherImp extends Dispatcher{



    public void redirect(ComandoResponse respuestacomando) {

        IDEventos eventoActual = respuestacomando.getEvento();
        Object datos = respuestacomando.getDatos();

        if (eventoActual == IDEventos.EVENTO_ENTRANDO_EN
                || eventoActual == IDEventos.EVENTO_SALIENDO_DE
                || eventoActual == IDEventos.EVENTO_ANDANDO_EN
                || eventoActual == IDEventos.ERROR_ENTRANDO_EN
                || eventoActual == IDEventos.ERROR_SALIENDO_DE
                || eventoActual == IDEventos.ERROR_ANDANDO_EN) {

            ParableGUI.getInstancia().update(eventoActual, datos);
        }
    }

}
