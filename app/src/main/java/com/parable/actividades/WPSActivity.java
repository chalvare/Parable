package com.parable.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parable.WPS.WPS;
import com.parable.WPS.WPSException;
import com.parable.parablesdb.R;

import java.util.List;

/**
 * Created by Altair on 10/11/15.
 */
public class WPSActivity extends ListActivity {

    private WPS wps;
    public static List<ScanResult> resultados;
    public static int elementoSelec;
    public final static int MENU_REFRESH = 1;
    public final static int MENU_INSERT = 2;

    public void onCreate(final Bundle savedInstanceState) {
        MainActivity.activeActivity = this;
        super.onCreate(savedInstanceState);
        cargarDatos();
    }

    protected void onListItemClick (ListView l, View v, int position, long id){
        elementoSelec = position;
        Intent i = new Intent(this,WPSDatosBD.class);
        startActivity(i);
    }

    private void cargarDatos(){
        if(wps==null){
            wps = new WPS(this.getApplicationContext());
        }
        refresh();
    }

    private void refresh(){
        try {
            resultados = wps.scanAndShow();
            String[] valores = new String[resultados.size()];

            for(int i = 0; i < resultados.size(); i++){
                valores[i] = resultados.get(i).SSID + " (" + resultados.get(i).BSSID + ")" +" \nNivel: "+ resultados.get(i).level;
            }
            Log.e("NUMERO DE COORDENDADAS", String.valueOf(resultados.size()));
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_list_item_1, valores);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.elemento_lista,R.id.titulo, valores);

            this.setListAdapter(adapter);

        } catch (WPSException e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(e.getMessage());
            builder.setPositiveButton("Activar", new android.content.DialogInterface.OnClickListener(){

                public void onClick(DialogInterface arg0, int arg1) {
                    WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    manager.setWifiEnabled(true);

                    int i = 0;
                    boolean b = false;
                    while(!b && i < 10){
                        b = manager.isWifiEnabled();
                        i++;
                        try {
                            SystemClock.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(manager.isWifiEnabled()){
                        refresh();
                    }else{
                        DialogController.createInformDialog(getContext(), "Se ha superado el tiempo de espera de activaci�n de la antena WIFI.");
                    }
                }

            });
            builder.setNegativeButton("Salir", new android.content.DialogInterface.OnClickListener(){

                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }

            });
            builder.setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();
        }

    }


//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case MainMenu.MENU_QUIT:
//                finish();
//                return true;
//            case MENU_REFRESH:
//                refresh();
//                return true;
//            case MENU_INSERT:
////                Intent i = new Intent(this,EnviarDatos.class);
////                startActivity(i);
//                return true;
//        }
//        return false;
//    }

    public Activity getContext(){
        return this;
    }



}
