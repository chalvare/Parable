package com.parable.parseoLenguaje;

import com.parable.actividades.MainActivity;
import com.parable.parablesdb.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Created by ReplacedKevin on 27/12/15.
 */
public class Lenguaje {



    private static ArrayList<String> sinonimosAndandoEn1;
    private static ArrayList<String> sinonimosAndandoEn2;
    private static ArrayList<String> sinonimosAndandoEn3;
    private static int numSinoAndandoEn1;
    private static int numSinoAndandoEn2;
    private static int numSinoAndandoEn3;

    private static ArrayList<String> sinonimosEntrandoEn1;
    private static ArrayList<String> sinonimosEntrandoEn2;
    private static ArrayList<String> sinonimosEntrandoEn3;
    private static int numSinoEntrandoEn1;
    private static int numSinoEntrandoEn2;
    private static int numSinoEntrandoEn3;





    public Lenguaje(){

        //////////////////ANDANDO_EN////////////////
        numSinoAndandoEn1=0;
        numSinoAndandoEn2=0;
        numSinoAndandoEn3=0;

        sinonimosAndandoEn1 = new ArrayList<>();
        sinonimosAndandoEn1.add(0,"camino");
        sinonimosAndandoEn1.add(1,"recorrido");
        sinonimosAndandoEn1.add(2,"trayecto");

        sinonimosAndandoEn2 = new ArrayList<>();
        sinonimosAndandoEn2.add(0,"duro");
        sinonimosAndandoEn2.add(1,"complicado");
        sinonimosAndandoEn2.add(2,"dificil");

        sinonimosAndandoEn3 = new ArrayList<>();
        sinonimosAndandoEn3.add(0,"rendirte");
        sinonimosAndandoEn3.add(1,"abandonar");
        sinonimosAndandoEn3.add(2,"tirar la toalla");

        /////////////////////////////////////////


        //////////////////ENTRANDO_EN////////////////
        numSinoEntrandoEn1=0;
        numSinoEntrandoEn2=0;
        numSinoEntrandoEn3=0;


        sinonimosEntrandoEn1 = new ArrayList<>();
        sinonimosEntrandoEn1.add(0,"explorando");
        sinonimosEntrandoEn1.add(1,"investigando");


        sinonimosEntrandoEn2= new ArrayList<>();
        sinonimosEntrandoEn2.add(0,"parar");
        sinonimosEntrandoEn2.add(1,"detenerte");

        sinonimosEntrandoEn3= new ArrayList<>();
        sinonimosEntrandoEn3.add(0,"vas andado por");
        sinonimosEntrandoEn3.add(1,"llegas a");
        sinonimosEntrandoEn3.add(2,"entras en");


        /////////////////////////////////////////






    }


    public  String inicio()
    {
        String fraseT= "";
        String frase= "";

        try {


            Properties propiedades = new Properties();


            InputStream is = MainActivity.activeActivity.getResources().openRawResource(R.raw.prop);
            propiedades.load(is);


            String nombre = propiedades.getProperty("Inicio");


            StringTokenizer st = new StringTokenizer(nombre, "[]");

            while (st.hasMoreTokens()) {

                frase = st.nextToken();

                fraseT +=frase;
            }


        } catch (FileNotFoundException e) {
            System.out.println("Error, El archivo no exite");
        } catch (IOException e) {
            System.out.println("Error, No se puede leer el archivo");
        }

        return fraseT + "\n";


    }

    public  String fin()
    {
        String fraseT= "";
        String frase= "";

        try {


            Properties propiedades = new Properties();


            InputStream is = MainActivity.activeActivity.getResources().openRawResource(R.raw.prop);
            propiedades.load(is);


            String nombre = propiedades.getProperty("Fin");


            StringTokenizer st = new StringTokenizer(nombre, "[]");

            while (st.hasMoreTokens()) {

                frase = st.nextToken();

                fraseT +=frase;
            }


        } catch (FileNotFoundException e) {
            System.out.println("Error, El archivo no exite");
        } catch (IOException e) {
            System.out.println("Error, No se puede leer el archivo");
        }

        return fraseT + "\n";


    }

