package com.parable.WPS;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Altair on 25/11/15.
 */
public class ParticleFilter {

    private int numParticulas = 10;
    private static final double sigma = 0.5;

    private ArrayList<Particle> particulas;

    /**
     * Mapa con las zonas permitidas en blanco
     */
    //private Bitmap mapaMascara;

    /**
     * Tiempo actual
     */
    private double time=0;

    private WPSDatabase database;

    public final static int colorBlanco = -1025;

    public ParticleFilter(double x, double y, WPSDatabase database){
        setParticulas(new ArrayList<Particle>());
        this.database = database;
        Particle p;
        double randomX;
        double randomY;
        for(int i = 0; i < numParticulas; i++){
            p = new Particle();
//            p.setWeight(1/numParticulas);
            randomX = Math.random();
            randomY = Math.random();
            if(randomX < 0.5){
                p.setX(x + randomX*0.5);
            }else{
                p.setX(x - randomX*0.5);
            }
            if(randomY < 0.5){
                p.setY(y + randomY*0.5);
            }else{
                p.setY(y - randomY*0.5);
            }

            p.setVelocityX(0);
            p.setVelocityY(0);
            particulas.add(p);

        }
    }

    /**
     * M�todo que realiza el paso de predicci�n, es decir, predice la nueva posici�n y velocidad de las part�culas
     * @param accX aceleraci�n del terminal en las coordenadas X del mapa, dado por el aceler�metro
     * @param accY aceleraci�n del terminal en las coordenadas Y del mapa, dado por el aceler�metro
     * @param time segundos actuales
     */
    public void prediction(double accX, double accY, double time){
        double incTime;
        if(this.time == 0){
            incTime = 0;
        }else{
            incTime = time - this.time;
        }
        double incTimePow = (Math.pow(incTime, 2))/2;
        Particle p;
        double x_old;
        double y_old;
        //Log.e("EL TAMAÑO:", String.valueOf(particulas.size()));
        for(int i = 0; i < particulas.size(); i++){
            p = particulas.get(i);
            /**
             * Xk = Xk-1 + At*vx + At2/2*ax
             */
            x_old = p.getX();
            y_old = p.getY();
            p.setX(/*Math.round(*/p.getX() + incTime*p.getVelocityX() + incTimePow*accX/*)*/);
            p.setY(/*Math.round(*/p.getY() + incTime * p.getVelocityY() + incTimePow*accY/*)*/);
            p.setVelocityX(p.getVelocityX() + incTime*accX);
            p.setVelocityY(p.getVelocityY() + incTime*accY);
            p.setProb_Xk_Xk1(1);
        }
        this.time = time;
    }

    /**
     * M�todo que corrije las probabilidades de las part�culas que definen la probabilidad
     * de que est� en la posici�n dada por el algoritmo k-closest neibourghs.
     * @param c posici�n devuelta por el algoritmos de posicionamiento est�ndar
     */
    public void correction(Coordenada c){
        Particle p;
        double probBase =  1 / (Math.sqrt(2*Math.PI)*sigma);
        Log.e("CORRECION PROBASE",String.valueOf(probBase));
        double exp;
        for(int i = 0; i < particulas.size(); i++){
            p = particulas.get(i);
            exp = (Math.pow(p.getX() - c.getX(),2) + Math.pow(p.getY() - c.getY(), 2))/(2*sigma);
//            Log.e("CORRECION EXP",String.valueOf(exp));
            p.setProb_Zk_Xk(Math.pow(probBase, exp));
        }
    }

    /**
     * Actualiza los pesos de las part�culas dependiendo de las probabilidades anteriormente calculadas
     */
    public void particleUpdate(){
        Particle p;
        for(int i = 0; i < particulas.size(); i++){
            p = particulas.get(i);
            Log.e("Particle Update",String.valueOf(p.getWeight()));
            Log.e("Particle Update", String.valueOf(p.getProb_Zk_Xk()));
            double aux=p.getWeight() * p.getProb_Zk_Xk();
//            Log.e("Particle Update",String.valueOf(aux));
            p.setWeight(aux);
        }
    }

