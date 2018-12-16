package com.example.petr.asteroids.feature.Classes;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.petr.asteroids.feature.Helpers.Box;
import com.example.petr.asteroids.feature.Helpers.Line;
import com.example.petr.asteroids.feature.Interfaces.IGameParticle;

import java.util.ArrayList;

public class Particle implements IGameParticle {

    private PositionVector positionVector;

    private PositionVector speedVector;

    private double width;

    private double lifeTime;

    private int color;

    public Particle (PositionVector vector, PositionVector speedVector, double width, double lifeTime, int color) {
        this.positionVector = vector;
        this.speedVector = speedVector;
        this.width = width;
        this.lifeTime = lifeTime;
        this.color = color;
    }
    public boolean isLifetime() {
        return this.lifeTime > 0;
    }

    public void DrawOnCanvas(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(this.color);
        paint.setStrokeWidth(5);

        canvas.drawRect(
                (float)this.positionVector.getX(),
                (float)this.positionVector.getY(),
                (float)(this.positionVector.getX()+this.width),
                (float)(this.positionVector.getY()+this.width),
            paint
        );
    }

    @Override
    public void GetBounds() {

    }

    @Override
    public void Move(int screenWidth, int screenHeigth) {
        this.lifeTime--;
        this.positionVector.addToX(this.speedVector.getX());
        this.positionVector.addToY(this.speedVector.getY());
    }

    @Override
    public Box getBox() {
        return new Box(this.positionVector, this.width);
    }

    @Override
    public ArrayList<Line> getLines() {
        return new Box(this.positionVector, this.width).getLines();
    }
}
