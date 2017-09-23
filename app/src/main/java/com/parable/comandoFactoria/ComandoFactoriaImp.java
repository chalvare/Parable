package com.parable.comandoFactoria;

import com.parable.comando.Comando;
import com.parable.comando.IDEventos;
import com.parable.comandoParable.AndandoEn;
import com.parable.comandoParable.EntrandoEn;
import com.parable.comandoParable.SaliendoDe;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public class ComandoFactoriaImp extends ComandoFactoria{

    @Override
    public Comando nuevoComando(IDEventos id_comando) {

        Comando comando = null;

        switch(id_comando){

            case EVENTO_ANDANDO_EN:
                comando = new AndandoEn();
                break;
            case EVENTO_ENTRANDO_EN:
                comando = new EntrandoEn();
                break;
            case EVENTO_SALIENDO_DE:
                comando = new SaliendoDe();
                break;

            default:
                break;
        }
        return comando;
    }

}
