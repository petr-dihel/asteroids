package com.example.petr.asteroids.feature.Classes;

public class PositionVector {

    private float x,y;

    public PositionVector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void addToX(float add) {
        this.x = add;
    }

    public void addToY(float add) {
        this.y = add;
    }
}
