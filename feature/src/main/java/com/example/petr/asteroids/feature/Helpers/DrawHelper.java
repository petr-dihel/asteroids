package com.example.petr.asteroids.feature.Helpers;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.petr.asteroids.feature.Classes.PositionVector;

public class DrawHelper {

    public static void DrawLine(Canvas canvas, PositionVector firstPoint, PositionVector secondPoint, Paint paint) {
        canvas.drawLine((float)firstPoint.getX(), (float)firstPoint.getY(), (float)secondPoint.getX(), (float)secondPoint.getY(), paint);
    }



}
