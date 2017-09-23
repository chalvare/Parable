package com.parable.comando;

import com.parable.comandoFactoria.ComandoResponse;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public interface Comando {

    public ComandoResponse ejecutar(Object datos);
}
