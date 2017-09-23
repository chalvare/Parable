package com.parable.WPS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.util.Log;

import com.parable.actividades.MainActivity;
import com.parable.actividades.Posicion;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Altair on 17/11/15.
 * Hilo de ejecución que se encarga de ubicar al usuario en el mapa
 */
public class ThreadUbicacion extends Thread {

    private WPS lista;//para escanear las wifis a nuestro alrededor
    private Posicion actividadPadre;//para pasar el contexto en el que nos encontramos
    private int estado;//estado actual de ejecución, parado o error
    private Coordenada coord;//coordenada de donde nos encontramos actualmente


    private long time;
    private ParticleFilter filtro;
    private SensorManager mSensorManager;
    public float[] magnetic_angles;
    public float[] accelerations;
    private boolean giroValido;
    //base de datos de pruebas para las particulas
    private WPSDatabase baseDeDatos;
    /**
     * Mapa con las zonas permitidas en blanco
     */
    public static Bitmap mapaMascara;

    private final int ESTADO_PARADO = 0;
    private final int ESTADO_EJECUCION = 1;
    private final int ESTADO_ERROR = 2;


    private ArrayList<Coordenada> listaCoordenadas;

    /**
     * Constructora
     * @Param Le pasamos la actividad que tiene el mapa pintado
     */

    public ThreadUbicacion(Posicion actividadMapa) throws SQLException, WPSException {
        actividadPadre=actividadMapa;
        this.setName("ThreadUbicacion");
        estado = ESTADO_EJECUCION;

        //pruebas para el particlefilter
        baseDeDatos = MainActivity.threadDatos.getDatabase();


        mSensorManager = (SensorManager) actividadMapa.getSystemService(Context.SENSOR_SERVICE);

        Sensor s = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        Sensor s2 = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);

        SensorEventListener magneticAnglesListener = new SensorEventListener(){

            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

            public void onSensorChanged(SensorEvent event) {
                magnetic_angles = event.values;
            }

        };

        SensorEventListener accelerationsListener = new SensorEventListener(){

            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

            public void onSensorChanged(SensorEvent event) {
                accelerations = event.values;
            }

        };

        mSensorManager.registerListener(accelerationsListener, s, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(magneticAnglesListener, s2, SensorManager.SENSOR_DELAY_GAME);

//        ImageView img = new ImageView(actividadMapa);
//        img.setImageResource(R.drawable.mapamascara); //mapa que esta en drawable.
//        mapaMascara = BitmapFactory.decodeResource(img.getResources(), R.drawable.mapamascara);





    }


