package com.example.petr.asteroids.feature.Classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.petr.asteroids.feature.Helpers.Box;
import com.example.petr.asteroids.feature.Helpers.Constants;
import com.example.petr.asteroids.feature.Helpers.Line;
import com.example.petr.asteroids.feature.Interfaces.IGameObject;
import com.example.petr.asteroids.feature.Helpers.DrawHelper;
import com.example.petr.asteroids.feature.Helpers.MathHelper;

import java.util.ArrayList;

public class Ship implements IGameObject {

    private static final int ANGLE_FOR_TRIANGLE = 20;

    public PositionVector positionVector;

    public int angle = 0;

    public PositionVector speedVector;

    private int wingLength;

    public Ship(int x, int y) {
        this.positionVector = new PositionVector(x, y);
        this.speedVector = new PositionVector(0,0);
        this.wingLength = 100;
    }

    public void addToAngle(int add) {
        this.angle += add;
        if (this.angle > 360) {
            this.angle -= 360;
        }
        if (this.angle < 0) {
            this.angle += 360;
        }
    }

    public void addSpeed(boolean front) {

        double tmpX;
        double tmpY;
        tmpX = (front ? 1 : -1) * Math.cos(this.angle* (Math.PI / 180))*Constants.SHIP_ACCELERATION_RATE;
        tmpY = (front ? 1 : -1) * Math.sin(this.angle* (Math.PI / 180))*Constants.SHIP_ACCELERATION_RATE;

        if (!MathHelper.isInCircle(Constants.MAX_SHIP_SPEED,
                tmpX + this.speedVector.getX(),
                this.speedVector.getY()
        )) {
            tmpX = 0;
        }

        if (!MathHelper.isInCircle(Constants.MAX_SHIP_SPEED,
                tmpX,
                this.speedVector.getY()+tmpY
        )) {
            tmpY = 0;
        }
        this.speedVector.addToX((float)tmpX);
        this.speedVector.addToY((float)tmpY);
    }

    public ArrayList<Line> getLines() {
        ArrayList<Line> list = new ArrayList<>();
        PositionVector secondPoint = MathHelper.getVectorByLengthAndAngle(
                this.positionVector,
                this.angle + Ship.ANGLE_FOR_TRIANGLE,
                this.wingLength
        );
        PositionVector thirdPoint = MathHelper.getVectorByLengthAndAngle(
                this.positionVector,
                this.angle - Ship.ANGLE_FOR_TRIANGLE,
                this.wingLength
        );
        list.add(new Line( this.positionVector, secondPoint));
        list.add(new Line( this.positionVector, thirdPoint));
        list.add(new Line(secondPoint, thirdPoint));
        return list;
    }

    public Box getBox() {
        return new Box(this.positionVector, this.wingLength);
    }

    @Override
    public void DrawOnCanvas(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setStrokeWidth(5);
        PositionVector secondPoint = MathHelper.getVectorByLengthAndAngle(
                this.positionVector,
                this.angle + Ship.ANGLE_FOR_TRIANGLE,
                this.wingLength
        );
        PositionVector thirdPoint = MathHelper.getVectorByLengthAndAngle(
                this.positionVector,
                this.angle - Ship.ANGLE_FOR_TRIANGLE,
                this.wingLength
        );
        DrawHelper.DrawLine(canvas, this.positionVector, secondPoint, paint);
        DrawHelper.DrawLine(canvas, this.positionVector, thirdPoint, paint);
        DrawHelper.DrawLine(canvas, secondPoint, thirdPoint, paint);
    }

    @Override
    public void GetBounds() {

    }

    @Override
    public void Move(int screenWidth, int screenHeigth) {
        this.positionVector.addToX(speedVector.getX());
        this.positionVector.addToY(speedVector.getY());

        if (this.positionVector.getX() < 0) {
            this.positionVector.addToX(screenWidth);
        } else {
            if (this.positionVector.getX() > screenWidth) {
                this.positionVector.addToX(-screenWidth);
            }
        }
        if (this.positionVector.getY() < 0) {
            this.positionVector.addToY(screenHeigth);
        } else {
            if (this.positionVector.getY() > screenHeigth) {
                this.positionVector.addToY(-screenHeigth);
            }
        }

    }
}
