package com.parable.actividades;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parable.WPS.Coordenada;
import com.parable.http.HttpServices;
import com.parable.parablesdb.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Altair on 10/11/15.
 */
public class WPSDatosBD extends ActionBarActivity {

    private EditText xEdit;
    private EditText yEdit;
    private EditText zEdit;
    private Button enviarBot;
    private Button mostrar;
    private HttpServices services;
    private List<ScanResult> resultados;
    private ArrayList<Coordenada> listaCoordenadas;
    private Coordenada coordenada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.activeActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enviar_datos);


        listaCoordenadas = new ArrayList<Coordenada>();
        xEdit = (EditText)findViewById(R.id.xEdit);
        yEdit=(EditText)findViewById(R.id.yEdit);
        zEdit=(EditText)findViewById(R.id.zEdit);
        enviarBot=(Button)findViewById(R.id.enviarBot);


        services = new HttpServices();
        resultados = WPSActivity.resultados;
        Log.e("TAMAÑO DE RESULTADOS: ",String.valueOf(resultados.size()));
        enviarBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //envia a la base de datos cuando pulsamos enviar.
                Toast.makeText(MainActivity.activeActivity, "Coordenada Insertada", Toast.LENGTH_LONG).show();
                new InsertarClase(WPSDatosBD.this).execute();

            }
        });

//        mostrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "boton", Toast.LENGTH_LONG).show();
//                //envia a la base de datos cuando pulsamos enviar.
//                new Mostrar().execute();
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }



//    private boolean filtrarDatos(){
//        listaCoordenadas.clear();
//        String data=services.recuperarCoordenadasEnString();
//        if(!data.equalsIgnoreCase("")){
//            JSONObject json;
//            try {
//                json = new JSONObject(data);
//                JSONArray jsonArray = json.optJSONArray("coordenadas");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    coordenada=new Coordenada();
//                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
//                    coordenada.setMac(jsonArrayChild.optString("MAC"));
//                    coordenada.setX(jsonArrayChild.optDouble("X"));
//                    coordenada.setY(jsonArrayChild.optDouble("Y"));
//                    coordenada.setZ(jsonArrayChild.optDouble("Z"));
//                    coordenada.setNivel(jsonArrayChild.optInt("NIVEL"));
//                    listaCoordenadas.add(coordenada);
//                }
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            return true;
//        }
//        return false;
//    }

    /**
     * Clase privada que utilizamos para crear un hilo y cargar los datos en la BD
     */
    class InsertarClase extends AsyncTask<String,String,String> {

        private Activity context;

        InsertarClase(Activity context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            for (int i=0; i < resultados.size();i++) {

                    if (services.insertarBD(resultados.get(i).BSSID, xEdit, yEdit, zEdit, resultados.get(i).level))//cargamos en la base de datos
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                //Toast.makeText(context, "dato insertado con Èxito", Toast.LENGTH_LONG).show();
                                //numX.setText("");
                            }
                        });
                    else
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                //Toast.makeText(context, "dato no insertado", Toast.LENGTH_LONG).show();
                            }
                        });
                }

            return null;
        }
    }

//    class Mostrar extends AsyncTask<String,String,String>{
//        @Override
//        protected String doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            //Toast.makeText(getApplicationContext(), "Mostrar", Toast.LENGTH_LONG).show();
//            if(filtrarDatos()){
//                Log.e("ANTES DE ","MOSTRARPERSONA");
//                mostrarPersona(posicion);
//            }
//            return null;
//        }
//    }
//    private void mostrarPersona(final int posicion){
//        runOnUiThread(new Runnable(){
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "MostrarPersona", Toast.LENGTH_LONG).show();
//                Log.e("TAMAÑO ", String.valueOf(listaCoordenadas.size()));
//
//                Coordenada personas=listaCoordenadas.get(posicion);
//                Log.e("X ", String.valueOf(personas.getX()));
//                Log.e("Y ", String.valueOf(personas.getY()));
//                Log.e("Z ", String.valueOf(personas.getZ()));
//                xEdit.setText(String.valueOf(personas.getX()));
//                yEdit.setText(String.valueOf(personas.getY()));
//                zEdit.setText(String.valueOf(personas.getZ()));
//
//            }
//        });
//    }

}