    /**
     * Remuestrea las part�culas en el caso de que hayan perdido mucho peso.
     */
    public void resampling(){
        double sum = 0;
        for(int i = 0; i < particulas.size(); i++){
            sum += Math.pow(particulas.get(i).getWeight(),2);
        }
        Log.e("SUm de resampling", String.valueOf(sum));
        sum = 1/sum;
        Log.e("SUm de resampling", String.valueOf(sum));
        if(sum < 0.5){
            ArrayList<Particle> particulasNuevas = new ArrayList<Particle>();
            quicksort(0,particulas.size()-1);
            int mitad = numParticulas/2;
            Particle p;
            for(int i = 0; i < numParticulas; i++){
                p = particulas.get(i % mitad).clone();
                p.setWeight(1/numParticulas);
                particulasNuevas.add(p);
            }
            if(particulasNuevas.size()<numParticulas){
                p = particulas.get(0).clone();
                p.setWeight(1/numParticulas);
                particulasNuevas.add(p);
            }
            this.particulas = particulasNuevas;
        }
    }

    /**
     * Ordena el vector de part�culas por peso.
     * @param primero
     * @param ultimo
     */
    public void quicksort(int primero, int ultimo){

        int i=primero, j=ultimo;
        //int pivote=vector[(primero+ultimo)/2];
        double pivote = (particulas.get(primero).getWeight() + particulas.get(ultimo).getWeight())/2;
        Particle auxiliar;

        do{
            while(particulas.get(i).getWeight()<pivote) i++;
            while(particulas.get(j).getWeight()>pivote) j--;

            if (i <= j){
                auxiliar = particulas.get(j);
                particulas.set(j, particulas.get(i));
                particulas.set(i, auxiliar);
                i++;
                j--;
            }

        } while (i<=j);

        if(primero<j) quicksort(primero, j);
        if(ultimo>i) quicksort(i, ultimo);

    }

    public Coordenada stimation(Coordenada c){
        ArrayList<Coordenada> coordenadas = database.getClosetsCoords(c);
        Log.e("Tamaño de coordenadas stimation",String.valueOf(coordenadas.size()));
        double resX = 0;
        double resY = 0;
        double prob;
        Coordenada result = new Coordenada();
        for(int i = 0; i < coordenadas.size(); i++){
            prob = calculatePXkZ0k(coordenadas.get(i));
            Log.e("PROOOOOOBBBB",String.valueOf(prob));
            resX += coordenadas.get(i).getX()*prob;
            resY += coordenadas.get(i).getY()*prob;
        }
        Log.e("esult.setX(redondear(resX)",String.valueOf(redondear(resX)));
        Log.e("esult.setY(redondear(resY)",String.valueOf(redondear(resY)));
        result.setX(redondear(resX));
        result.setY(redondear(resY));

        //result.setX(resX);
        //result.setY(resY);
        result.setZ(1);
        //aqui las coordenadas ya son 0
        return result;
    }

    public int deltaDirac(double val){
        if(val==0){
            return Integer.MAX_VALUE;
        }else{
            return 1;
        }
    }

    public double calculatePXkZ0k(Coordenada pos){
        double result = 0;
        Coordenada c;
        double aux;
        pos.setX(redondear(pos.getX()));
        pos.setY(redondear(pos.getY()));
        pos.setZ(1);

        for(int i = 0; i < particulas.size(); i++){
            c = new Coordenada();
//            Log.e(" Particulas X en calculatePXkZ0k",String.valueOf(particulas.get(i).getX()));
//            Log.e(" Particulas Y en calculatePXkZ0k",String.valueOf(particulas.get(i).getY()));
            c.setX(redondear(particulas.get(i).getX()));
            c.setY(redondear(particulas.get(i).getY()));
            //c.setX(particulas.get(i).getX());
            //c.setY(particulas.get(i).getY());
            c.setZ(1);
            if(c.equals(pos)) aux = 0;
            else aux = 1;
            Log.e(" Particulas zkAQuisecambia en calculatePXkZ0k",String.valueOf(particulas.get(i).getProb_Zk_Xk()));
            result += particulas.get(i).getProb_Zk_Xk()*deltaDirac(aux);
        }
        Log.e("ParticleFilter calculatePXkZ0k",String.valueOf(result));
        return result;
    }


    public void setParticulas(ArrayList<Particle> particulas) {
        this.particulas = particulas;
    }

    public ArrayList<Particle> getParticulas() {
        return particulas;
    }

    /**
     * Trunca el valor por par�metro de la siguiente manera: si la parte decimal es menor que 0.25 la elimina,
     * si est� entre 0.25 y 0.75 la estima como 0.5 y si es mayor que 0.75 la estima como 1.
     * @param d
     * @return
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
