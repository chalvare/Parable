package com.parable.comandoParable;

import com.parable.comando.Comando;
import com.parable.comando.IDEventos;
import com.parable.comandoFactoria.ComandoResponse;
import com.parable.parseoLenguaje.Lenguaje;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public class EntrandoEn implements Comando {

    @Override
    public ComandoResponse ejecutar(Object datos) {

        ComandoResponse cr = new ComandoResponse();
        try {

            cr.setDatos(Lenguaje.entrandoEn((String) datos));
            cr.setEvento(IDEventos.EVENTO_ENTRANDO_EN);

        } catch (Exception excep) {

            cr.setDatos(excep);
            cr.setEvento(IDEventos.ERROR_ENTRANDO_EN);
        }

        return cr;
    }
}
