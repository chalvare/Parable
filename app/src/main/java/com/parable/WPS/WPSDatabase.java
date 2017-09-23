package com.parable.WPS;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Altair on 15/11/15.
 */
public class WPSDatabase {
    public SQLiteDatabase baseDeDatos;
    public final static int NUMERO_RESULTADOS_FINGERPRINT = 10;

    public WPSDatabase() throws SQLException{
        baseDeDatos = SQLiteDatabase.create(null);
        String query = "CREATE TABLE `coordenadas` ("+
                "`MAC` varchar(17) NOT NULL,"+
                "`x` double NOT NULL,"+
                "`y` double NOT NULL,"+
                "`z` double NOT NULL,"+
                "`NIVEL` int(11) NOT NULL,"+
                "PRIMARY KEY (`MAC`,`x`,`y`,`z`))";
        baseDeDatos.execSQL(query);
    }


    /**
     * Inserta las coordenadas en la BD SQLite
     * @param coord
     * @return void
     * @throws org.json.JSONException
     */

    public void insertarCoordenadas(ArrayList<Coordenada> coord)throws JSONException{
        JSONArray dato;
        Coordenada c;
        if(coord.size()!=0) {
            String query = "";
            for (int i = 0; i < coord.size(); i++) {
                //dato = coord.get(i);
                c = coord.get(i);
//                query = "INSERT INTO coordenadas Values('";
//                query = query + dato.getString(0) + "',";
//                query = query + dato.getDouble(1) + ",";
//                query = query + dato.getDouble(2) + ",";
//                query = query + dato.getDouble(3) + ",";
//                query = query + dato.getInt(4) + ");";
                query = "INSERT INTO coordenadas Values('";
                query = query + String.valueOf(c.getMac()) + "',";
                query = query + String.valueOf(c.getX()) + ",";
                query = query + String.valueOf(c.getY()) + ",";
                query = query + String.valueOf(c.getZ()) + ",";
                query = query + String.valueOf(c.getNivel()) + ");";
                baseDeDatos.execSQL(query);

            }

        }
    }

    /**
     * Devuelve todas las coordenadas almacenadas que tienen la MAC
     * del punto que le estamos pasando como parametro
     * @param  punto
     * @return ArrayList
     */

