package com.parable.parableGUI;

import com.parable.actividades.Posicion;
import com.parable.comando.IDEventos;

/**
 * Created by ReplacedKevin on 23/12/15.
 */
public class ParableGUIImp extends ParableGUI {

    /*private static Posicion panelParable;

    public Posicion getPanelParable()
    {
        if(panelParable == null)
            panelParable = new Posicion();

        return panelParable;
    }*/


    public void update(IDEventos evento_actual, Object datos) {

        Posicion aux = new Posicion();
        switch (evento_actual) {

            case EVENTO_ENTRANDO_EN:
            case ERROR_ENTRANDO_EN:
                aux.actualizarVentanaEntrandoEn(evento_actual, datos);
                break;


            case EVENTO_ANDANDO_EN:
            case ERROR_ANDANDO_EN:
                aux.actualizarVentanaAndandoEn(evento_actual, datos);
                break;

            case EVENTO_SALIENDO_DE:
            case ERROR_SALIENDO_DE:
                aux.actualizarVentanaSaliendoDe(evento_actual, datos);
                break;

            default:
                break;

        }
    }

}
