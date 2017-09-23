package com.parable.http;

import android.util.Log;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parable.WPS.Coordenada;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Altair on 10/11/15.
 */
public class HttpServices {



    public boolean insertarBD(String macEdit, EditText xEdit, EditText yEdit, EditText zEdit, int nivelEdit){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost("http://tot.fdi.ucm.es/parable/insertarEnBD.php");
        //httpPost = new HttpPost("http://192.168.1.135/parable/insertarEnBD.php");
        nameValuePairs = new ArrayList<NameValuePair>();
        //nameValuePairs.add(new BasicNameValuePair("numX",numX.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("mac", macEdit));
        nameValuePairs.add(new BasicNameValuePair("x",xEdit.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("y",yEdit.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("z",zEdit.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("nivel", String.valueOf(nivelEdit)));


        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpClient.execute(httpPost);

            return true;
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Recupera todas las coordenadas almacenadas en la BD
     * @return String
     * @throws HttpServicesException
     */
    private String recuperarCoordenadasEnString(){
        HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httppost = new HttpPost("http://192.168.1.135/parable/getAllPositions.php");
        HttpPost httppost = new HttpPost("http://tot.fdi.ucm.es/parable/getAllPositions.php");
        String resultado="";
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            resultado= convertStreamToString(instream);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.e("Resultado",resultado);
        return resultado;
    }

    /**
     * @param is
     * @return String
     * @throws IOException
     * Convierte la secuencia de entrada en un string (método buscado en internet)
     */
    private String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * Transforma el String de recuperarCoordenadasEnString en un JSONArray para que sea más manejable
     * @return ArrayList
     */
    public /*ArrayList<Coordenada>*/JSONArray getCoordenadas(){
        ArrayList<Coordenada> listaCoordenadas=new ArrayList<Coordenada>() ;
        Coordenada coordenada;
        listaCoordenadas.clear();
        String data=recuperarCoordenadasEnString();
        JSONArray jsonArray;
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try {
                json = new JSONObject(data);
                jsonArray = json.optJSONArray("coordenada");

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
                return jsonArray;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //return listaCoordenadas;

        }
        return null;
    }

    public boolean enviarHistoria(EditText nombre, TextView texto, RatingBar valoracion, TextView puntuacion){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost("http://tot.fdi.ucm.es/parable/web/insertarEnHistoria.php");
        //httpPost = new HttpPost("http://192.168.1.135/parable/insertarEnBD.php");
        nameValuePairs = new ArrayList<NameValuePair>();
        //nameValuePairs.add(new BasicNameValuePair("numX",numX.getText().toString().trim()));

        nameValuePairs.add(new BasicNameValuePair("nombre", nombre.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("texto", texto.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("valoracion", String.valueOf(valoracion.getRating())));

        String aux = puntuacion.getText().toString().trim();
        String[]pun = aux.split(":");
        nameValuePairs.add(new BasicNameValuePair("puntuacion", pun[1]));

        try{
            Log.e("try", "Aqui");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.e("try", "Aqui");
            httpClient.execute(httpPost);
            Log.e("try", "Aqui");
            return true;
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean enviarQR(String fraseQR){


        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost("http://tot.fdi.ucm.es/parable/insertarQR.php");
        //httpPost = new HttpPost("http://192.168.1.135/parable/insertarEnBD.php");
        nameValuePairs = new ArrayList<NameValuePair>();
        //nameValuePairs.add(new BasicNameValuePair("numX",numX.getText().toString().trim()));

        nameValuePairs.add(new BasicNameValuePair("fraseEnc", fraseQR));

        try{
            Log.e("try", "Aqui");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.e("try", "Aqui");
            httpClient.execute(httpPost);
            Log.e("try", "Aqui");
            return true;
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean modificarLeido(){


        HttpClient httpClient;

        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost("http://tot.fdi.ucm.es/parable/modificarLeido.php");
        //httpPost = new HttpPost("http://192.168.1.135/parable/insertarEnBD.php");

        //nameValuePairs.add(new BasicNameValuePair("numX",numX.getText().toString().trim()));



        try{
            Log.e("try", "Aqui");
            //httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.e("try", "Aqui");
            httpClient.execute(httpPost);
            Log.e("try", "Aqui");
            return true;
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * Recupera todas las coordenadas almacenadas en la BD
     * @return String
     * @throws HttpServicesException
     */
    public String recuperarQR(String fraseEnc){

        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        Log.e("ENTROOOENRECUPERAR","asdasd");
        httpPost = new HttpPost("http://tot.fdi.ucm.es/parable/getQR.php");
        String resultado="";
        HttpResponse response;


        nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("frase", fraseEnc));


        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            resultado= convertStreamToString(instream);

            Log.e("ResultadoESSSS",resultado);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resultado;
    }



    public boolean enviarValoracion(EditText nombre, EditText resena, RatingBar valoracion){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost("http://tot.fdi.ucm.es/parable/insertarValoracion.php");
        //httpPost = new HttpPost("http://192.168.1.135/parable/insertarEnBD.php");
        nameValuePairs = new ArrayList<NameValuePair>();
        //nameValuePairs.add(new BasicNameValuePair("numX",numX.getText().toString().trim()));

        nameValuePairs.add(new BasicNameValuePair("nombre", nombre.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("resena",resena.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("valoracion", String.valueOf(valoracion.getRating())));

        try{
            Log.e("try", "Aqui");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Log.e("try", "Aqui");
            httpClient.execute(httpPost);
            Log.e("try", "Aqui");
            return true;
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }



}
