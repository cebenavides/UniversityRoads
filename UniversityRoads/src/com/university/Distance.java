package com.university;

public class Distance {
    public double distance;
    public int location;
    public String word;

    public Distance(double distance, int location, String word) {
        this.distance = distance;
        this.location = location;
        this.word = word;
    }

    public double distance() {
        return distance;
    }

    
    
    @Override
    public String toString(){
        return word + " - " + distance;
    }
    
}
