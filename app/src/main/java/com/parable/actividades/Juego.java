package com.parable.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parable.parablesdb.R;

import java.util.Random;

/**
 * Created by Altair on 23/2/16.
 */
public class Juego extends ActionBarActivity {

    public static final int PIEDRA = 1;
    public static final int PAPEL = 2;
    public static final int TIJERA = 3;
    public static final int LAGARTO = 4;
    public static final int SPOCK = 5;

    private TextView resultado;
    private TextView marcador;

    private ImageButton bpiedra;
    private ImageButton bpapel;
    private ImageButton btijera;
    private ImageButton blagarto;
    private ImageButton bspock;

    private Button bjuego;
    private Button bayuda;

    private ProgressBar progressBar;
    private int eleccionJugador;
    private int contadorJugador;
    private int contadorOrdenador;
    private int eleccionOrdenador;

    private Handler handler = new Handler();
    private int progressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);
        MainActivity.activeActivity=this;

        eleccionJugador=0;
        contadorJugador=0;
        contadorOrdenador=0;
        eleccionOrdenador = 0;

        resultado = (TextView) findViewById(R.id.resultado);
        marcador = (TextView)findViewById(R.id.marcador);


        bpiedra = (ImageButton)findViewById(R.id.bpiedra);
        bpapel = (ImageButton)findViewById(R.id.bpapel);
        btijera = (ImageButton)findViewById(R.id.btijera);
        blagarto = (ImageButton)findViewById(R.id.blagarto);
        bspock = (ImageButton)findViewById(R.id.bspock);
        bjuego = (Button)findViewById(R.id.bjugar);
        bayuda = (Button)findViewById(R.id.bayuda);

        bpiedra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              eleccionJugador=1;
                resetBotones();
                bpiedra.setImageResource(R.drawable.piedrapulsado);

            }
        });
        bpapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eleccionJugador=2;
                resetBotones();
                bpapel.setImageResource(R.drawable.papelpulsado);
            }
        });
        btijera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eleccionJugador=3;
                resetBotones();
                btijera.setImageResource(R.drawable.tijerapulsado);
            }
        });
        blagarto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eleccionJugador=4;
                resetBotones();
                blagarto.setImageResource(R.drawable.lagartopulsado);
            }
        });
        bspock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eleccionJugador=5;
                resetBotones();
                bspock.setImageResource(R.drawable.spockpulsado);
            }
        });

        bayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.activeActivity,AyudaEG.class);
                startActivity(i);
            }
        });


        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //  android:src="@drawable/twitter"
        bjuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eleccionJugador != 0) {
                   final int partida = juego(eleccionJugador);
                    progressStatus=0;
                    new Thread(new Runnable() {
                        public void run() {
                            while (progressStatus < 100) {
                                progressStatus += 10;
                                // Update the progress bar and display the
                                //current value in the text view
                                handler.post(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(progressStatus);
                                        // textView.setText(progressStatus+"/"+progressBar.getMax());
                                        if(progressStatus==100) {
                                            evaluacion(partida);
                                            evaluacionTextos(partida);
                                            actualizarMarcador();

                                        }
                                    }
                                });

                                try {
                                    // Sleep for 200 milliseconds.
                                    //Just to display the progress slowly
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }).start();




                } else {
                    resultado.setText("Resultado: Elige una de las opciones");
                }
            }
        });






    }

    private void resetBotones(){
        bpiedra.setImageResource(R.drawable.piedra);
        bpapel.setImageResource(R.drawable.papel);
        btijera.setImageResource(R.drawable.tijera);
        blagarto.setImageResource(R.drawable.lagarto);
        bspock.setImageResource(R.drawable.spock);
    }

    public void actualizarMarcador(){
        String score = String.valueOf(contadorJugador) +"/"+ String.valueOf(contadorOrdenador);
        marcador.setText("Marcador: "+ score);

    }

    public void evaluacion(int res){
        switch(res){
            case 0:
                contadorJugador++;
                break;
            case 1:
                break;
            case 2:
                contadorOrdenador++;
                break;
            case 3:
                break;
            default:

        }
    }

    public void evaluacionTextos(int res){
        switch(res){
            case 0:
                resultado.setText("Resultado: Has ganado!! El ordenador eligio: "+jugadaOrdenador(eleccionOrdenador));
                break;
            case 1:
                resultado.setText("Resultado: Empate!! El ordenador eligio: "+jugadaOrdenador(eleccionOrdenador));
                break;
            case 2:
                resultado.setText("Resultado: Has Perdido El ordenador eligio: "+jugadaOrdenador(eleccionOrdenador));
                break;
            case 3:
                resultado.setText("Resultado: ERROR!!!");
                break;
            default:

        }
    }

    public int juego(int jugador){

        Random rand = new Random();
        eleccionOrdenador = rand.nextInt((5 - 1) + 1) + 1;

        switch(jugador){
            case PIEDRA:
                if(eleccionOrdenador==TIJERA || eleccionOrdenador==LAGARTO)
                    return 0;
                else if(eleccionOrdenador==PIEDRA)
                    return 1;
                else return 2;

            case PAPEL:
                if(eleccionOrdenador==PIEDRA || eleccionOrdenador==SPOCK)
                    return 0;
                else if(eleccionOrdenador==PAPEL)
                    return 1;
                else return 2;
            case TIJERA:
                if(eleccionOrdenador==PAPEL || eleccionOrdenador==LAGARTO)
                    return 0;
                else if(eleccionOrdenador==TIJERA)
                    return 1;
                else return 2;
            case LAGARTO:
                if(eleccionOrdenador==PAPEL || eleccionOrdenador==SPOCK)
                    return 0;
                else if(eleccionOrdenador==LAGARTO)
                    return 1;
                else return 2;
            case SPOCK:
                if(eleccionOrdenador==PIEDRA || eleccionOrdenador==TIJERA)
                    return 0;
                else if(eleccionOrdenador==SPOCK)
                    return 1;
                else return 2;
            default:
                return 3;

        }
    }


    public String jugadaOrdenador(int ordenador){
        switch(ordenador){
            case 1:
                return "Piedra";
            case 2:
                return "Papel";
            case 3:
                return "Tijera";
            case 4:
                return "Lagarto";
            case 5:
                return "Spock";
            default:
                return "Error de texto";

        }
    }

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

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this,Acercade.class);
        startActivity(i);
    }
}
