package com.example.petr.asteroids.feature.Classes;

public class PositionVector {

    private double x,y;

    public PositionVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void addToX(double add) {
        this.x += add;
    }

    public void addToY(double add) {
        this.y += add;
    }
}
