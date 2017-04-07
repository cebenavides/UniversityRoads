package com.university;

import java.util.List;
import org.jgrapht.graph.*;
import org.jgrapht.alg.shortestpath.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class UniversityRoads {

    public static void main(String[] args) {
        computeMatrix();    
    }
    
    private static void computeMatrix() {
        SimpleWeightedGraph<String, DefaultWeightedEdge> uni = createGraph();
        System.out.println(uni.toString());       
        
        List<String> people = new ArrayList<>();
        List<String> boxes = new ArrayList<>();
        people.add("A");
        people.add("B");
        boxes.add("E");
        boxes.add("K");
        boxes.add("I");
        
        Set<String> vertices = uni.vertexSet();
        Set<DefaultWeightedEdge> edges = uni.edgeSet();        
        List<String> places = new ArrayList<>(vertices);
        
        FloydWarshallShortestPaths<String, DefaultWeightedEdge> fw =
                new FloydWarshallShortestPaths<>(uni);
        
        int adj[][] = new int[places.size()][places.size()];
        double shortest[][] = new double[people.size()][boxes.size()];
        for (DefaultWeightedEdge conn : edges) {            
            int ind1 = places.indexOf(uni.getEdgeSource(conn));
            int ind2 = places.indexOf(uni.getEdgeTarget(conn));
            adj[ind1][ind2] = adj[ind2][ind1] = 1;
        }        
        for (int i = 0; i < shortest.length; i++) {
            for (int j = 0; j < shortest[0].length; j++) {
                shortest[i][j] = fw.getPathWeight(people.get(i), boxes.get(j));
            }
        }
        System.out.println("* Matriz de adyacencia *");
        printMatrix(adj);        
        System.out.println("Personas: " + people.toString());
        System.out.println("Cajas: " + boxes.toString());
        System.out.println("\n* Matriz de caminos más cortos *");
        printMatrix(shortest);
        
        List<List<Double>> distances = new ArrayList<>();
        for (int i = 0; i < shortest.length; i++) {
            double vec[] = shortest[i];
            Arrays.sort(vec);
            System.out.println(Arrays.toString(vec));
        }
    }
    
    public static void printMatrix(int[][] m){
        try{
            int rows = m.length;
            int columns = m[0].length;
            String str = "|\t";

            for(int i=0;i<rows;i++){
                for(int j=0;j<columns;j++){
                    str += m[i][j] + "\t";
                }

                System.out.println(str + "|");
                str = "|\t";
            }

        }catch(Exception e){System.out.println("Matrix is empty!!");}
    }
    
    public static void printMatrix(double[][] m){
        try{
            int rows = m.length;
            int columns = m[0].length;
            String str = "|\t";

            for(int i=0;i<rows;i++){
                for(int j=0;j<columns;j++){
                    str += m[i][j] + "\t";
                }

                System.out.println(str + "|");
                str = "|\t";
            }

        }catch(Exception e){System.out.println("Matrix is empty!!");}
    }
    
    private static SimpleWeightedGraph<String, DefaultWeightedEdge> createGraph() {
        SimpleWeightedGraph<String, DefaultWeightedEdge> g =
            new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        g.addVertex("E");
        g.addVertex("F");
        g.addVertex("G");
        g.addVertex("I");
        g.addVertex("K");
        
        DefaultWeightedEdge e1 = g.addEdge("A", "B");
        g.setEdgeWeight(e1, 1);
        DefaultWeightedEdge e2 = g.addEdge("B", "C");
        g.setEdgeWeight(e2, 1);
        DefaultWeightedEdge e3 = g.addEdge("C", "D");
        g.setEdgeWeight(e3, 1);
        DefaultWeightedEdge e4 = g.addEdge("D", "E");
        g.setEdgeWeight(e4, 1);
        DefaultWeightedEdge e5 = g.addEdge("E", "F");
        g.setEdgeWeight(e5, 1);
        DefaultWeightedEdge e6 = g.addEdge("G", "I");
        g.setEdgeWeight(e6, 1);
        DefaultWeightedEdge e7 = g.addEdge("I", "K");
        g.setEdgeWeight(e7, 1);
        DefaultWeightedEdge e8 = g.addEdge("K", "A");
        g.setEdgeWeight(e8, 1);
        return g;
    }
    
}
