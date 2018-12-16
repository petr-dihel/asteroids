package com.example.petr.asteroids.feature.Classes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.SyncStateContract;
import android.util.Log;

import com.example.petr.asteroids.feature.Helpers.Box;
import com.example.petr.asteroids.feature.Helpers.Constants;
import com.example.petr.asteroids.feature.Helpers.DrawHelper;
import com.example.petr.asteroids.feature.Helpers.Line;
import com.example.petr.asteroids.feature.Helpers.MathHelper;
import com.example.petr.asteroids.feature.Interfaces.IGameObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Asteroid implements IGameObject {

    public PositionVector positionVector;

    private PositionVector speedVector;

    private int length = 0;

    private int countOfVectors = 0;

    private ArrayList<PositionVector> vectors;

    public int randomDefaultLengthLimit;

    public Asteroid(PositionVector positionVector, PositionVector speedVecotr, int length) {
        this.positionVector = positionVector;
        this.speedVector = speedVecotr;
        this.length = length;
        vectors = new ArrayList<PositionVector>();
        initAsteroid(length);
    }

    private void initAsteroid(int length) {
        this.countOfVectors = (int)Math.floor(Math.random()
                *(Constants.LIMIT_OF_ASTEROID_MAX_VECTORS
                -Constants.LIMIT_OF_MIN_ASTEROID_VECTORS+1)
                +Constants.LIMIT_OF_MIN_ASTEROID_VECTORS);
        double degree = 360/this.countOfVectors;
        if (this.length != 0) {
            this.randomDefaultLengthLimit = length;
        } else {
            this.randomDefaultLengthLimit = (int)Math.floor((Math.random()
                    *Constants.LIMIT_OF_MAX_ASTEROID_LENGTH
                    +Constants.LIMIT_OF_MIN_ASTEROID_LENGTH)+1
            );
        }

        int randomLength = 0;
        for(int i = 0; i < this.countOfVectors; i++) {
            if (randomLength == 0) {
                randomLength = (int)Math.floor((Math.random()*(this.randomDefaultLengthLimit)+(this.randomDefaultLengthLimit/3))+1);
            } else {
                randomLength = (int)Math.floor((Math.random()*(randomLength+randomLength/3)+(randomLength*2/3)));
            }
            if (this.randomDefaultLengthLimit < randomLength) {
                randomLength = this.randomDefaultLengthLimit*2/3;
            }
            this.vectors.add(MathHelper.getVectorByLengthAndAngle(
                    new PositionVector(0,0),
                    (int)degree*(i+1),
                    randomLength
            ));
        }
    }

    @Override
    public void DrawOnCanvas(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        for (int i = 0; i < vectors.size(); i++) {
            int nextVector = (i+1 == vectors.size() ? 0 : i+1);
            canvas.drawLine(
                    (float)(vectors.get(i).getX() + this.positionVector.getX()),
                    (float)(vectors.get(i).getY() + this.positionVector.getY()),
                    (float)(vectors.get(nextVector).getX() + this.positionVector.getX()),
                    (float)(vectors.get(nextVector).getY() + this.positionVector.getY()),
                    paint);
        }
    }

    public ArrayList<Line> getLines() {
        ArrayList<Line> list = new ArrayList<>();
        for (int i = 0; i < vectors.size(); i++) {
            int nextVector = (i+1 == vectors.size() ? 0 : i+1);
            list.add(new Line(
                        new PositionVector(
                                (float)(vectors.get(i).getX() + this.positionVector.getX()),
                                (float)(vectors.get(i).getY() + this.positionVector.getY())
                        ),
                        new PositionVector(
                                (float)(vectors.get(nextVector).getX() + this.positionVector.getX()),
                                (float)(vectors.get(nextVector).getY() + this.positionVector.getY())
                        )
                    )
            );
        }
        return list;
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

    public Box getBox() {
        return new Box(this.positionVector,this.randomDefaultLengthLimit*2);
    }
}
