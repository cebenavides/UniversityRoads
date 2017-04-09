package com.university;

import java.util.List;
import org.jgrapht.graph.*;
import org.jgrapht.alg.shortestpath.*;
import java.util.ArrayList;
import java.util.Collections;
import org.jgrapht.*;
import com.opencsv.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class UniversityRoads {

    public static void main(String[] args) throws IOException {
        computeMatrix();
    }

    private static void computeMatrix() throws IOException {
        //Creación del grafo de la universidad
        SimpleWeightedGraph<String, DefaultWeightedEdge> uni = createGraph();
        System.out.println(uni.toString());

        //Creación de las listas para almacenar las personas y las cajas
        List<Element> people = new ArrayList<>();
        List<Element> boxes = new ArrayList<>();

        //Ubicación de las personas y las cajas
        people.add(new Element("Jorge", "A"));
        people.add(new Element("Dayo", "B"));
        boxes.add(new Element("Caja 1", "E"));
        boxes.add(new Element("Caja 2", "K"));
        boxes.add(new Element("Caja 3", "I"));
        boxes.add(new Element("Caja 4", "C"));

        /*Inicialización de la lista en la que quedarán las rutas que deberá
        tomar cada persona*/
        List<List<GraphPath<String, DefaultWeightedEdge>>> routes = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            routes.add(new ArrayList<>());
        }
        /*Creación e instancia del método para ejecutar el algoritmo de 
        Floyd Warshall*/
        FloydWarshallShortestPaths<String, DefaultWeightedEdge> fw 
                = new FloydWarshallShortestPaths<>(uni);
        while (boxes.size() > 0) {
            double shortest[][] = new double[people.size()][boxes.size()];

            for (int i = 0; i < people.size(); i++) {
                for (int j = 0; j < boxes.size(); j++) {
                    shortest[i][j] = fw.getPathWeight(people.get(i).location,
                            boxes.get(j).location);
                }
            }

            System.out.println("Personas: " + people.toString());
            System.out.println("Cajas: " + boxes.toString());
            System.out.println("\n* Matriz de caminos más cortos *");
            Utilities.printMatrix(shortest);

            List<Integer> destination = new ArrayList<>();
            for (int i = 0; i < shortest.length; i++) {
                double min = Integer.MAX_VALUE;
                int ind = -1;
                for (int j = 0; j < shortest[0].length; j++) {
                    if (shortest[i][j] < min) {
                        min = shortest[i][j];
                        ind = j;
                    }
                }
                destination.add(ind);
            }
            System.out.println("Destinos: " + destination.toString());

            for (int i = 0; i < people.size(); i++) {
                GraphPath<String, DefaultWeightedEdge> path
                        = fw.getPath(people.get(i).location, boxes.get(destination.get(i)).location);
                routes.get(i).add(path);
                people.get(i).location = boxes.get(destination.get(i)).location;
            }
            Collections.sort(destination, Collections.reverseOrder());
            for (int i : destination) {
                boxes.remove(i);
            }
        }
        System.out.println("\nRESULTADOS");
        for (int i = 0; i < routes.size(); i++) {
            System.out.println("Ruta que tomará " + people.get(i).name);
            System.out.println(routes.get(i).toString());
        }

    }
   

    private static SimpleWeightedGraph<String, DefaultWeightedEdge> createGraph() throws FileNotFoundException, IOException {
        //Declaración e instancia del grafo
        SimpleWeightedGraph<String, DefaultWeightedEdge> g
                = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        //Añadiendo los nodos (lugares de la universidad)
       
         try (CSVReader reader = new CSVReader(new FileReader("Nodos.csv"))) {
            String[] nextLine;
            
            while ((nextLine = reader.readNext()) != null) {
                
                for (String e: nextLine) {
                    String d[] = e.split(";");
                    //System.out.format("%s ", d[0]);
                   
                    g.addVertex(d[0]);
                }
            }
        }
         try (CSVReader reader = new CSVReader(new FileReader("Vertices.csv"))) {
            String[] nextLine;
            
            while ((nextLine = reader.readNext()) != null) {
                
                for (String e: nextLine) {
                    String f[] = e.split(";");
                    g.setEdgeWeight(g.addEdge(f[0], f[1]), Double.parseDouble(f[2]));
                    
                }
            }
        
        }

        //Añadiendo las aristas (rutas entre los lugares) con sus pesos
       /* g.setEdgeWeight(g.addEdge("A", "B"), 1);
        g.setEdgeWeight(g.addEdge("B", "C"), 1);
        g.setEdgeWeight(g.addEdge("C", "D"), 1);
        g.setEdgeWeight(g.addEdge("D", "E"), 1);
        g.setEdgeWeight(g.addEdge("E", "F"), 1);
        g.setEdgeWeight(g.addEdge("G", "I"), 1);
        g.setEdgeWeight(g.addEdge("I", "K"), 1);
        g.setEdgeWeight(g.addEdge("K", "A"), 1);
        g.setEdgeWeight(g.addEdge("B", "D"), 1);*/

        return g;
    
    }
}
