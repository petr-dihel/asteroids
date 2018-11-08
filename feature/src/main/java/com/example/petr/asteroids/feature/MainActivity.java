package com.example.petr.asteroids.feature;

import android.app.Activity;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.util.Log;
import android.view.Window;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import com.example.petr.asteroids.feature.Classes.Ship;
import com.example.petr.asteroids.feature.Interfaces.IGameObject;

import java.util.ArrayList;

public class MainActivity extends Activity {

    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        setContentView(new GameView(this.getBaseContext(), sensorManager));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        // Register the listener
        sensorManager.registerListener(gyroscopeSensorListener,
                gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