    public static String andandoEn(String lugar) {

        String fraseT= "";
        String frase= "";

        try {


            Properties propiedades = new Properties();


            InputStream is = MainActivity.activeActivity.getResources().openRawResource(R.raw.prop);
            propiedades.load(is);


            String nombre = propiedades.getProperty("Andando_En");


            StringTokenizer st = new StringTokenizer(nombre, "[]");

            frase = st.nextToken();

            fraseT +=frase;
            fraseT +=lugar;

            st.nextToken();

            while (st.hasMoreTokens()) {

                frase = st.nextToken();

               if(frase.equalsIgnoreCase("Lugar"))
                    frase=lugar;
               else if(frase.equalsIgnoreCase("camino")) {
                    frase = sinonimosAndandoEn1.get(numSinoAndandoEn1);
                    numSinoAndandoEn1++;
               }
               else if (frase.equalsIgnoreCase("duro")) {
                   frase = sinonimosAndandoEn2.get(numSinoAndandoEn2);
                   numSinoAndandoEn2++;
               }else if (frase.equalsIgnoreCase("rendirte")){
                   frase = sinonimosAndandoEn3.get(numSinoAndandoEn3);
                   numSinoAndandoEn3++;
               }


                if(numSinoAndandoEn1==3)
                    numSinoAndandoEn1=0;
                else if(numSinoAndandoEn2==3)
                    numSinoAndandoEn2=0;
                else if (numSinoAndandoEn3==4)
                    numSinoAndandoEn3=0;


                fraseT +=frase;
            }


        } catch (FileNotFoundException e) {
            System.out.println("Error, El archivo no exite");
        } catch (IOException e) {
            System.out.println("Error, No se puede leer el archivo");
        }

        return fraseT + "\n";

    }

    public static String entrandoEn(String lugar) {

        String fraseT = "";
        String frase="";

        try {


            Properties propiedades = new Properties();



            InputStream is = MainActivity.activeActivity.getResources().openRawResource(R.raw.prop);
            propiedades.load(is);


            String nombre = propiedades.getProperty("Entrando_En");


            StringTokenizer st = new StringTokenizer(nombre, "[]");

            frase = st.nextToken();

            fraseT +=frase;
            fraseT +=lugar;

            st.nextToken();

            while (st.hasMoreTokens()) {

                frase = st.nextToken();

                if(frase.equalsIgnoreCase("acabas de entrar en")) {
                    frase = sinonimosEntrandoEn3.get(numSinoEntrandoEn3);
                    numSinoEntrandoEn3++;
                }
                else if(frase.equalsIgnoreCase("Lugar")){
                    frase=lugar;
                }
                else if(frase.equalsIgnoreCase("explorando")) {
                    frase = sinonimosEntrandoEn1.get(numSinoEntrandoEn1);
                    numSinoEntrandoEn1++;
                }
                else if (frase.equalsIgnoreCase("parar")) {
                    frase = sinonimosEntrandoEn2.get(numSinoEntrandoEn2);
                    numSinoEntrandoEn2++;
                }


                if(numSinoEntrandoEn1==2)
                    numSinoEntrandoEn1=0;
                else if(numSinoEntrandoEn2==2)
                    numSinoEntrandoEn2=0;
                else if(numSinoEntrandoEn3==3)
                    numSinoEntrandoEn3=0;



                fraseT +=frase;

            }



        } catch (FileNotFoundException e) {
            System.out.println("Error, El archivo no exite");
        } catch (IOException e) {
            System.out.println("Error, No se puede leer el archivo");
        }


        return fraseT + "\n";

    }

    public static String saliendoDe(String lugar) {

        String fraseT = "";
        String frase="";

        try {


            Properties propiedades = new Properties();



            InputStream is = MainActivity.activeActivity.getResources().openRawResource(R.raw.prop);
            propiedades.load(is);


            String nombre = propiedades.getProperty("Saliendo_De");

            StringTokenizer st = new StringTokenizer(nombre, "[]");

            frase = st.nextToken();

            fraseT +=frase;
            fraseT +=lugar;

            st.nextToken();

            while (st.hasMoreTokens()) {

                frase = st.nextToken();
                if(frase.equalsIgnoreCase("Lugar"))
                    frase=lugar;

                fraseT +=frase;

            }



        } catch (FileNotFoundException e) {
            System.out.println("Error, El archivo no exite");
        } catch (IOException e) {
            System.out.println("Error, No se puede leer el archivo");
        }


        return fraseT;

    }

}
