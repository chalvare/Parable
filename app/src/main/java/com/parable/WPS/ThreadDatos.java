package com.parable.WPS;

import com.parable.http.HttpServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Altair on 17/11/15.
 */
public class ThreadDatos extends Thread {

    public static final int ESTADO_EXITO = 0;
    public static final int ESTADO_EJECUCION = 1;
    public static final int ESTADO_ERROR = 2;

    private WPSDatabase db;
    private int estado;


    public ThreadDatos(int estado){
        super("HiloDatos");
        this.estado = estado;
    }

    public void run(){

        ArrayList<Coordenada> listaCoordenadas=new ArrayList<Coordenada>() ;
        Coordenada coordenada;
        listaCoordenadas.clear();

        try{
            db=new WPSDatabase();//creamos una nueva SQLite para recuperar los datos

            HttpServices service = new HttpServices();
            try{
                //JSONArray jsArray = new JSONArray(service.getCoordenadas());//Convertimos el arrayList en JSONArray ERROROROROROORORORO
                JSONArray jsArray = service.getCoordenadas();

                for (int i = 0; i < jsArray.length(); i++) {
                    coordenada=new Coordenada();
                    JSONObject jsonArrayChild = jsArray.getJSONObject(i);
                    coordenada.setMac(jsonArrayChild.optString("MAC"));
                    coordenada.setX(jsonArrayChild.optDouble("X"));
                    coordenada.setY(jsonArrayChild.optDouble("Y"));
                    coordenada.setZ(jsonArrayChild.optDouble("Z"));
                    coordenada.setNivel(jsonArrayChild.optInt("NIVEL"));
                    listaCoordenadas.add(coordenada);
                }

                //db.insertarCoordenadas(jsArray);
                db.insertarCoordenadas(listaCoordenadas);
                estado=ESTADO_EXITO;
            } catch (JSONException e) {
                e.printStackTrace();
                estado = ESTADO_ERROR;
            }
        }catch(SQLException e){
            e.printStackTrace();
            estado = ESTADO_ERROR;
        }
    }

    public WPSDatabase getDatabase() throws WPSException{
        if(db == null){
            throw new WPSException("La base de datos esta sin inicializar");
        }else{
            if(estado == ESTADO_ERROR){
                throw new WPSException("Ha ocurrido un error mientras se intentaba crear la BBDD");
            }else if(estado == ESTADO_EJECUCION){
                throw new WPSException("Se estan recuperando los datos...");
            }
            return db;
        }
    }

    public int getEstado(){
        return estado;
    }


}
