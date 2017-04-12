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
        
        try (CSVReader reador = new CSVReader(new FileReader("Configuración Inicial.csv"))) {
            String[] nextLine;
            
            int salte=0;
            while ((nextLine = reador.readNext()) != null) {
                
                for (String e: nextLine) {
                    if (salte ==0){
                    
                    }else{
                        
                            String d[] = e.split(";");
                            if(d[5] != null && !d[5].equals(".")){
                                                        
                                people.add(new Element(d[4],d[5]));
                            }else{
                            } 
                            if(d[6] != null && !d[6].equals(".")){
                                
                                 boxes.add(new Element(d[6], d[7]));
                            }else{
                            } 
                    }
                    salte++;
                }
              
            }
        }
        //people.add(new Element("Jorge", "A"));
        //people.add(new Element("Mike", "E"));
        //people.add(new Element("Dayo", "C"));
       //boxes.add(new Element("Caja 1", "B"));
    //    boxes.add(new Element("Caja 1", "E"));
        
       
        //boxes.add(new Element("Caja 2", "D"));
        //boxes.add(new Element("Caja 3", "G"));
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
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < rdist.size(); i++) {
                destination.add(rdist.get(i).get(0).location);
                indices.add(0);                
            }
            System.out.println("Destinos: " + destination.toString());
            int k=4;
            int c=0;
            for (int i = 0; i < destination.size()-1; i++) {
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
                                    indices.set(j, indices.get(j) + 1);
                                    k=0;
                                }else{
                                    destination.set(i, dist11.location);
                                    indices.set(i, indices.get(i) + 1);
                                    k=1;
                                }
                            
                            }else if(dist1.distance > dist2.distance){
                                if((indices.get(i)+1)<boxes.size()){
                                Distance dist112 = rdist.get(i).get(indices.get(i)+1);    
                                destination.set(i, dist112.location);
                                indices.set(i, indices.get(i)+1);
                                }
                                else{                                  
                                  peopleDP.add(people.get(i));
                          //        people.remove(i);
                                System.out.println("Hola mundo cruel"); 
                                
                                }

                                //k=0;
                            
                            }else if (dist1.distance < dist2.distance) {
                                if((indices.get(j)+1)<boxes.size()){
                                Distance dist222 = rdist.get(j).get(indices.get(j)+1);    
                                destination.set(j, dist222.location);
                                indices.set(j, indices.get(j)+1);                                
                                }
                                else{                                   
                                  peopleDP.add(people.get(j));
                                  //people.remove(j);
                                System.out.println("Hola mundo cruel"); 
                                
                                }
                            }else{
                                peopleDP.add(people.get(j));
                            }
                    }                      
                }
            }
        
            System.out.println("Destinos nuevos: " + destination.toString());
            
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
                             caso = destination.get(i);
                            boxes.remove(caso);       
                            path = fw.getPath(people.get(i).location, "DP");
                            routes.get(i).add(path);
                            people.get(i).location = "DP";    
                         }
                     }
                }                
            }
        }
        System.out.println("\nRESULTADOS");
        for (int i = 0; i < routes.size(); i++) {
            System.out.println("Ruta que tomará " + people.get(i).name);
            System.out.println(routes.get(i).toString());
        }

    }

    private static SimpleWeightedGraph<String, DefaultWeightedEdge> createGraph() throws IOException {
        //Declaración e instancia del grafo
        SimpleWeightedGraph<String, DefaultWeightedEdge> g
                = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        //Añadiendo los nodos (lugares de la universidad)
        try (CSVReader reador = new CSVReader(new FileReader("Configuración Inicial.csv"))) {
            String[] nextLine;
            int salte=0;
            while ((nextLine = reador.readNext()) != null) {
                
                for (String e: nextLine) {
                    if (salte ==0){
                    
                    }else{
                        
                            String d[] = e.split(";");
                            if(d[0] != null && !d[0].equals(".")){
                                //System.out.format("%s ", d[0]);
                                g.addVertex(d[0]);
                            }else{
                            } 
                    }
                    salte++;
                }
              
            }
        }
         try (CSVReader reador = new CSVReader(new FileReader("Configuración Inicial.csv"))) {
            String[] nextLine;
            int salte=0;
            
            while ((nextLine = reador.readNext()) != null) {
                
                for (String e: nextLine) {
                    if (salte ==0){
                    
                    }else{
                        //System.out.println("Entre");
                            String d[] = e.split(";");
                            if(d[2] != null && !d[2].equals(".")){
                                //System.out.format("%s ", d[2]);
                                g.setEdgeWeight(g.addEdge(d[1], d[2]),Double.parseDouble(d[3]));
                            }else{
                            } 
                    }
                    salte++;
                }
              
            }
        }
        
        
        /*g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");

        g.addVertex("E");
        g.addVertex("F");
        g.addVertex("G");
        g.addVertex("I");
        g.addVertex("K");
        g.addVertex("H");
        g.addVertex("DP");
          
        //Añadiendo las aristas (rutas entre los lugares) con sus pesos
        g.setEdgeWeight(g.addEdge("A", "B"), 1);
        g.setEdgeWeight(g.addEdge("B", "C"), 1);
        g.setEdgeWeight(g.addEdge("C", "D"), 1);
        
        g.setEdgeWeight(g.addEdge("D", "E"), 1);
        g.setEdgeWeight(g.addEdge("E", "F"), 1);
        g.setEdgeWeight(g.addEdge("F", "G"), 1);
        g.setEdgeWeight(g.addEdge("G", "H"), 1);
        g.setEdgeWeight(g.addEdge("H", "I"), 1);
        g.setEdgeWeight(g.addEdge("I", "A"), 1);
        g.setEdgeWeight(g.addEdge("A", "DP"), 1);*/

        return g;
    }

}
