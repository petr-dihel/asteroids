package com.example.petr.asteroids.feature.Classes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.petr.asteroids.feature.Interfaces.IGameObject;
import com.example.petr.asteroids.feature.MathHelper.DrawHelper;
import com.example.petr.asteroids.feature.MathHelper.MathHelper;

public class Ship implements IGameObject {

    private static final int ANGLE_FOR_TRIANGLE = 20;

    private PositionVector positionVector;

    private int angle = 0;

    private PositionVector speedVector;

    private int wingLength;

    public Ship(int x, int y) {
        this.positionVector = new PositionVector(x, y);
        Log.d("MyLog x:", String.valueOf(x));
        Log.d("MyLog y:", String.valueOf(y));
        this.speedVector = new PositionVector(0,0);
        this.wingLength = 150;
    }

    private void addToAngle(int add) {
        this.angle += add;
        if (this.angle > 360) {
            this.angle -= 360;
        }
        if (this.angle < 0) {
            this.angle += 360;
        }
    }

    @Override
    public void DrawOnCanvas(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setStrokeWidth(5);
        PositionVector secondPoint = MathHelper.getVectorByLengthAndAngle(
                this.positionVector,
                this.ANGLE_FOR_TRIANGLE,
                this.wingLength
        );
        PositionVector thirdPoint = MathHelper.getVectorByLengthAndAngle(
                this.positionVector,
                -this.ANGLE_FOR_TRIANGLE,
                this.wingLength
        );
        DrawHelper.DrawLine(canvas, this.positionVector, secondPoint, paint);
        DrawHelper.DrawLine(canvas, this.positionVector, thirdPoint, paint);
        DrawHelper.DrawLine(canvas, secondPoint, thirdPoint, paint);
        Log.d("MyLog:", "Ship draw");
    }

    @Override
    public void GetBounds() {

    }
}
