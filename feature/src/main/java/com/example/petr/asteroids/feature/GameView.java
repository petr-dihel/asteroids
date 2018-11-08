package com.example.petr.asteroids.feature;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.petr.asteroids.feature.Classes.Ship;
import com.example.petr.asteroids.feature.Interfaces.IGameObject;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    ArrayList<IGameObject> gameObjects;

    SensorManager sensorManager;

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
        this.gameObjects.add(new Ship(250, 500));
        this.gyroscopeSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        // Create a listener


    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;


        // Draw the example drawable on top of the text.
        /*
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }*/

        Log.d("MyLog:", "onDraw");
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
