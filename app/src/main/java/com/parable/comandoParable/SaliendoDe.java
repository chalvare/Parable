package com.parable.comandoParable;

import com.parable.comando.Comando;
import com.parable.comando.IDEventos;
import com.parable.comandoFactoria.ComandoResponse;
import com.parable.parseoLenguaje.Lenguaje;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public class SaliendoDe implements Comando{

    @Override
    public ComandoResponse ejecutar(Object datos) {

        ComandoResponse cr = new ComandoResponse();
        try {

            cr.setDatos(Lenguaje.saliendoDe((String) datos));
            cr.setEvento(IDEventos.EVENTO_SALIENDO_DE);

        } catch (Exception excep) {

            cr.setDatos(excep);
            cr.setEvento(IDEventos.ERROR_SALIENDO_DE);
        }

        return cr;
    }
}