    public ArrayList<Coordenada> getCoordenadasEnPunto(ScanResult punto){
        String[] columnas = new String[4];
        columnas[0]="x";
        columnas[1]="y";
        columnas[2]="z";
        columnas[3]="NIVEL";

        String seleccion = "MAC = ?;";
        String seleccionArgs[] = new String[1];
        seleccionArgs[0]=punto.BSSID;
        //creamos un cursor para devolver todas las MAC y lo posicionamos al inicio
        Cursor cursor = baseDeDatos.query("coordenadas",columnas,seleccion,seleccionArgs,null,null,null);
        cursor.moveToFirst();

        ArrayList<Coordenada> resultado = new ArrayList<Coordenada>();
        Coordenada coord;

        for(int i=0; i< cursor.getCount();i++){
            if(punto.level>-83) {
                coord = new Coordenada();
                coord.setMac(punto.BSSID);
                coord.setX(cursor.getDouble(0));
                coord.setY(cursor.getDouble(1));
                coord.setZ(cursor.getDouble(2));
                coord.setNivel(cursor.getInt(3));
//            Log.e("COORDENADAS EN UN PUNTO", String.valueOf(coord));
//            Log.e("COORDENADAS EN UN PUNTO", String.valueOf(coord.getMac()));
//            Log.e("COORDENADAS EN UN PUNTO",String.valueOf(coord.getX()));
//            Log.e("COORDENADAS EN UN PUNTO",String.valueOf(coord.getY()));
//            Log.e("COORDENADAS EN UN PUNTO",String.valueOf(coord.getZ()));
//            Log.e("COORDENADAS EN UN PUNTO",String.valueOf(coord.getNivel()));
                resultado.add(coord);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return resultado;

    }


    /**
     * Método que recupera los vecinos más cercanos y calcula la posición del terminal
     * la estimaci�n de posici�n del terminal
     * @param macs
     * @return Coordenada
     */
    public Coordenada calcularPosicion(List<ScanResult> macs){
//        Log.e("tamaño de las mac al rastrear",String.valueOf(macs.size()));
//        for(int i = 0; i < macs.size(); i++) {
//            Log.e("tamaño de las mac al rastrear", String.valueOf(macs.get(i).BSSID));
//        }
        ArrayList<Coordenada> coordenadas = this.getClosestNeighbors(macs);
//
//        Log.e("calcularPosicion tamaño de los vecinos",String.valueOf(coordenadas.size()));
//        for(int i = 0; i < coordenadas.size(); i++) {
//            Log.e("tamaño de las mac de los vecinos", String.valueOf(coordenadas.get(i).getMac()));
//            Log.e("tamaño de las mac de los vecinosDiSTANCIA", String.valueOf(coordenadas.get(i).getDistancia()));
//
//        }
        double sumaX = 0;
        double sumaY = 0;
        double aux = 0;
        Coordenada c = new Coordenada();
        if(coordenadas.size()>0){
//            Log.e("ENTRAMOS",String.valueOf(coordenadas.get(0).getMac()));
            for(int i = 0; i < coordenadas.size(); i++){
//                Log.e("MACS de CalcularPosicion",coordenadas.get(i).getMac().toString());
//                Log.e("coordenadas.get(i).getDistancia( VALOR",String.valueOf(coordenadas.get(i).getDistancia()));
//                Log.e("coordenadas.get(i).getX VALOR: ",String.valueOf(coordenadas.get(i).getX()));
                sumaX +=((1/coordenadas.get(i).getDistancia())*coordenadas.get(i).getX());
                sumaY +=((1/coordenadas.get(i).getDistancia())*coordenadas.get(i).getY());
                aux += (1/coordenadas.get(i).getDistancia());
//                Log.e("EL VALOR DE GETDISTANCIAS: ",String.valueOf(coordenadas.get(i).getDistancia()));
//                Log.e("calcularPosicion X",String.valueOf(sumaX));
//                Log.e("calcularPosicion Y",String.valueOf(sumaY));
//                Log.e("calcularPosicion AUX",String.valueOf(aux));
            }
            sumaX = sumaX / aux;
            sumaY = sumaY / aux;
            sumaX = redondear(sumaX);
            sumaY = redondear(sumaY);
        }else{
            sumaX = Double.MAX_VALUE;
            sumaY = Double.MAX_VALUE;
        }
        c.setMac("PRUEBA DE MAC");
        c.setX(sumaX);
        c.setY(sumaY);
        c.setZ(1); //Recuperar Z
        //c.setNivel(187);

        return c;
    }

    /**
     * Devuelve las coordenadas m�s cercanas con la distancia al punto de referencia<br>
     * ya calculada
     * @param macs
     * @return
     */
    private ArrayList<Coordenada> getClosestNeighbors(List<ScanResult> macs){

        ArrayList<ArrayList<Coordenada>> coordenadas = new ArrayList<ArrayList<Coordenada>>();
        /**
         * Guardo las coordenadas de las macs registradas con sus distancias eculideas almacenadas
         */
        ArrayList<Coordenada> arrAux = new ArrayList<Coordenada>();

        for(int i = 0; i < macs.size(); i++){
            coordenadas.add(this.getCoordenadasEnPunto(macs.get(i)));
        }


        double suma = 0;
        ArrayList<Coordenada> result = new ArrayList<Coordenada>();


        String[] columns = new String[3];
        columns[0] = "x";
        columns[1] = "y";
        columns[2] = "z";
        String groupBy = "x,y,z";

        Cursor c = baseDeDatos.query("coordenadas", columns, null,null, groupBy, null, null);

        Coordenada coordActual;
        double distancia;
        double aux;

        c.moveToFirst();
        int j = 0;
        boolean fin = false;
        int h= 0;
        int index=-1;
        while(j < c.getCount() && !fin){
            coordActual = new Coordenada();
            coordActual.setX(c.getDouble(0));
            coordActual.setY(c.getDouble(1));
            coordActual.setZ(c.getDouble(2));
            suma = 0;
            ScanResult l;

            for(int i = 0; i < macs.size(); i++){
                //index = coordenadas.get(i).indexOf(coordActual);
                for(int z=0;z<coordenadas.get(i).size();z++){
                    if( coordenadas.get(i).get(z).getX()==coordActual.getX() &&
                            coordenadas.get(i).get(z).getY()==coordActual.getY()&&
                            coordenadas.get(i).get(z).getZ()==coordActual.getZ()) {
                        //index=z;
                        l = macs.get(i);
                        h = l.level - coordenadas.get(i).get(z).getNivel();
                        suma+= Math.pow(h,2);
                    }
                }

//                if(index!=-1){
//                    l = macs.get(i);
//                    h = l.level - coordenadas.get(i).get(index).getNivel();
//                    suma+= Math.pow(h,2);
//                }
            }
            aux = ((double)1/macs.size());
            distancia = (aux)*Math.sqrt(suma);
            coordActual.setDistancia(distancia);
            arrAux.add(coordActual);
            fin = !c.moveToNext();

        }

        c.close();

        int position;
        double min;
        fin = (arrAux.size()==0);
        while((result.size() < WPSDatabase.NUMERO_RESULTADOS_FINGERPRINT) && !fin){
            position = 0;
            min = Double.MAX_VALUE;
            for(int k = 0; k < arrAux.size(); k++){
                if(min > arrAux.get(k).getDistancia()){
                    min = arrAux.get(k).getDistancia();
                    position = k;
                }
            }
            result.add(arrAux.get(position));
            arrAux.remove(position);
            if(arrAux.size()==0) fin = true;
        }

        return result;
    }

    /**
     * Recupera las 15 coordenadas de la base de datos m�s cercanas a la pasada como par�metro.
     * @param c
     * @return
     */
    public ArrayList<Coordenada> getClosetsCoords(Coordenada c){
        String[] columns = new String[3];
        columns[0] = "x";
        columns[1] = "y";
        columns[2] = "z";
        String selection = "x < ? AND x > ? AND y < ? AND y > ?";
        String selectionArgs[] = new String[4];
        selectionArgs[0] = "" + (c.getX()+5);
        selectionArgs[1] = "" + (c.getX()-5);
        selectionArgs[2] = "" + (c.getY()+5);
        selectionArgs[3] = "" + (c.getY()-5);
        String groupBy = "x,y,z";
        //String groupBy = "x,y,z";
        Cursor cursor = baseDeDatos.query("coordenadas", columns, selection, selectionArgs, groupBy, null, null);

        cursor.moveToFirst();
        ArrayList<Coordenada> result = new ArrayList<Coordenada>();
        //HashMap<Double,Coordenada> result = new HashMap<Double,Coordenada>();
        Coordenada coord;
        double distancia;
        int j = 0;
        Log.e("TAMAÑO del CURSOR wpsdatabase", String.valueOf(cursor.getCount()));
        for(int i = 0; i < cursor.getCount(); i++){
            coord = new Coordenada();
            //coord.setMac(punto.BSSID);
            coord.setX(cursor.getDouble(0));
            coord.setY(cursor.getDouble(1));
            coord.setZ(cursor.getDouble(2));
            Log.e("El valor de x en getClosetsCoords", String.valueOf(cursor.getDouble(0)));
            Log.e("El valor de Y getClosetsCoords", String.valueOf(cursor.getDouble(1)));
            Log.e("El valor de Z getClosetsCoords",String.valueOf(cursor.getDouble(2)));

            distancia = Math.sqrt(Math.pow(coord.getX()-c.getX(),2) + Math.pow(coord.getY()-c.getY(), 2));
            Log.e("La distancia es",String.valueOf(distancia));
            coord.setDistancia(distancia);
            //coord.setStrength(c.getInt(3));
            //result.put(distancia, coord);
            result.add(coord);
            Log.e("el tamaño de result en wpsdatabase", String.valueOf(result.size()));
            j = 0;
//            while(j < result.size()){
//                if(result.get(j).getDistancia()<= coord.getDistancia()){
//                    j++;
//                    Log.e("Entramos en el ", "if de wpsdataabase");
//                }else{
//                    Log.e("Entramos en el ", "else de wpsdataabase");
//                    result.add(j, coord);
//                    if(result.size()>15){
//                        result.remove(result.size()-1);
//                        Log.e("Entramos en el ", "elseif de wpsdataabase");
//                    }
//                }
//            }
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }
    /**
     * Trunca el valor por par�metro de la siguiente manera: si la parte decimal es menor que 0.25 la elimina,
     * si est� entre 0.25 y 0.75 la estima como 0.5 y si es mayor que 0.75 la estima como 1.
     * @param d
     * @return Double
     */
    private double redondear(double d){
        int aux = (int)d;
        double aux2 = d - aux;
        if(aux2<0.25){
            return aux;
        }else if(aux2 >=0.25 && aux2 < 0.5 || aux2>=0.5 && aux2 <= 0.75){
            return aux + 0.5;
        }else return aux + 1;
    }

}

