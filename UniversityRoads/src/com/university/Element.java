package com.university;

public class Element {
    /*
    Clase para representar un elemento, sea persona o caja en el mapa. Consta
    de un nombre y una ubicación, donde esta última debe coincidir con algún
    nodo definido en el grafo.
    */
    public String name;
    public String location;

    public Element(String name, String location) {
        this.name = name;
        this.location = location;
    }    
    
    @Override
    public String toString(){
        return name + " {" + location + "}";
    }
}