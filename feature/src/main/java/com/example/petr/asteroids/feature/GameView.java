package com.example.petr.asteroids.feature;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.petr.asteroids.feature.Classes.Ship;
import com.example.petr.asteroids.feature.Interfaces.IGameObject;

import java.io.Console;
import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    public enum DIRECTION {
        LEFT, RIGHT
    }

    public static SensorEvent gyroscopeEvent;

    public static SensorEvent accelometerEvent;

    public static SensorEvent rotationEvent;

    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private boolean gameRunning = true;

    ArrayList<IGameObject> gameObjects;

    Ship ship;

    SensorManager sensorManager;

    long lastTurnShip = 0;

    Sensor gyroscopeSensor;


    private int screenWidth, screenHeigth;

    public GameView(Context context, SensorManager sensorManager) {
        super(context);
        this.sensorManager = sensorManager;
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.screenHeigth = w;
        this.screenWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void turnShip(DIRECTION direction) {
        if (direction == DIRECTION.RIGHT) {
            this.ship.addToAngle(45);
        } else {
            this.ship.addToAngle(-45);
        }
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GameView, defStyle, 0);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.GameView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.GameView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.GameView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();
        this.gameObjects = new ArrayList<>();
        this.ship = new Ship(250, 500);
        this.gameObjects.add(this.ship);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long currentTime = System.currentTimeMillis();
        /**
         * @// TODO: 12.11.2018 use accelometer instead of gyroscope to have Absolute position not relative
         */
        if (currentTime - lastTurnShip > 200) {
            Log.d("MEINELOG", "Acc[0]" + Float.toString(GameView.accelometerEvent.values[0]));
            Log.d("MEINELOGE0", "Rotation[0]" + Float.toString(GameView.rotationEvent.values[0]));
            Log.d("MEINELOGE1", "Rotation[1]" + Float.toString(GameView.rotationEvent.values[1]));
            Log.d("MEINELOGE2", "Rotation[2]" + Float.toString(GameView.rotationEvent.values[2]));
            Log.d("MEINELOGE3", "Rotation[3]" + Float.toString(GameView.rotationEvent.values[3]));
            if (GameView.accelometerEvent.values[0] > 1.0f) {
                this.turnShip(DIRECTION.LEFT);
            } else if (GameView.accelometerEvent.values[0] < -1.0f) {
                this.turnShip(DIRECTION.RIGHT);
            }
            this.lastTurnShip = currentTime;
        }
        //Log.d("gyroscopeLo", Float.toString(GameView.gyroscopeEvent.values[2]));
        for (IGameObject gameObject : this.gameObjects) {
            gameObject.DrawOnCanvas(canvas);
        }
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }


    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