    public void run(){
        lista = new WPS(actividadPadre);
        WifiManager manager = (WifiManager)actividadPadre.getSystemService(Context.WIFI_SERVICE);





        while(estado!=ESTADO_ERROR){
            if(estado==ESTADO_EJECUCION){
                if(!manager.isWifiEnabled()){//si el wifi esta parado lo activamos
                    estado=ESTADO_PARADO;
                    activarWifi();

                }else{//si el wifi esta funcionando
                    try{


                        /****************************/
                        coord = MainActivity.threadDatos.getDatabase().calcularPosicion(lista.scanAndShow());
                        //coord = stimation(coord);
                        coord=miRedondeo(coord);
                        /****************************/




                        if(coord.getX()!=1&& coord.getY()!=1)
                            actividadPadre.cargarDatosXML(coord);


                        sleep(3000);







                    }catch (WPSException e){
                        Log.e("ThreadUbicacion", e.getMessage());
                        e.printStackTrace();
                        estado = ESTADO_PARADO;
                        activarWifi();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    if(MainActivity.activeActivity == actividadPadre){
//                        actividadPadre.refresh(coord.getX(),coord.getY(),id);
//                    }
                }
            }//fin if
        }//fin while


    }

    /**
     * Pregunta al usuario si quiere activar el WiFi
     */
    private void activarWifi(){

        AlertDialog.Builder builder = new AlertDialog.Builder(actividadPadre);
        builder.setMessage("El dispositivo WiFi esta desactivado.");
        builder.setPositiveButton("Activar", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                WifiManager manager = (WifiManager) actividadPadre.getSystemService(Context.WIFI_SERVICE);
                manager.setWifiEnabled(true);
                int i = 0;
                boolean b = false;
                while (!b && i < 10) {
                    b = manager.isWifiEnabled();
                    i++;
                    try {
                        SystemClock.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (manager.isWifiEnabled()) {
                    estado = ESTADO_EJECUCION;
                } else {
                    estado = ESTADO_ERROR;
                }
            }

        });

		/*builder.setCancelable(false);
		AlertDialog alert = builder.create();
		alert.show();*/
    }

    /**
     * Estima la posici�n del m�vil con el filtro de part�culas
     * @param coordK devuelta por el algoritmo est�ndra de posicionamiento
     * @throws Exception
     */
    public Coordenada stimation(Coordenada coordK) throws Exception{

        if(magnetic_angles!=null && accelerations!=null){
            float[] R = new float[9];
            float[] I = new float[9];
            float[] result = new float[3];
            //float aux;
            if(SensorManager.getRotationMatrix(R, I, accelerations, magnetic_angles)){
                //float inclination = (float)((SensorManager.getInclination(I)*180)/Math.PI);
                //accZNorm.setText("" + inclination);
                //SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Y, R2);
                SensorManager.getOrientation(R, result);
                float giroSobreX = (float) Math.toDegrees(result[1]);
                float giroSobreY= (float) Math.toDegrees(result[2]);
                float giroSobreZ = (float) Math.toDegrees(result[0]);

                //Calculo si la posici�n del m�vil es v�lida
//                Log.e("Giro sobre x",String.valueOf(giroSobreX));
//                Log.e("Giro sobre Y",String.valueOf(giroSobreY));
//                Log.e("Giro sobre Z",String.valueOf(giroSobreZ));
                if((giroSobreX>=-100 && giroSobreX<=100) && (giroSobreY>=-10 && giroSobreY<=190)){
                    setGiroValido(true);
                    float newAngle;

                    //Calculo la aceleraci�n en el eje Z combinando los giros de X e Y

                    if(giroSobreY<=0 && giroSobreY>=-90){
                        newAngle = -giroSobreY;
                    }else{
                        newAngle = 180 - Math.abs(giroSobreY);
                    }
                    double radiansY = Math.toRadians(newAngle);
                    double accAux;
                    if(accelerations[2]>0){
                        accAux = accelerations[2] - Math.cos(radiansY)* SensorManager.STANDARD_GRAVITY;

                    }else{
                        accAux = accelerations[2] +  Math.cos(radiansY)* SensorManager.STANDARD_GRAVITY;
                    }

                    //Redirijo el �ngulo de Z por el giro del m�vil en modo landscape
                    giroSobreZ = giroSobreZ - 90;
                    if(giroSobreZ < -180) giroSobreZ = giroSobreZ + 360;
                    //En el caso de que la aceleraci�n sea negativa invierto su direcci�n
                    if(accAux<0){
                        if(giroSobreZ<0){
                            giroSobreZ = giroSobreZ + 180;
                        }else {
                            giroSobreZ = giroSobreZ - 180;
                        }
                        accAux = -accAux;
                    }

                    //Calculo las aceleraciones en los ejes X e Y del mapa

					/*boolean accXPositiva;
					boolean accYPositiva;*/
                    double accX;
                    double accY;
                    if(giroSobreZ>=-180 && giroSobreZ<=-90){
                        newAngle = giroSobreZ + 180;
						/*accXPositiva = true;
						accYPositiva = false;*/
                        accX = accAux * Math.sin(Math.toRadians(newAngle));
                        accY = -(accAux * Math.cos(Math.toRadians(newAngle)));
                    }else if(giroSobreZ>-90 && giroSobreZ <=0){
                        newAngle = giroSobreZ + 90;
						/*accXPositiva = true;
						accYPositiva = true;*/
                        accX = accAux * Math.cos(Math.toRadians(newAngle));
                        accY = accAux * Math.sin(Math.toRadians(newAngle));
                    }else if(giroSobreZ>0 && giroSobreZ <=90){
                        newAngle = giroSobreZ;
						/*accXPositiva = false;
						accYPositiva = true;*/
                        accY = accAux * Math.cos(Math.toRadians(newAngle));
                        accX = -(accAux * Math.sin(Math.toRadians(newAngle)));
                    }else{
                        newAngle = giroSobreZ - 90;
						/*accXPositiva = false;
						accYPositiva = false;*/
                        accX = -(accAux * Math.cos(Math.toRadians(newAngle)));
                        accY = -(accAux * Math.sin(Math.toRadians(newAngle)));
                    }
                    time = System.currentTimeMillis()/1000;
                    filtro = new ParticleFilter(accX,accY,baseDeDatos);
                    filtro.prediction(accX, accY, time);
                    filtro.correction(coordK);
                    //filtro.particleUpdate();
                    //filtro.resampling();
                    Coordenada c2 = filtro.stimation(coordK);
                    return c2;
                }else{
                    setGiroValido(false);
                    throw new Exception("Giro no v�lido.");
                }
            }else{
                throw new Exception("Error con los sensores.");
            }
        }else{
            throw new Exception("Error con los sensores.");
        }
    }

    public Coordenada miRedondeo(Coordenada miC){
        double randomX = Math.random();
        double randomY = Math.random();

        if(randomX >=0.25 && randomX < 0.5 || randomX>=0.5 && randomX <= 0.75){
             miC.setX(miC.getX()+0.5);
        }else if(randomX > 0.75) miC.setX(miC.getX() + 1);

        if(randomY >=0.25 && randomY < 0.5 || randomY>=0.5 && randomY <= 0.75){
            miC.setY(miC.getY() + 0.5);
        }else if(randomY > 0.75) miC.setY(miC.getY() + 1);


        return miC;
    }
    public void setGiroValido(boolean giroValido) {
        this.giroValido = giroValido;
    }

    public boolean isGiroValido() {
        return giroValido;
    }

    public Coordenada getC(){
        return coord;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }
}
