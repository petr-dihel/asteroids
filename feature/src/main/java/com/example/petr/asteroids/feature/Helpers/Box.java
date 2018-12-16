package com.example.petr.asteroids.feature.Helpers;

import com.example.petr.asteroids.feature.Classes.PositionVector;

import java.util.ArrayList;

public class Box {

    private PositionVector positionVector;

    private double width;

    public Box(PositionVector positionVector, double width) {
        this.positionVector = positionVector;
        this.width = width;
    }

    public ArrayList<Line> getLines() {
        ArrayList<Line> list = new ArrayList<>();
        PositionVector secondVector = new PositionVector(this.positionVector.getX() + this.width, this.positionVector.getY());
        PositionVector thirdVector = new PositionVector(this.positionVector.getX() + this.width, this.positionVector.getY() + this.width);
        PositionVector fourthVector = new PositionVector(this.positionVector.getX(), this.positionVector.getY() + this.width);
        list.add(new Line(this.positionVector, secondVector));
        list.add(new Line(secondVector, thirdVector));
        list.add(new Line(thirdVector, fourthVector));
        list.add(new Line(fourthVector, this.positionVector));
        return list;

    }

    public boolean isColliding(Box box) {
        double firstX, secondX, firstY, secondY;
        if (this.positionVector.getX() > box.positionVector.getX()) {
             firstX = this.positionVector.getX();
             secondX = box.positionVector.getX();
        } else {
             firstX = box.positionVector.getX();
             secondX = this.positionVector.getX();
        }

        double length = firstX - secondX;
        double halfWidth = this.width/2;
        double halfWidthBox = box.width/2;
        double gapBetweenBoxes = length - halfWidth - halfWidthBox;

        if (this.positionVector.getY() > box.positionVector.getY()) {
             firstY = this.positionVector.getY();
             secondY = box.positionVector.getY();
        } else {
             firstY = box.positionVector.getY();
             secondY = this.positionVector.getY();
        }

        double length2 = firstY - secondY;
        double halfWidth2 = this.width/2;
        double halfWidthBox2 = box.width/2;
        double gapBetweenBoxes2 = length2 - halfWidth2 - halfWidthBox2;

        return (gapBetweenBoxes <= 0 && gapBetweenBoxes2 <= 0);
    }
}
