package com.example.petr.asteroids.feature;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;

public class MainActivity extends Activity {

    SensorManager sensorManager;

    View gameView;

    public Handler frame = new Handler();

    public boolean running = true;

    private static final int FRAME_RATE = 40; //25 frames per se

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        this.gameView = new GameView(this.getBaseContext(), sensorManager);
        setContentView(gameView);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                initGfx();
            }
        }, 1000);

    }

    @Override
    protected void onResume() {
        super.onResume();

        SensorEventListener accelometerSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                GameView.accelerometerEvent = sensorEvent;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        Sensor accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        sensorManager.registerListener(
                accelometerSensorListener,
                accSensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );

        SensorEventListener orientationListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                GameView.orientationEvent = sensorEvent;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(
                orientationListener,
                orientationSensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            running = true;
            frame.removeCallbacks(frameUpdate);
            gameView.invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }

    };

    synchronized public void initGfx() {
        frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    @Override
    public void finish(){
        frame.removeCallbacks(frameUpdate);
        super.finish();
    }
}
