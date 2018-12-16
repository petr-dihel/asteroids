package com.example.petr.asteroids.feature.Helpers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.petr.asteroids.feature.Classes.PositionVector;

public class Line {

    public PositionVector startVector;

    public PositionVector endVector;

    public Line(PositionVector startVector, PositionVector endVector) {
        this.startVector = startVector;
        this.endVector = endVector;
    }

    public boolean intersect(Line line, Canvas canvas) {
        double det, gamma, lambda;
        Paint p1 = new Paint();
        p1.setStrokeWidth(10);
        p1.setColor(Color.RED);
        DrawHelper.DrawLine(canvas, this.startVector, this.endVector, p1);
        p1.setColor(Color.CYAN);

        DrawHelper.DrawLine(canvas, line.startVector, line.endVector, p1);
        //this.drawLine(ctx, "green");
        //line.drawLine(ctx, "red");
        det = (this.endVector.getX() - this.startVector.getX())
                * (line.endVector.getY() - line.startVector.getY())
                - (line.endVector.getX() - line.startVector.getX())
                * (this.endVector.getY() - this.startVector.getY());
        if (det == 0) {
            return false;
        } else {
            lambda = (
                    (line.endVector.getY() - line.startVector.getY())
                            * (line.endVector.getX() - this.startVector.getX())
                            + (line.startVector.getX() - line.endVector.getX())
                            * (line.endVector.getY() - this.startVector.getY())
            ) / det;
            gamma = (
                    (this.startVector.getY()- this.endVector.getY())
                            * (line.endVector.getX() - this.startVector.getX())
                            + (this.endVector.getX() - this.startVector.getX())
                            * (line.endVector.getY() - this.startVector.getY())
            ) / det;
            return (0 < lambda && lambda < 1) && (0 < gamma && gamma < 1);
        }
    }
}
