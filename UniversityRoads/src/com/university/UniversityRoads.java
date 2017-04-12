package com.university;

import java.util.List;
import org.jgrapht.graph.*;
import org.jgrapht.alg.shortestpath.*;
import java.util.ArrayList;
import java.util.Collections;
import org.jgrapht.*;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

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
        List<Element> peopleDP = new ArrayList<>(); 
        List<Element> boxes = new ArrayList<>();

        //Ubicación de las personas y las cajas
        people.add(new Element("Jorge", "A"));
        people.add(new Element("Dayan", "E"));
        people.add(new Element("Mike", "B"));
        boxes.add(new Element("Caja 1", "C"));
    //    boxes.add(new Element("Caja 1", "E"));
        
       
        boxes.add(new Element("Caja 2", "D"));
//        boxes.add(new Element("Caja 3", "G"));
//        boxes.add(new Element("Caja 4", "C"));

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
            double shortest[][] = new double[2][2];
            List<List<Distance>> rdist = new ArrayList<>();
            for (int i = 0; i < people.size(); i++) {
                rdist.add(new ArrayList<>());
            }
            for (int i = 0; i < people.size(); i++) {
                for (int j = 0; j < boxes.size(); j++) {
                    rdist.get(i).add(new Distance(fw.getPathWeight(people.get(i).location,
                            boxes.get(j).location),j,boxes.get(j).location));
                }
            }

            System.out.println("Personas: " + people.toString());
            System.out.println("Cajas: " + boxes.toString());
            System.out.println("\n* Matriz de caminos más cortos *");
            //Utilities.printMatrix(shortest);
            System.out.println(rdist.toString());
            
            
            for (int i = 0; i < rdist.size(); i++) {
                rdist.get(i).sort(Comparator.comparing(Distance::distance));
                //rdist.sort((o1, o2) -> o1.distance().compareTo(o2.distance()));
            }
            System.out.println(rdist.toString());
            List<Integer> destination = new ArrayList<>();
            List<String> desti = new ArrayList<>();
            List<Double> camino = new ArrayList<>();
            List<Integer> copydestine = new ArrayList<>();
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < rdist.size(); i++) {
                destination.add(rdist.get(i).get(0).location);
                indices.add(0); 
                desti.add(rdist.get(i).get(0).word);
                camino.add(0.0);
            }
            System.out.println("Destinos: " + destination.toString());
            int k=4;
            int c=0;

            for (int i = 0; i < destination.size()-1; i++) {
                reinicio:
                for (int j = i+1; j < destination.size(); j++) {
                    
                    if (destination.get(i) == destination.get(j)) {
                        
                            Distance dist1 = rdist.get(i).get(indices.get(i));
                            Distance dist2 = rdist.get(j).get(indices.get(j));
                            
                            if ((dist1.distance == dist2.distance) && 
                                    (((1+indices.get(i))<boxes.size()) || ((indices.get(j)+1)<boxes.size()))) {
                            
                            Distance dist11 = rdist.get(i).get(indices.get(i) + 1);
                            Distance dist22 = rdist.get(j).get(indices.get(j) + 1);
                            
                                if (dist11.distance > dist22.distance) {
                                    destination.set(j, dist22.location);
                                    desti.set(j,dist22.word);
                                    indices.set(j, indices.get(j) + 1);
                                    k=0;
                                    i=-1;
                                    break reinicio;
                                }else{
                                    destination.set(i, dist11.location);
                                    indices.set(i, indices.get(i) + 1);
                                    desti.set(i,dist11.word);
                                    k=1;
                                    i=-1;
                                    break reinicio;
                                }
                            
                            }else if(dist1.distance > dist2.distance){
                                if((indices.get(i)+1)<boxes.size()){
                                Distance dist112 = rdist.get(i).get(indices.get(i)+1);    
                                destination.set(i, dist112.location);
                                desti.set(i,dist112.word);
                                indices.set(i, indices.get(i)+1);
                                }
                                else{                                  
                                  peopleDP.add(people.get(i));
                                  desti.set(i,"DP");
                          //        people.remove(i);
                                System.out.println("Hola mundo cruel"); 
                                
                                }

                                //k=0;
                            
                            }else if (dist1.distance < dist2.distance) {
                                if((indices.get(j)+1)<boxes.size()){
                                Distance dist222 = rdist.get(j).get(indices.get(j)+1);    
                                destination.set(j, dist222.location);
                                desti.set(j,dist222.word);
                                indices.set(j, indices.get(j)+1);                                
                                }
                                else{                                   
                                  peopleDP.add(people.get(j));
                                  desti.set(j,"DP");
                                  //people.remove(j);
                                System.out.println("Hola mundo cruel"); 
                                
                                }
                            }else{
                                peopleDP.add(people.get(j));
                                desti.set(j,"DP");
                            }
                    }                      
                }
            }
        
            System.out.println("Destinos nuevos: " + destination.toString());
            System.out.println("Desti nuevos: " + desti.toString());
            if (people.size()<=boxes.size()){
                System.out.println("PERSONAS <= CAJAS");
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
                if (boxes.size()==0) {
                    for (int i = 0; i < people.size(); i++) {

                        GraphPath<String, DefaultWeightedEdge> path
                                = fw.getPath(people.get(i).location, "DP");
                        routes.get(i).add(path);
                        people.get(i).location = "DP";
                    }                    
                }
            }else{
                int caso;
                System.out.println("PERSONAS > CAJAS");            
                 for (int i = 0; i < people.size(); i++) {
                     for (int j = 0; j < peopleDP.size(); j++) {
                         if (people.get(i).name == peopleDP.get(j).name)  {
                        GraphPath<String, DefaultWeightedEdge> path
                            = fw.getPath(people.get(i).location, "DP");
                        routes.get(i).add(path);
                        people.get(i).location = "DP";    
                         }
                         else{
                        GraphPath<String, DefaultWeightedEdge> path
                            = fw.getPath(people.get(i).location, boxes.get(destination.get(i)).location);
                        routes.get(i).add(path);
                        people.get(i).location = boxes.get(destination.get(i)).location; 
                             copydestine.add(destination.get(i));   
                            path = fw.getPath(people.get(i).location, "DP");
                            routes.get(i).add(path);
                            people.get(i).location = "DP";    
                         }
                     }
                }  
                Collections.sort(copydestine, Collections.reverseOrder());
                for (int i : copydestine) {
                    boxes.remove(i);
                }
            }
        }
            List<Double> camino = new ArrayList<>();
            for (int i = 0; i < routes.size(); i++) {
                camino.add(0.0);
            }
        System.out.println("\nRESULTADOS");
        double c;
        for (int i = 0; i < routes.size(); i++) {
            System.out.println("Ruta que tomará " + people.get(i).name);
            System.out.println(routes.get(i).toString());
            for (int j = 0; j < routes.get(i).size(); j++) {
                c=routes.get(i).get(j).getWeight();
                camino.set(i,c+camino.get(i));
            }
            System.out.println(camino.get(i).toString());
        }

    }

    private static SimpleWeightedGraph<String, DefaultWeightedEdge> createGraph() throws IOException {
        //Declaración e instancia del grafo
        SimpleWeightedGraph<String, DefaultWeightedEdge> g
                = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        //Añadiendo los nodos (lugares de la universidad)
        try (CSVReader reador = new CSVReader(new FileReader("nodos.csv"))) {
            String[] nextLine;
            while ((nextLine = reador.readNext()) != null) {
                
                for (String e: nextLine) {
                    System.out.format("%s ", e);
                }
            }
        }
        
        
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        g.addVertex("DP");
        g.addVertex("E");
        g.addVertex("F");
        g.addVertex("G");
/*
        g.addVertex("E");
        g.addVertex("F");
        g.addVertex("G");
        g.addVertex("I");
        g.addVertex("K");
        g.addVertex("H");
        g.addVertex("DP");
  */        
        //Añadiendo las aristas (rutas entre los lugares) con sus pesos
        g.setEdgeWeight(g.addEdge("A", "B"), 1.1);
        g.setEdgeWeight(g.addEdge("A", "C"), 1.2);
        g.setEdgeWeight(g.addEdge("A", "DP"), 10);
        g.setEdgeWeight(g.addEdge("B", "D"), 1.3);
        g.setEdgeWeight(g.addEdge("B", "C"), 1.4);
        g.setEdgeWeight(g.addEdge("C", "E"), 1.5);
        g.setEdgeWeight(g.addEdge("D", "E"), 2);

        return g;
    }

}
