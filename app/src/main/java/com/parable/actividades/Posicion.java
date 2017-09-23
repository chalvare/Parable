package com.parable.actividades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parable.QR.ListaQR;
import com.parable.WPS.Coordenada;
import com.parable.WPS.ThreadDatos;
import com.parable.WPS.ThreadUbicacion;
import com.parable.WPS.WPSException;
import com.parable.comando.IDEventos;
import com.parable.controlador.Controlador;
import com.parable.grafo.Grafo;
import com.parable.http.HttpServices;
import com.parable.parablesdb.R;
import com.parable.parseoLenguaje.Lenguaje;
import com.parable.socialNetwork.SocialNetwork;
import com.parable.xml.DatosXML;
import com.parable.xml.RssParserSax;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Altair on 17/11/15.
 */



//Crear un array auxiliar para cada historia que tendra la misma dimension que el de listaqr y esta relleno de 0 cuando lea un qr lo primero que hacemos es comprobar que est en listaqr si esta
// devolvemos la posicion y comprobamos que no este a 1 en el array auxiliar (Mostramos un mensaje que diga si ya esta leido)
public class Posicion extends Activity {


    private ThreadUbicacion thread;
    private ProgressDialog pd;
    private Thread comprobar;
    private List<DatosXML> posicion;
    private TextView Planta;
    private TextView Pasillo;
    private TextView Seccion;
    private TextView Lugar;
    private TextView LugarSalir;
    private TextView Puntuacion;
    private String s1 = "";
    private String s2 = "";
    private String s3 = "";
    private static String s4 = "";
    private static String s5 = "";
    private String historiaCompleta = "";
    private static String fraseQR = "";



    private String fraseMovCorrecta="";
    private String fraseMovParado="";
    private String fraseMovIncorrecto="";
    private String decisionCamino ="";
    private String fraseCheckpoint="";



    String mejorCamino="";
    private VideoView videoView;
    private ImageButton btwitter;

    private Button bHistoriaCompleta;
    private ImageButton bParar;
    private Button bleerqr;
    Locale loc;


    private ArrayList<String>checkpoints;
    //private ArrayList<String>caminoAuxiliar;

    private int numPuntos;
    private boolean parado;
    private int contNumParadas;

    TextToSpeech t1;




    private Coordenada coord;
//    private Mapa m;
//    private ListaCuadrantes cuadrantes;

    private CargarXmlTask tarea;


    private Lenguaje leng;


    //QR
    private ListaQR historiaQR;
    private static String fraseBD;
    private HttpServices http;
    private static String fraseComparar;



    //Conectores


    private ArrayList<String> conector;
    private static int contador;


    private boolean acabado;


    public void onCreate(final Bundle savedInstanceState) {
        MainActivity.activeActivity = this;
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.historiatemporal2);
        //AbsoluteLayout main = (AbsoluteLayout) findViewById(R.id.temporalLayout);
        Planta = (TextView) this.findViewById(R.id.Planta);
        Pasillo = (TextView) this.findViewById(R.id.Pasillo);
        Seccion = (TextView) this.findViewById(R.id.Seccion);
        //Lugar = (TextView) this.findViewById(R.id.LugarSalir);





        contador=0;

        loc = new Locale ("spa", "ESP");
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(loc);
                }
            }
        });


        LugarSalir = (TextView) this.findViewById(R.id.LugarSalir);
        leng = new Lenguaje();
        String inicio = leng.inicio();
        LugarSalir.append(inicio + "\n");


        Puntuacion = (TextView) this.findViewById(R.id.puntuacion);


        fraseMovCorrecta="Has elegido el camino correcto, la historia sigue su curso.";
        fraseMovIncorrecto="Has decidido tomar otro camino, deberas hacernos caso si quieres llegar al final.";
        fraseMovParado="Sigues en el mismo lugar sin moverte. Deberias continuar."  + "\n";
        fraseCheckpoint="Felicidades! Acabas de pasar por el checkpoint.";
        fraseQR="";


        videoView = (VideoView) findViewById(R.id.videoView1);
        btwitter = (ImageButton) findViewById(R.id.btwitter);

        bHistoriaCompleta = (Button) findViewById(R.id.bHistoriaCompleta);
        bParar = (ImageButton) findViewById(R.id.bParar);
        bParar.setImageResource(R.drawable.pause);
        bleerqr = (Button) findViewById(R.id.bleerqr);

        checkpoints = Grafo.cargarCheckpoints();
        //caminoAuxiliar = Grafo.cargarCaminoaux();

        posicion = null;
        coord = new Coordenada();

        numPuntos=0;
        contNumParadas=0;
        parado = false;




        //////QR///////////

        historiaQR = new ListaQR();
        fraseBD="";
        fraseComparar="";


        //historiaQR.QRbasicos();
        //historiaQR.encriptarArray();

        //////////////



        acabado=false;



        //cuadrantes = thread.getCuadrantes();
        // cuadrantes = new ListaCuadrantes();
