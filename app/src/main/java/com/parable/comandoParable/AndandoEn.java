package com.parable.comandoParable;

import com.parable.comando.Comando;
import com.parable.comando.IDEventos;
import com.parable.comandoFactoria.ComandoResponse;
import com.parable.parseoLenguaje.Lenguaje;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public class AndandoEn implements Comando{


    @Override
    public ComandoResponse ejecutar(Object datos) {


        ComandoResponse cr = new ComandoResponse();
        try {

            cr.setDatos(Lenguaje.andandoEn((String) datos));

            cr.setEvento(IDEventos.EVENTO_ANDANDO_EN);

        } catch (Exception excep) {

            cr.setDatos(excep);
            cr.setEvento(IDEventos.ERROR_ANDANDO_EN);
        }

        return cr;
    }
}
