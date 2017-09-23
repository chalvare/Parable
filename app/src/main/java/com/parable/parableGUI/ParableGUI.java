package com.parable.parableGUI;

import com.parable.comando.IDEventos;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public abstract class ParableGUI {

    private static ParableGUI ParableGUIInstancia;

    public abstract void update(IDEventos evento_actual, Object datos);

    public static ParableGUI getInstancia() {

        createParableGUI();

        return ParableGUIInstancia;
    }

    private static synchronized void createParableGUI() {

        if (ParableGUIInstancia == null) {
            ParableGUIInstancia = new ParableGUIImp();
        }
    }

    //public abstract Posicion getPanelParable();

}