//        m = new Mapa(this);
//        m.setListaCuadrantes(cuadrantes);
//        main.addView(m);
        pd = ProgressDialog.show(this, "", "Cargando datos...");
        comprobar = new Thread() {
            public void run() {
                try {
                    comprobar();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (WPSException e) {
                    e.printStackTrace();
                }
            }
        };
        comprobar.start();


    }


    private void comprobar() throws SQLException, WPSException {
        if (MainActivity.threadDatos.getEstado() == ThreadDatos.ESTADO_ERROR) {
            pd.dismiss();
            // DialogController.createInformDialog(this, "Ha ocurido un error mientras se recuperaban los datos.");
        } else if (MainActivity.threadDatos.getEstado() == ThreadDatos.ESTADO_EJECUCION) {
            SystemClock.sleep(2000);
            comprobar();
        } else if (MainActivity.threadDatos.getEstado() == ThreadDatos.ESTADO_EXITO) {
            //Mapa m = new Mapa(this);
            pd.dismiss();
            //setContentView(m);
            if (thread == null) {
                thread = new ThreadUbicacion(this);
                // thread.setCuadrantes(cuadrantes);
                thread.start();
            }
        }
    }




    public void actualizarVentanaAndandoEn(IDEventos idEvento, Object datos) {

        if (IDEventos.EVENTO_ANDANDO_EN == idEvento) {

            if (datos instanceof String) {
                String frase = (String) datos;

                if (frase != null) {
                    s4 = frase + "\n";

                } else {

                    s4 = "Error";
                }
            }
        } else if (IDEventos.ERROR_ANDANDO_EN == idEvento) {

            if (datos instanceof Exception) {

            } else {

            }
        }
    }

    public void actualizarVentanaEntrandoEn(IDEventos idEvento, Object datos) {

        conector=conectores();
        if (IDEventos.EVENTO_ENTRANDO_EN == idEvento) {

            if (datos instanceof String) {
                String frase = (String) datos;

                if (frase != null) {
                    if(contador<conector.size()){
                        s4 = conector.get(contador) + frase;
                    }else{
                        contador=0;
                        s4 = conector.get(contador) + frase;
                    }
                    contador++;

                    //s4 = frase;
                } else {

                    s4 = "Error";
                }
            }
        } else if (IDEventos.ERROR_ENTRANDO_EN == idEvento) {

            if (datos instanceof Exception) {

            } else {

            }
        }
    }

    public void actualizarVentanaSaliendoDe(IDEventos idEvento, Object datos) {

        if (IDEventos.EVENTO_SALIENDO_DE == idEvento) {

            if (datos instanceof String) {
                String frase = (String) datos;

                if (frase != null) {
                    s5 = frase;

                } else {

                    s5 = "Error";
                }
            }
        } else if (IDEventos.EVENTO_SALIENDO_DE == idEvento) {

            if (datos instanceof Exception) {

            } else {

            }
        }
    }


    public void cargarDatosXML(Coordenada coord) {

        tarea = new CargarXmlTask();
        //tarea.execute("http://147.96.110.51/parable/mapa.xml");
        this.coord = coord;
        tarea.execute("http://tot.fdi.ucm.es/parable/mapa.xml");

    }

    public class CargarXmlTask extends AsyncTask<String, Integer, Boolean> {



        protected Boolean doInBackground(String... params) {


            RssParserSax saxparser = new RssParserSax(params[0]);


            posicion = saxparser.parse(coord);

            return true;
        }

        protected void onPostExecute(Boolean result) {

            Grafo grafoPlanta1 = new Grafo("Planta1");

            Puntuacion.setText("Puntuación: " + numPuntos);



            if(!acabado) {

                if (posicion.get(0).getPoi().equalsIgnoreCase(" 1 ")) {

                    if (s1.equalsIgnoreCase(posicion.get(0).getNombrePlanta()) &&
                            s2.equalsIgnoreCase(posicion.get(0).getNombrePasillo()) &&
                            s3.equalsIgnoreCase(posicion.get(0).getNombreSeccion())) {

                        s1 = posicion.get(0).getNombrePlanta();
                        s2 = posicion.get(0).getNombrePasillo();
                        s3 = posicion.get(0).getNombreSeccion();
                        //Controlador.getInstancia().handleRequest(IDEventos.EVENTO_ANDANDO_EN, s3);

                        if (contNumParadas == 2) {
                            s4 = fraseMovParado;
                            contNumParadas = 0;
                        } else {
                            contNumParadas++;
                            s4 = "";
                        }

                    } else {

                        s1 = posicion.get(0).getNombrePlanta();
                        s2 = posicion.get(0).getNombrePasillo();
                        s3 = posicion.get(0).getNombreSeccion();
                        Controlador.getInstancia().handleRequest(IDEventos.EVENTO_ENTRANDO_EN, s3);


                        if (mejorCamino != null) {
                            //if (caminoAuxiliar.get(0).equalsIgnoreCase(mejorCamino)) {
                            if (s3.equalsIgnoreCase(mejorCamino)) {
                                Log.e("Estas yendo por: ", s3);
                                Log.e("SIIIIIII me has hecho", "caso");


                                //Checkpoint
                                //if (caminoAuxiliar.get(0).equalsIgnoreCase(checkpoints.get(0))) {
                                if (s3.equalsIgnoreCase(checkpoints.get(0))) {
                                    if (checkpoints.size() == 1) {
                                        acabado = true;
                                    }
                                    Log.e("CHECKPOINT!!!", "!!!");
                                    s4 += fraseCheckpoint;
                                    checkpoints.remove(0);
                                } else {
                                    Log.e("No hemos llegado", "AL CHECK");
                                    s4 += fraseMovCorrecta;
                                }

                            } else {
                                Log.e("NOOOOOOOO me has hecho", "caso");
                                s4 += fraseMovIncorrecto;
                            }
                        } else {
                            Log.e("No has", " andado");
                        }

                        if (checkpoints.size() != 0) {
                            //mejorCamino = grafoPlanta1.camino(grafoPlanta1.getGrafo(), caminoAuxiliar.get(0), checkpoints.get(0));
                            mejorCamino = grafoPlanta1.camino(grafoPlanta1.getGrafo(), s3, checkpoints.get(0));
                            Log.e("El mejor camino:", mejorCamino);
                            decisionCamino = " Deberias ir a: " + mejorCamino + "\n";
                            s4 += decisionCamino + "\n";
                        }
                        //caminoAuxiliar.remove(0);


                        //videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.quieto));

                        //videoView.start();

                    }

                } else {
                    //s4="Neeeeein"+posicion.get(0).getPoi();
                    s1 = posicion.get(0).getNombrePlanta();
                    s2 = posicion.get(0).getNombrePasillo();
                    s3 = posicion.get(0).getNombreSeccion();

                    if (mejorCamino != null) {
                        //if (caminoAuxiliar.get(0).equalsIgnoreCase(mejorCamino)) {
                        if (s3.equalsIgnoreCase(mejorCamino)) {
                            //Log.e("Estas yendo por: ", caminoAuxiliar.get(0).toString());
                            Log.e("Estas yendo por: ", s3);
                            Log.e("SIIIIIII me has hecho", "caso");

                            s4 += fraseMovCorrecta;

                            //Checkpoint
                            //if (caminoAuxiliar.get(0).equalsIgnoreCase(checkpoints.get(0))) {
                            if (s3.equalsIgnoreCase(checkpoints.get(0))) {
                                Log.e("CHECKPOINT!!!", "!!!");
                                if (checkpoints.size() == 1) {
                                    acabado = true;
                                }

                                s4 += fraseCheckpoint;
                                checkpoints.remove(0);
                            } else {
                                Log.e("No hemos llegado", "AL CHECK");
                                s4 += fraseMovCorrecta;
                            }

                        } else {
                            Log.e("NOOOOOOOO me has hecho", "caso");
                            s4 += fraseMovIncorrecto;
                        }
                    } else {
                        Log.e("No has", " andado");
                    }

                    if (checkpoints.size() != 0) {
                        //mejorCamino = grafoPlanta1.camino(grafoPlanta1.getGrafo(), caminoAuxiliar.get(0), checkpoints.get(0));
                        mejorCamino = grafoPlanta1.camino(grafoPlanta1.getGrafo(), s3, checkpoints.get(0));
                        Log.e("El mejor camino:", mejorCamino);
                        decisionCamino = " Deberias ir a: " + mejorCamino + "\n";
                        s4 += decisionCamino + "\n";
                    }
                    //caminoAuxiliar.remove(0);


                }

                historiaCompleta += s4;
                LugarSalir.setMovementMethod(new ScrollingMovementMethod());
                LugarSalir.append(s4 + "\n");
                String[] fraseLeer = s4.split("\\.");
                t1.speak(fraseLeer[0], TextToSpeech.QUEUE_FLUSH, null);

            }else{
                String fin = leng.fin();
                historiaCompleta+="\n" + fin;

                LugarSalir.setMovementMethod(new ScrollingMovementMethod());
                LugarSalir.append(fin + "\n");
                t1.speak(fin, TextToSpeech.QUEUE_FLUSH, null);
                thread.setEstado(0);
                tarea.cancel(true);


            }



            //Punto de interes.










            Log.e("S4Final", historiaCompleta);


//            Planta.setText(s1);
//            Pasillo.setText(s2);
//            Seccion.setText(s3);
//            Lugar.setText(s4);
            //LugarSalir.setText(s5);
            //s5="";


            /*Log.e("QRModificado1", historiaQR.getListadoQREnc().get(0));
            Log.e("QRModificado2", historiaQR.getListadoQREnc().get(1));
            Log.e("QRModificado3", historiaQR.getListadoQREnc().get(2));
            Log.e("QRModificado4", historiaQR.getListadoQREnc().get(3));
            Log.e("QRModificado5", historiaQR.getListadoQREnc().get(4));*/

            /*ListaQR ejemploQR = new ListaQR();

            String qrEncriptado= historiaQR.encriptar("Hola");
            String qrDesencriptado= historiaQR.desencriptar(qrEncriptado);

            Log.e("QRModificado", qrEncriptado);
            Log.e("QRNormal", qrDesencriptado);*/

            Log.e("NombrePlanta", posicion.get(0).getNombrePlanta());
            Log.e("NombrePasillo", posicion.get(0).getNombrePasillo());
            Log.e("NombreSeccion", posicion.get(0).getNombreSeccion());
            Log.e("NombrePoi", posicion.get(0).getPoi());
            //Log.e("NombreAula", posicion.get(0).getNombreAula());
            Log.e("ValoriniX", String.valueOf(posicion.get(0).getInix()));
            Log.e("ValorfinX", String.valueOf(posicion.get(0).getFinx()));
            Log.e("ValoriniY", String.valueOf(posicion.get(0).getIniy()));
            Log.e("ValorfinY", String.valueOf(posicion.get(0).getFiny()));



            /**************************************************************************/
            /****************************SOCIAL NETWORK********************************/
            /**************************************************************************/

            btwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SocialNetwork social = new SocialNetwork(fraseQR);
                    startActivity(new Intent(Intent.ACTION_VIEW, social.twitter()));

                }
            });


            bHistoriaCompleta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    thread.setEstado(0);
                    tarea.cancel(true);

                    http = new HttpServices();
                    new modificarLeido(Posicion.this).execute();


                    Intent intent = new Intent(MainActivity.activeActivity, HistoriaCompleta.class);
                    String puntuacion = "Puntuacion: " + numPuntos;
                    intent.putExtra("puntos", puntuacion);
                    intent.putExtra("historia", historiaCompleta);

                    startActivity(intent);


                }
            });



            bleerqr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thread.setEstado(0);
                    tarea.cancel(true);
                    IntentIntegrator integrator = new IntentIntegrator(Posicion.this);
                    integrator.initiateScan();
                    parado=true;

                }


            });


            bParar.setOnClickListener(new View.OnClickListener() {

                //ArrayList<String> aux = caminoAuxiliar;

                @Override
                public void onClick(View v) {
                    if (thread.getEstado() == 1) {
                        thread.setEstado(0);
                        tarea.cancel(true);
                        bParar.setImageResource(R.drawable.play);
                    } else {
                        bParar.setImageResource(R.drawable.pause);
                        thread.setEstado(1);
                        tarea = new CargarXmlTask();
                        //this.coord=coord;
                        tarea.execute("http://tot.fdi.ucm.es/parable/mapa.xml");
                        //caminoAuxiliar=aux;
                    }

                }
            });


        }
    }


    //QR done se coge el codigo que te devuelve y lo imprime
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        int puntos =0;
        //ArrayList<String> aux = caminoAuxiliar;


        if (scanResult != null) {
            String re = scanResult.getContents();

           // String prub = historiaQR.encriptar(re);
            //int encontrado = historiaQR.comprobarQR(prub);

            //historiaQR.anadirQR("¿Te paraste a mirar por la ventana?");


            //boolean acabado = historiaQR.comprobarQR(re);
            //Log.e("LA FRASE ES:", "" +   acabado);
            fraseComparar=re;


            http = new HttpServices();
            new getQR(Posicion.this).execute();


            try {
                thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Log.e("El valor de encontrado:", "" + isEncontrado());


            Log.e("LA FRASE Final :", fraseBD);

            //Incorrecto
            if(fraseBD.equalsIgnoreCase("nada\n")){
                Log.e("NONONONONONON", "La cagaste");
                historiaCompleta +=  "\n"+ "El QR introducido no existe o ya ha sido leido." + "\n";

            }else{ //Correcto
                Log.e("BIEEEEEN", "EEEEEXISTE");
                fraseQR=re;
                historiaCompleta +=  "\n"+ fraseQR + ". Has conseguido 10 puntos!" + "\n";
                numPuntos += 10;
                Puntuacion.setText("Puntuación conseguida: " + numPuntos);

            }

            /*if(acabado)
            {
                Log.e("BIEEEEEN", "EEEEEXISTE");
                /*historiaCompleta +=  "\n"+ re + ". Has conseguido 10 puntos!" + "\n";
                numPuntos+=10;
                Puntuacion.setText("Puntuación conseguida: " + numPuntos);
            }
            else{
                Log.e("NONONONONONON", "La cagaste");
                //historiaCompleta +=  "\n"+ "El QR introducido ya ha sido leido" + "\n";
            }*/


            /*if(encontrado!=-1)
            {
                if(historiaQR.getListadoLeidos().get(encontrado)==0){
                    fraseQR=historiaQR.recuperarQR(encontrado);
                    historiaCompleta +=  "\n"+ fraseQR + ". Has conseguido 10 puntos!" + "\n";
                    numPuntos+=10;
                    Puntuacion.setText("Puntuación conseguida: " + numPuntos);
                    historiaQR.getListadoLeidos().set(encontrado,1);

                }else{
                    historiaCompleta +=  "\n"+ "El QR introducido ya ha sido leido" + "\n";
                }

            }else{

                historiaCompleta +=  "\n"+ "El QR introducido no es válido" + "\n";
            }*/






            /*try{
                puntos= Integer.parseInt(lecturaqr);
                numPuntos+=puntos;
                historiaCompleta += "Has conseguido " + puntos + " sigue así!." + "\n";
                Puntuacion.setText("Puntuación conseguida: " + numPuntos);
            }catch (NumberFormatException e){
                historiaCompleta += "El QR leido no contiene ningun punto." + "\n";
            }*/

            /*if(re.equalsIgnoreCase("Has conseguido 10 puntos")){
              numPuntos+=10;

            }*/



            //Log.d("code", re);

//            String [] loc= re.split(",",3);
//            String res = loc[0]+" 1 "+loc[1]+" 2 "+loc[2]+" 3";
//            historiaCompleta+=res;
            //historiaCompleta += re + "\n";
            //historiaCompleta += ""+numPuntos + "\n";
            Log.e("HISTORIAAAA", historiaCompleta);
            LugarSalir.setText(historiaCompleta + "\n");

            /*
            try {
                thread = new ThreadUbicacion(this);
                thread.setEstado(1);
                tarea = new CargarXmlTask();
                //this.coord=coord;
                tarea.execute("http://tot.fdi.ucm.es/parable/mapa.xml");

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (WPSException e) {
                e.printStackTrace();
            }*/

            thread.setEstado(1);
            tarea = new CargarXmlTask();
            //this.coord=coord;
            tarea.execute("http://tot.fdi.ucm.es/parable/mapa.xml");
            //caminoAuxiliar=aux;

        }
        // else continue with any other code you need in the method

    }


    class getQR extends AsyncTask<String,String,String> {

        private Activity context;

        getQR(Activity context) {
            this.context = context;
        }



        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            Log.e("ENTROOOENGET","asdasd");

            /*if (http.recuperarQR())//cargamos en la base de datos
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
                });*/


            fraseBD=http.recuperarQR(fraseComparar);
            Log.e("LA FRASE ES:", fraseBD);


            return null;
        }




    }


    class modificarLeido extends AsyncTask<String,String,String> {

        private Activity context;

        modificarLeido(Activity context) {
            this.context = context;
        }



        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub



            if (http.modificarLeido())//cargamos en la base de datos
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

    public ArrayList<String> conectores(){


        /////Conectores
        conector = new ArrayList<String>();


        conector.add("A continuación ");
        conector.add("Ahora ");
        conector.add("Después ");
        conector.add("Ademas ");
        conector.add("Entonces ");
        return conector;
    }










}
