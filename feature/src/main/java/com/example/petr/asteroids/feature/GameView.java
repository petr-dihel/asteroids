package com.example.petr.asteroids.feature;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.petr.asteroids.feature.Classes.Asteroid;
import com.example.petr.asteroids.feature.Classes.CircleBuffer;
import com.example.petr.asteroids.feature.Classes.Particle;
import com.example.petr.asteroids.feature.Classes.PositionVector;
import com.example.petr.asteroids.feature.Classes.Ship;
import com.example.petr.asteroids.feature.Helpers.Constants;
import com.example.petr.asteroids.feature.Helpers.Line;
import com.example.petr.asteroids.feature.Helpers.MathHelper;
import com.example.petr.asteroids.feature.Interfaces.IGameObject;
import com.example.petr.asteroids.feature.Interfaces.IGameParticle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    public enum DIRECTION {
        LEFT, RIGHT
    }

    public static SensorEvent accelerometerEvent;

    public static SensorEvent orientationEvent;

    private CircleBuffer rotateSensorBuffer = new CircleBuffer(5);
    private CircleBuffer speedSensorBuffer = new CircleBuffer(5);

    private double relativeZeroRotation = 0;
    private double relativeZeroSpeed = 0;

    private boolean calibrated = false;

    private float mExampleDimension = 0;
    private Drawable mExampleDrawable;

    private boolean gameRunning = true;

    ArrayList<IGameObject> gameObjects;

    ArrayList<IGameParticle> gameParticles;

    ArrayList<IGameParticle> shotParticles;

    ArrayList<IGameObject> asteroids;

    Ship ship;

    SensorManager sensorManager;

    long lastAsteroidGenerated = 0;

    long lastShot = 0;

    long startCalibrating = 0;

    long timeAlive = 0;

    private int screenWidth, screenHeigth;

    public GameView(Context context, SensorManager sensorManager) {
        super(context);
        this.sensorManager = sensorManager;
        startCalibrating = System.currentTimeMillis();
        timeAlive = startCalibrating + (5*1000);
        lastShot = timeAlive;
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
        this.screenHeigth = h;
        this.screenWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void addShipParticle(boolean front) {
        int count = (int)(front ? Constants.COUNT_OF_PARTICLES_FRONT_THRUST : Constants.COUNT_OF_PARTICLES_BACK_THRUST);
        int angleAdd = (front ? -180 : 0);
        int speed = (front ? (-1)*Constants.FRONT_THRUST_SPEED : Constants.BACK_THRUST_SPEED);
        int width = (front ? Constants.FRONT_THRUST_MAX_WIDTH : Constants.BACK_THRUST_MAX_WIDTH);
        int color = (front ? Constants.FRONT_THRUST_COLOR : Constants.BACK_THRUST_COLOR);
        int lifetime = (front ? Constants.FRONT_THURST_LIFETIME : Constants.BACK_THURST_LIFETIME);
        for (int i = 0; i <= count; i++ ) {
            PositionVector randVector = MathHelper.getRandomVector();
            randVector.addToX(this.ship.positionVector.getX()+i*randVector.getX());
            randVector.addToY(this.ship.positionVector.getY()+i*randVector.getY());
            PositionVector particleVector = MathHelper.getVectorByLengthAndAngle(
                    randVector,
                    this.ship.angle + angleAdd,
                    Constants.THURST_PATICLE_POSITION
            );
            PositionVector particleSpeedVector = MathHelper.getParticleSpeedVectorFromAngle(this.ship.angle);
            particleSpeedVector.addToX(particleSpeedVector.getX()*speed);
            particleSpeedVector.addToY(particleSpeedVector.getY()*speed);
            double particleWidth = (int)(Math.random() * (width - 1 + 1)) + 1;
            this.gameParticles.add(
                    new Particle(
                            particleVector,
                            particleSpeedVector,
                            particleWidth,
                            lifetime,
                            color
                    )
            );
        }
    }

    private void addSpeed(boolean front) {
        addShipParticle(front);
        this.ship.addSpeed(front);

    }

    private void turnShip(DIRECTION direction) {
        if (direction == DIRECTION.RIGHT) {
            this.ship.addToAngle(5);
        } else {
            this.ship.addToAngle(-5);
        }
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.gameObjects = new ArrayList<>();
        this.gameParticles = new ArrayList<>();
        this.shotParticles = new ArrayList<>();
        this.asteroids = new ArrayList<>();
        this.ship = new Ship(250, 500);
        this.gameObjects.add(this.ship);
    }

    private void generateAsteroid() {
        int randX = (int)Math.floor(Math.random()*this.screenWidth*+0+1);
        int y = this.screenHeigth;
        this.asteroids.add(
                new Asteroid(
                        new PositionVector(randX, y),
                        MathHelper.getRandSpeedVector(Constants.MAX_ASTEROID_SPEED, Constants.MIN_ASTEROID_SPEED),
                        0
                )
        );
    }

    private void gameOver() {
        long currentTime = System.currentTimeMillis();
        timeAlive = (currentTime - timeAlive);
        gameRunning = false;
    }

    private boolean checkAsteroids(Canvas canvas) {
        ArrayList<Line> shipLines = this.ship.getLines();
        for (Iterator<IGameObject> asteroidIterator = this.asteroids.iterator(); asteroidIterator.hasNext(); ) {
            IGameObject asteroid = asteroidIterator.next();
            ArrayList<Line> asteroidLines = asteroid.getLines();
            if (asteroid.getBox().isColliding(this.ship.getBox())) {
                for (Line shipLine : shipLines) {
                    for (Line asteroidLine : asteroidLines) {
                        if (shipLine.intersect(asteroidLine, canvas)) {
                            gameOver();
                            return true;
                        }
                    }
                }
            }
            for (Iterator<IGameParticle> iteratorShots = this.shotParticles.iterator(); iteratorShots.hasNext(); ) {
                IGameParticle particle = iteratorShots.next();
                if (asteroid.getBox().isColliding(particle.getBox())) {
                    ArrayList<Line> shotLines = particle.getLines();
                    for (Line asteroidLine : asteroidLines) {
                        for (Line shotLine : shotLines) {
                            if (shotLine.intersect(asteroidLine,canvas)) {
                                this.explode(asteroid);
                                asteroidIterator.remove();
                                iteratorShots.remove();
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private void explode(IGameObject gameObj) {
        Asteroid asteroid = (Asteroid)gameObj;
        for (int i = 0; i < Constants.EXPLODE_PARTICLES; i++) {
            int angle = (int)Math.floor(Math.random()*360+1);
            double width =  Math.floor((Math.random()*(30-10+1)))+10;
            int lenght = (int)Math.floor(Math.random()*asteroid.randomDefaultLengthLimit+1);
            PositionVector vector = MathHelper.getVectorByLengthAndAngle(asteroid.positionVector, angle, lenght);
            PositionVector speedVector = MathHelper.getRandSpeedVector(-30, 30);
            this.gameParticles.add(new Particle(vector, speedVector, width, 5, Color.BLACK));
        }
    }

    private void shot() {
        PositionVector positionVector = MathHelper.getVectorByLengthAndAngle(
                ship.positionVector,
                ship.angle,
                15
        );
        PositionVector speedVector = MathHelper.getParticleSpeedVectorFromAngle(ship.angle);
        speedVector.addToX(speedVector.getX()*-Constants.SHOT_SPEED);
        speedVector.addToY(speedVector.getY()*-Constants.SHOT_SPEED);
        this.shotParticles.add(
                new Particle(
                        positionVector,
                        speedVector,
                        Constants.SHOT_WIDTH,
                        Constants.SHOT_LIFETIME,
                        Color.MAGENTA
                )
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long currentTime = System.currentTimeMillis();
        if (!gameRunning) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(10);
            paint.setTextSize(50);
            canvas.drawText("Game Over, seconds alive : " + Long.toString(timeAlive/1000),
                    screenWidth/3,screenHeigth/3, paint);
            return;
        }
        if (!this.calibrated) {
            if (GameView.orientationEvent != null) {
                this.rotateSensorBuffer.add(GameView.orientationEvent.values[1]);
                this.speedSensorBuffer.add(GameView.orientationEvent.values[2]);
            }
            if (this.rotateSensorBuffer.isFull()) {
                this.relativeZeroRotation = rotateSensorBuffer.getAvg();
                this.relativeZeroSpeed = speedSensorBuffer.getAvg();
                if (currentTime - startCalibrating < 3*1000) {
                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK);
                    paint.setStrokeWidth(10);
                    paint.setTextSize(50);
                    canvas.drawText("Calibrating, hold your phone still", screenWidth/3,screenHeigth/3, paint);
                    return;
                } else {
                    this.calibrated = true;
                }
            }
        }

        if (GameView.orientationEvent != null) {
            this.rotateSensorBuffer.add(GameView.orientationEvent.values[1]);
            this.speedSensorBuffer.add(GameView.orientationEvent.values[2]);
            if (this.relativeZeroRotation + rotateSensorBuffer.getAvg() > 10.d) {
                this.turnShip(DIRECTION.LEFT);
            } else if (this.relativeZeroRotation + rotateSensorBuffer.getAvg() < -10.d) {
                this.turnShip(DIRECTION.RIGHT);
            }

            if (this.relativeZeroSpeed - speedSensorBuffer.getAvg() > 10.d) {
                addSpeed(false);
            } else if (this.relativeZeroSpeed - speedSensorBuffer.getAvg() < -10.d) {
                addSpeed(true);
            }
        }

        if (currentTime - lastShot > 2000) {
            if (this.shotParticles.size() < Constants.SHOT_MAX_COUNT) {
                this.shot();
            }
            lastShot = currentTime;
        }

        if (currentTime - lastAsteroidGenerated > Constants.TIME_TO_SPAWN_ASTEROID
                && asteroids.size() < Constants.MAX_COUNT_OF_ASTEROIDS) {
            this.generateAsteroid();
            lastAsteroidGenerated = currentTime;
        }

        for (IGameObject gameObject : this.gameObjects) {
            gameObject.Move(screenWidth, screenHeigth);
            gameObject.DrawOnCanvas(canvas);
        }

        for (IGameObject asteroid : this.asteroids) {
            asteroid.Move(screenWidth, screenHeigth);
            asteroid.DrawOnCanvas(canvas);
        }

        for (Iterator<IGameParticle> iterator = this.shotParticles.iterator(); iterator.hasNext(); ) {
            IGameParticle shotParticle = iterator.next();
            if (shotParticle.isLifetime()) {
                shotParticle.Move(screenWidth, screenHeigth);
                shotParticle.DrawOnCanvas(canvas);
            } else {
                iterator.remove();
            }
        }

        for (Iterator<IGameParticle> iterator = this.gameParticles.iterator(); iterator.hasNext(); ) {
            IGameParticle gameParticle = iterator.next();
            if (gameParticle.isLifetime()) {
                gameParticle.Move(screenWidth, screenHeigth);
                gameParticle.DrawOnCanvas(canvas);
            } else {
                iterator.remove();
            }
        }

        while(!checkAsteroids(canvas));

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
