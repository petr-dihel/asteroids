package com.example.petr.asteroids.feature.MathHelper;

import com.example.petr.asteroids.feature.Classes.PositionVector;

public class MathHelper {

    public static PositionVector getVectorByLengthAndAngle(PositionVector start, int angle, int length) {
        float x = start.getX() + (float)(Math.cos(Math.toRadians(angle)) * length);
        float y = start.getY() + (float)(Math.sin(Math.toRadians(angle)) * length);
        return new PositionVector(x, y);
    }

}
