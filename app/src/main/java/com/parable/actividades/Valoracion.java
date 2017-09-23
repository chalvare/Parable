package com.parable.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parable.http.HttpServices;
import com.parable.parablesdb.R;

/**
 * Created by Altair on 18/2/16.
 */
public class Valoracion extends ActionBarActivity {



    private EditText resena;
    private EditText nombre;
    private RatingBar valoracion;
    private Button enviar_valoracion;
    private  HttpServices http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.activeActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valoracion);
        resena = (EditText) findViewById(R.id.resena);
        nombre = (EditText) findViewById(R.id.nombre);
        valoracion = (RatingBar) findViewById(R.id.valoracion);
        enviar_valoracion = (Button) findViewById(R.id.enviar_valoracion);



        enviar_valoracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nombre.getText().toString().trim().equalsIgnoreCase("") /*&& !nombreWeb.getText().toString().trim().equalsIgnoreCase("Nombre")*/) {
                    http = new HttpServices();
                    new InsertarValoracion(Valoracion.this).execute();
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.activeActivity).create();
                    alertDialog.setTitle("Enviado!");
                    alertDialog.setMessage("Tu reseña ha sido guardada en nuestra base de datos. Puedes volver a leerla en: www.parableTFG.es");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Volver",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    //AÑADIR PARA QUE PUEDAS VER TODAS LAS RESEÑAS EN LA WEB!
                    /*alertDialog.setButton("Ir a la web", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

    //                            Uri url = Uri.parse("http://tot.fdi.ucm.es/parable/web/tuhistoria.php");
    //                            Intent intent = new Intent(Intent.ACTION_VIEW, url);
    //                            startActivity(intent);

                            WebView webview = new WebView(MainActivity.activeActivity);
                            setContentView(webview);
                            WebSettings webSettings = webview.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            webview.setWebViewClient(new WebViewClient());
                            String postData = "nombre=" + nombreWeb.getText().toString().trim() + "&submit=Enviar";
                            webview.postUrl("http://tot.fdi.ucm.es/parable/parableWeb/tuhistoria.php", EncodingUtils.getBytes(postData, "BASE64"));

    //                            byte[] post = EncodingUtils.getBytes("nombre=Christian&submit=Enviar", "BASE64");
    //                            webview.postUrl("http://localhost/parableWeb/tuhistoria.php", post);
                        }
                    });*/
                    alertDialog.show();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.activeActivity).create();
                    alertDialog.setTitle("Cuidado!");
                    alertDialog.setMessage("Introduce tu nombre");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    Toast.makeText(MainActivity.activeActivity, "vacio", Toast.LENGTH_LONG).show();
                }

            }
        });





    }


    class InsertarValoracion extends AsyncTask<String,String,String> {

        private Activity context;

        InsertarValoracion(Activity context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub



            if (http.enviarValoracion(nombre,resena, valoracion))//cargamos en la base de datos
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


            return null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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
}
