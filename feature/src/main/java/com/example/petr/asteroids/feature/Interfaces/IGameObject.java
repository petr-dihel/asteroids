package com.example.petr.asteroids.feature.Interfaces;

import android.graphics.Canvas;

import com.example.petr.asteroids.feature.Helpers.Box;
import com.example.petr.asteroids.feature.Helpers.Line;

import java.util.ArrayList;

public interface IGameObject {

    public void DrawOnCanvas(Canvas canvas);

    public void GetBounds();

    public void Move(int screenWidth, int screenHeigth);

    public Box getBox();

    public ArrayList<Line> getLines();
}
