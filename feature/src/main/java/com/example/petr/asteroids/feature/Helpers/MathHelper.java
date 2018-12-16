package com.example.petr.asteroids.feature.Helpers;

import com.example.petr.asteroids.feature.Classes.PositionVector;

public class MathHelper {

    public static PositionVector getVectorByLengthAndAngle(PositionVector start, int angle, int length) {
        float x = (float)start.getX() + (float)(Math.cos(Math.toRadians(angle)) * length);
        float y = (float)start.getY() + (float)(Math.sin(Math.toRadians(angle)) * length);
        return new PositionVector(x, y);
    }

    public static boolean isInCircle(double wantedR, double x, double y) {
        double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return (r <= wantedR);
    }

    public static PositionVector getRandomVector() {
        double randX = Math.floor(Math.random()*6+1);
        double randY = Math.floor(Math.random()*6+1);
        if (randX > 3) {
            randX *= -1;
        }
        if (randY > 3) {
            randY *= -1;
        }
        return new PositionVector(randX, randY);
    }

    public static PositionVector getParticleSpeedVectorFromAngle(double angle) {
        double x = Math.cos(Math.toRadians(angle));
        double y = Math.sin(Math.toRadians(angle));
        return new PositionVector(x, y);

    }

    public static PositionVector getRandSpeedVector(double minimumLimit, double maximumLimit) {
        double range = 2*(maximumLimit-minimumLimit+1);
        double randX = (Math.floor(Math.random()*range))+minimumLimit+1;
        double randY = (Math.floor(Math.random()*range))+minimumLimit+1;
        if (randX > maximumLimit/2) {
            randX *= -1;
        }
        if (randY > maximumLimit/2) {
            randY *= -1;
        }
        return new PositionVector(randX, randY);
    }
}
