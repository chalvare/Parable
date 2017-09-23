package com.parable.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parable.WPS.ThreadDatos;
import com.parable.parablesdb.R;

public class MainActivity extends ActionBarActivity {

    Button bwifi;
    Button blocalizacion;
    Button bacercade;
    Button bvaloracion;
    Button bqr;
    public static Activity activeActivity = null;
    public static ThreadDatos threadDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bwifi = (Button)findViewById(R.id.but_wifi);
        bwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Entramos", "EN EL MAINACTIVITY");
                Intent i = new Intent(MainActivity.this, WPSActivity.class);
                startActivity(i);

            }
        });

        blocalizacion = (Button)findViewById(R.id.but_localizacion);
        blocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Posicion.class);
                startActivity(i);
            }
        });
        if(threadDatos==null){
            threadDatos = creaEIniciaThread(); //Comienza a recuperar los datos de BD
        }

        bqr = (Button)findViewById(R.id.bqr);
        bqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CodigoQR.class);
                startActivity(i);
            }
        });


        bacercade = (Button)findViewById(R.id.but_acercade);
        bacercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarAcercaDe(null);
                //leerArchivo();
            }
        });

        bvaloracion = (Button)findViewById(R.id.but_valoracion);
        bvaloracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Valoracion.class);
                startActivity(i);
            }
        });


    }

    /**
     * Crea un hilo para recuperar los datos de las posiciones de la BD en segundo plano
     * @return
     */
    public ThreadDatos creaEIniciaThread(){
        ThreadDatos t = new ThreadDatos(ThreadDatos.ESTADO_EJECUCION);
        t.start();
        return t;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menu.add(0, 0,0,"Salir").setIcon(android.R.drawable.star_off);
//        menu.add(1, 1,1,"Refrescar lista").setIcon(android.R.drawable.ic_menu_send);
//        MenuItem insertar = menu.add(0,2,0,"Insertar(Server)");
//        insertar.setIcon(android.R.drawable.ic_menu_upload);
//
//            insertar.setEnabled(true);


        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this,Acercade.class);
        startActivity(i);
    }




}
