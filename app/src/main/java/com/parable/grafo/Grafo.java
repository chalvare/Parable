package com.parable.grafo;

import android.util.Log;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;

/**
 * Created by Altair on 1/3/16.
 */
public class Grafo {

    UndirectedGraph<String, DefaultEdge> grafo;
    public Grafo(String planta) {
        if(planta.equalsIgnoreCase("Planta1"))
            this.grafo = crearGrafoPlanta1();
    }

    private static UndirectedGraph<String, DefaultEdge> crearGrafoPlanta1()
    {
        UndirectedGraph<String, DefaultEdge> g = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        String v1 = "Inicio del Pasillo de la Izquierda";
        String v2 = "Medio del Pasillo de la Izquierda";
        String v3 = "Final del Pasillo de la Izquierda";
        String v4 = "MiniPasillo Norte";
        String v5 = "Inicio del Pasillo de la Derecha";
        String v6 = "Medio del Pasillo de la Derecha";
        String v7 = "Final del Pasillo de la Derecha";
        String v8 = "MiniPasillo Sur";

        // add the vertices
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addVertex(v8);

        // add edges to create a circuit
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v4);
        g.addEdge(v4, v5);
        g.addEdge(v5, v6);
        g.addEdge(v6, v7);
        g.addEdge(v7, v8);
        g.addEdge(v8, v1);

        return g;
    }

    public static ArrayList<String> cargarCheckpoints(){
        ArrayList<String>checkpoints = new ArrayList<String>();

        checkpoints.add("Final del Pasillo de la Izquierda");
        checkpoints.add("Medio del Pasillo de la Derecha");

        return checkpoints;

    }

    public static ArrayList<String> cargarCaminoaux(){
        ArrayList<String>caminoAuxiliar = new ArrayList<String>();
        caminoAuxiliar.add("Inicio del Pasillo de la Izquierda");
        caminoAuxiliar.add("Inicio del Pasillo de la Izquierda");
        caminoAuxiliar.add("MiniPasillo Norte");
        caminoAuxiliar.add("Inicio del Pasillo de la Izquierda");
        caminoAuxiliar.add("Medio del Pasillo de la Izquierda");
        caminoAuxiliar.add("Final del Pasillo de la Izquierda");
        caminoAuxiliar.add("MiniPasillo Sur");
        caminoAuxiliar.add("Final del Pasillo de la Derecha");
        caminoAuxiliar.add("Medio del Pasillo de la Derecha");
        return caminoAuxiliar;

    }

    public String camino (UndirectedGraph grafo, String inicio, String check){



        //UndirectedGraph<String, DefaultEdge> stringGraph = createStringGraph();

//        for(int i=0; i<camino2.size();i++){
//            Log.e("Camino:  ", camino2.get(i).toString());
//        }

        //este for sera sustituido por el dato xml de nuestra situacion

        double cortoOptimo=9999;
        String vecinoMejor="";

        NeighborIndex vecinos1 = new NeighborIndex(grafo);
        ArrayList<String> vecinos = (ArrayList<String>) vecinos1.neighborListOf(inicio);

        String grupoVecinos="";
        for(int j=0; j<vecinos.size();j++){
            grupoVecinos+=" "+ vecinos.get(j);
        }

        Log.e("Puedes dirigirte a todos estos lugares: ", grupoVecinos);

        // String inicio = stringGraph.getEdgeSource(camino2.get(i));

        for(int j=0; j<vecinos.size();j++){
            DijkstraShortestPath caminoOptimo = new DijkstraShortestPath(grafo,vecinos.get(j),check);
            double corto= caminoOptimo.getPathLength();
            Log.e("El valor del camino: ",String.valueOf(corto));
            if(corto<cortoOptimo) {
                cortoOptimo = corto;
                vecinoMejor=vecinos.get(j);
            }

        }
        Log.e("El valor del camino: ",vecinoMejor + " ve por aqui anda");

        return vecinoMejor;



    }

    public UndirectedGraph<String, DefaultEdge> getGrafo() {
        return grafo;
    }

    public void setGrafo(UndirectedGraph<String, DefaultEdge> grafo) {
        this.grafo = grafo;
    }
}
