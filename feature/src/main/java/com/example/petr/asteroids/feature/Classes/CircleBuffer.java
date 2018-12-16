package com.example.petr.asteroids.feature.Classes;

import android.util.Log;

public class CircleBuffer {

    private double[] buffer;

    private int current = -1;

    private boolean isFull = false;

    public CircleBuffer(int size) {
        this.buffer = new double[size];
        this.current = -1;
    }

    public void add(double value) {
        if (++this.current == this.buffer.length) {
            this.current = 0;
            isFull = true;
        }
        this.buffer[current] = value;
    }

    public double getAvg() {
        double sum = 0;
        for (double value : buffer) {
            sum += value;
        }

        return sum/buffer.length;
    }

    public boolean isFull() {
        return isFull;
    }
}
