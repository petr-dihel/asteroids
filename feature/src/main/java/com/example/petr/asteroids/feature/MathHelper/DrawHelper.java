package com.example.petr.asteroids.feature.MathHelper;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.petr.asteroids.feature.Classes.PositionVector;

public class DrawHelper {

    public static void DrawLine(Canvas canvas, PositionVector firstPoint, PositionVector secondPoint, Paint paint) {
        canvas.drawLine(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(), paint);
    }

}
