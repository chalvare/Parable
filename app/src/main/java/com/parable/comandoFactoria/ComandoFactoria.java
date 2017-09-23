package com.parable.comandoFactoria;

import com.parable.comando.Comando;
import com.parable.comando.IDEventos;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public abstract class ComandoFactoria {


    private static ComandoFactoria comandoFactoriaInstancia;


    public static ComandoFactoria getInstancia() {

        crearComandoFactoria();
        return comandoFactoriaInstancia;

    }

    private synchronized static void crearComandoFactoria(){
        if(comandoFactoriaInstancia==null)
            comandoFactoriaInstancia = new ComandoFactoriaImp();

    }

    public abstract Comando nuevoComando(IDEventos id_comando);

}
