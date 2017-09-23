package com.parable.QR;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;

import com.parable.http.HttpServices;

import java.util.ArrayList;

/**
 * Created by ReplacedKevin on 24/5/16.
 */
public class ListaQR extends ActionBarActivity {



    private ArrayList<String> listadoQREnc;
    private ArrayList<String> listadoQRDesenc;
    private ArrayList<Integer> listadoLeidos;
    private String textoEncriptado;
    private String textoDesencriptado;
    private Encryption encryption;
    private int numElementos;
    private HttpServices http;
    private static String fraseQR;
    private static String fraseBD;
    private boolean encontrado;

    public ListaQR(){

        //int  numRandom = 12312;

        //String prueba = "Parable" + numRandom + "_" + "dfghjkljfddfghjklhgfdfghjk";
        //String prueba = "Parable" + numRandom + "_" + "dfghjkljfddfghjklhgfdfghjk";
        //String prueba = "Parable" + numRandom + "_" + "dfghjkljfddfghjklhgfdfghjk";
        //String prueba = "Parable" + numRandom + "_" + "dfghjkljfddfghjklhgfdfghjk";
        //String prueba = "Parable" + numRandom + "_" + "dfghjkljfddfghjklhgfdfghjk";
        //String prueba = "Parable" + numRandom + "_" + "dfghjkljfddfghjklhgfdfghjk";
        //String prueba = "Parable" + numRandom + "_" + "dfghjkljfddfghjklhgfdfghjk";




        /*encryption= Encryption.getDefault("Key", "Salt", new byte[16]);
        listadoQREnc=new ArrayList<>();
        listadoQRDesenc=new ArrayList<>();
        listadoLeidos=new ArrayList<>();
        textoEncriptado="";
        textoDesencriptado="";
        numElementos=5;*/

        //Frase para añadir en la base de datos
        fraseQR="";

        //Frase que recoge datos de la BD
        fraseBD="";

        encontrado=false;



        /*for(int i=0; i < numElementos; i++){
            listadoLeidos.add(0);
        }*/

        //QRbasicos();
        //encriptarArray();
    }

    public boolean isEncontrado() {
        return encontrado;
    }

    public void setEncontrado(boolean encontrado) {
        this.encontrado = encontrado;
    }


/* public int getTam(){
        return listadoQREnc.size();

    }
    public String encriptar (String frase){

        textoEncriptado = encryption.encryptOrNull(frase);

        return textoEncriptado;

    }

    public String desencriptar (String frase){

        textoDesencriptado = encryption.decryptOrNull(textoEncriptado);

        return textoDesencriptado;
    }*/


    /*public void QRbasicos(){

        String qr1= "Estas en la cafeteria. ¿Te tomas un cafe?";
        String qr2= "La puerta de esta clase esconde algo...";
        String qr3= "Esta clase es la mitad del camino";
        String qr4= "Este te lo hemos puesto facil";
        String qr5= "¿Te paraste a mirar por la ventana?";

        listadoQRDesenc.add(qr1);
        listadoQRDesenc.add(qr2);
        listadoQRDesenc.add(qr3);
        listadoQRDesenc.add(qr4);
        listadoQRDesenc.add(qr5);


    }*/

    public void anadirQR(String frase){


        fraseQR=frase;
        http = new HttpServices();
        new InsertarQR(ListaQR.this).execute();
        //new getQR(ListaQR.this).execute();
    }


    /*public boolean comprobarQR(String frase){


        fraseQR=frase;

        http = new HttpServices();
        new getQR(ListaQR.this).execute();

        Log.e("El valor de encontrado:", "" + isEncontrado());

        Log.e("LA FRASE Final :", fraseBD);


        if(fraseBD.equalsIgnoreCase("nada\n")){
            setEncontrado(false);
            Log.e("LA FRASE DE FALSE:", "false");
        }else{
            setEncontrado(true);
            Log.e("LA FRASE DE TRUE:", "true");
        }



        return isEncontrado();

        /*Log.e("TAMAÑO ARRAY", "es:" + listadoQREnc.size());

        for(int i=0; i < listadoQREnc.size() && encontrado==-1; i++) {
            if(listadoQREnc.get(i).equalsIgnoreCase(qr)){
                encontrado=i;
            }
        }


    }*/


    /*public void encriptarArray(){

        for(int i=0; i < numElementos; i++) {
            String enc = encriptar(listadoQRDesenc.get(i));
            listadoQREnc.add(enc);
        }
    }*/





    /*public String recuperarQR(int posicion)
    {
        return listadoQRDesenc.get(posicion);
    }




    public ArrayList<Integer> getListadoLeidos() {
        return listadoLeidos;
    }



    public ArrayList<String> getListadoQREnc() {
        return listadoQREnc;
    }

    public ArrayList<String> getListadoQRDesenc() {
        return listadoQRDesenc;
    }*/




    class InsertarQR extends AsyncTask<String,String,String> {

        private Activity context;

        InsertarQR(Activity context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub



            if (http.enviarQR(fraseQR))//cargamos en la base de datos
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






}
