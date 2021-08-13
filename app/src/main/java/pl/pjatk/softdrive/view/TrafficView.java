package pl.pjatk.softdrive.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.pjatk.softdrive.Exit;
import pl.pjatk.softdrive.R;
import pl.pjatk.softdrive.database.DbManager;
import pl.pjatk.softdrive.gps.CLocation;
import pl.pjatk.softdrive.gps.IBaseGpsListener;

public class TrafficView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "trafficView"; // for logging errors


    // text size 1/18 of screen width
    public static final double TEXT_SIZE_PERCENT = 1.0 / 23;

    private CannonThread cannonThread; // controls the game loop
    private Activity activity; // to display Game Over dialog in GUI thread
    private boolean dialogIsDisplayed = false;

    // dimension variables
    private int screenWidth;
    private int screenHeight;

    // variables for the game loop and tracking statistics
    private double timeLeft; // time remaining in seconds
    private int shotsFired; // shots the user has fired
    private double totalElapsedTime; // elapsed seconds

    // constants and variables for managing sounds
    public static final int TARGET_SOUND_ID = 0;
    public static final int CANNON_SOUND_ID = 1;
    public static final int BLOCKER_SOUND_ID = 2;
    private SoundPool soundPool; // plays sound effects
    private SparseIntArray soundMap; // maps IDs to SoundPool

    // Paint variables used when drawing each item on the screen
    private Paint textPaint; // Paint used to draw text
    private Paint backgroundPaint; // Paint used to clear the drawing area


    /////////////////////////////my constant
    protected Motorcycle motor;
    private ForwardVehicle forwardVehicle;
    private Display display;
    private int displayWidth;
    private int displayHeight;

    // constants for the forward vehicle
    public static final double FORWARD_VEHICLE_WIDTH_PERCENT = 1.0 / 6;
    public static final double FORWARD_VEHICLE_HEIGHT_PERCENT = 1.0 / 8;
    public static final double FORWARD_VEHICLE_SPEED_PERCENT = 0.2;

    public static final double MOTORCYCLE_WIDTH_PERCENT = 1.0 / 9;
    public static final double MOTORCYCLE_HEIGHT_PERCENT = 1.0 / 11;

    private DbManager db;
    ExecutorService executor;
    int forwardDistance = 0;

    int motorHeight = 0;

    public String motorcycleSpeed = "none";

    float speed = 0f;

    HashMap<Integer, Integer> speedTable;

    Paint tooFastAlarmPaint;

    ScheduledExecutorService schedExecutor;

//    int counterplus;

    int distRegulator;

    int count;
    Paint ptConnAlert;

    protected int width;
    protected int height;

    // constructor
    @RequiresApi(api = Build.VERSION_CODES.O)
    public TrafficView(Context context, AttributeSet attrs) {
        super(context, attrs); // call superclass constructor
        activity = (Activity) context; // store reference to MainViewActivity

        // register SurfaceHolder.Callback listener
        getHolder().addCallback(this);

        // configure audio attributes for game audio
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        // initialize SoundPool to play the app's three sound effects
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(1);
        builder.setAudioAttributes(attrBuilder.build());
        soundPool = builder.build();

        // create Map of sounds and pre-load sounds
        soundMap = new SparseIntArray(3); // create new SparseIntArray
        soundMap.put(TARGET_SOUND_ID,
                soundPool.load(context, R.raw.target_hit, 1));
        soundMap.put(CANNON_SOUND_ID,
                soundPool.load(context, R.raw.cannon_fire, 1));
        soundMap.put(BLOCKER_SOUND_ID,
                soundPool.load(context, R.raw.blocker_hit, 1));

        textPaint = new Paint();
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);


        //////////////////my own
        db = new DbManager(getContext());
        executor = Executors.newFixedThreadPool(7);

        display = new Display(getContext());
        displayWidth = display.getDisplayWidth();
        displayHeight = display.getDisplayHeight();

        motorcycleSpeed = "from constructor";

        //speedTable = initSpeedTable();
        tooFastAlarmPaint = new Paint();
        tooFastAlarmPaint.setColor(Color.RED);


        distRegulator = 1;

        count = 0;
        ptConnAlert = new Paint();
        ptConnAlert.setColor(Color.argb(255, 200, 50, 200));
        ptConnAlert.setTextSize(120);

    }


    // called when the size of the SurfaceView changes,
    // such as when it's first added to the View hierarchy
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w; // store CannonView's width
        screenHeight = h; // store CannonView's height

        // configure text properties
        textPaint.setTextSize((int) (TEXT_SIZE_PERCENT * screenHeight));
        textPaint.setAntiAlias(true); // smoothes the text
    }

    // get width of the game screen
    public int getScreenWidth() {
        return screenWidth;
    }

    // get height of the game screen
    public int getScreenHeight() {
        return screenHeight;
    }

    // plays a sound with the given soundId in soundMap
    public void playSound(int soundId) {
        soundPool.play(soundMap.get(soundId), 1, 1, 1, 0, 1f);
    }

    // called repeatedly by the CannonThread to update game elements
    private void updateActualDistance() throws ExecutionException, InterruptedException {

        //forwardDistance = db.getDbDistance();

        distRegulator = db.getDbDistance() > 0 ? db.getDbDistance() : distRegulator;

        forwardDistance = distRegulator;


        System.out.println("distance from view: " + forwardDistance);
    }

    protected void drawPositionMeter(Canvas canvas) throws ExecutionException, InterruptedException {

        canvas.drawText("dist: " + forwardDistance, 600, 100, textPaint);

        if(db.getDbDistance() < 0 || true) {
            ++count;
            if(count>15 || true) {
                canvas.drawText("..car detection..", 120, 300, ptConnAlert);
                forwardVehicle.draw(canvas);
            }
        } else {
            count = 0;
        }

    }

    // aligns the barrel and fires a Cannonball if a Cannonball is not
    // already on the screen
    public void ExitApp(MotionEvent event) {

        /////////////////////////////on touch exit app
        cannonThread.setRunning(false);
        Intent i = new Intent(getContext(), Exit.class);
        i.putExtra("EXTRA_EXIT", true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getContext().startActivity(i);


    }

    public void drawSpeedMeter(Canvas canvas, String text) {

        canvas.drawText("V:  " + text + " km/h", 50, 100, textPaint);

    }

    protected void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
    }


    // draws the game to the given Canvas
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateCarPosition(Canvas canvas) throws InterruptedException, ExecutionException {
        // clear the background

//        new TrafficView(getContext(), null);

//        int width = (int) (FORWARD_VEHICLE_WIDTH_PERCENT * displayWidth);
//        int height = (int) (FORWARD_VEHICLE_HEIGHT_PERCENT * displayHeight);
//
//        int x = displayWidth / 2 - width / 2;
//
//        forwardVehicle = new ForwardVehicle(
//                getContext(),
//                TrafficView.this,
//                Color.GREEN,
//                TARGET_SOUND_ID,
//                x,
//                10,
//                width,
//                height,
//                0,
//                (float) FORWARD_VEHICLE_SPEED_PERCENT * displayHeight
//        );

//        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(),
//                backgroundPaint);



//        speedRegulator = db.getDbDistance() > 0 ? db.getDbDistance() : speedRegulator;
//        if(db.getDbDistance() < 0) {
//            ++count;
//            if(count>15) {
//                canvas.drawText("..car detection..", 120, 300, ptConnAlert);
//            }
//        } else {
//            count = 0;
//        }
//        forwardDistance = db.getDbDistance();
//        if(forwardDistance > 0) {
//            speedRegulator = forwardDistance;
//        }
        //forwardDistance = ++counterplus;
        //Thread.sleep(150);

        System.out.println("distance from view: " + forwardDistance);
//        canvas.drawText("dist: " + forwardDistance, 600, 100, textPaint);


        forwardVehicle.updateForwardVehiclePosition(forwardDistance, motor.getyCoord(), motor.getHeight());

        forwardVehicle.draw(canvas);

    }

    protected void drawSpeedAlert(Canvas canvas) {
        int fwDistMetric = forwardDistance / 100;

        if(isTooFast(speed, fwDistMetric) || true) {
            int safeSpeed = getSafeSpeed(speed, fwDistMetric);
            safeSpeed *= (0.001f / (1f/3600f));
            tooFastAlarmPaint.setTextSize(140);
            canvas.drawText("reduce: " + safeSpeed + " km/h", 30, 1600, tooFastAlarmPaint);
            forwardVehicle.paint.setColor(Color.RED);
        } else {
            forwardVehicle.paint.setColor(Color.GREEN);
        }
    }

    protected void drawMotorcycle(Canvas canvas) {
        motor = new Motorcycle(getContext(),
                canvas,
                (int) (MOTORCYCLE_WIDTH_PERCENT * displayWidth),
                (int) (MOTORCYCLE_HEIGHT_PERCENT * displayHeight));
    }

    public boolean isTooFast(float speed, int distance) {
        boolean isTooFast = false;

        // 9 m/s^2
        float breakLatency = 9f;
        // V = at
        float breakTime = speed / breakLatency;
        // S = at^2 / 2
        float breakDistance = (breakLatency * breakTime * breakTime) / 2;
        int breakDistanceInt = Math.round(breakDistance);

        if(breakDistanceInt >= distance) isTooFast = true;

        return isTooFast;
    }

    public int getSafeSpeed(float speed, int distance) {
        // 9 m/s^2
        float breakLatency = 9f;
        // V = at
        float breakTime = speed / breakLatency;
        // S = at^2 / 2
        float breakDistance = (breakLatency * breakTime * breakTime) / 2;
        int breakDistanceInt = Math.round(breakDistance);
        // V = s/t
        if (breakTime == 0) breakTime = 1;
        int safeSpeed = breakDistanceInt / Math.round(breakTime);

        return safeSpeed;
    }

    public void updateActualSpeed(float motorcycleSpeed) {
        this.speed = motorcycleSpeed;
        Log.v("processed speed ", String.valueOf(speed));

    }

    // stops the game: called by CannonGameFragment's onPause method
    public void stopGame() {
        if (cannonThread != null)
            cannonThread.setRunning(false); // tell thread to terminate
    }

    // release resources: called by CannonGame's onDestroy method
    public void releaseResources() {
        soundPool.release(); // release all resources used by the SoundPool
        soundPool = null;
    }

    protected void initConstant() {
        this.width = (int) (FORWARD_VEHICLE_WIDTH_PERCENT * displayWidth);
        this.height = (int) (FORWARD_VEHICLE_HEIGHT_PERCENT * displayHeight);
        this.motorHeight = (int) (MOTORCYCLE_HEIGHT_PERCENT * displayHeight);
    }

    protected void initForwardVehicle() {

//        int width = (int) (FORWARD_VEHICLE_WIDTH_PERCENT * displayWidth);
//        int height = (int) (FORWARD_VEHICLE_HEIGHT_PERCENT * displayHeight);

        int x = displayWidth / 2 - width / 2;

        forwardVehicle = new ForwardVehicle(
                getContext(),
                this,
                Color.GREEN,
                TARGET_SOUND_ID,
                x,
                10,
                width,
                height,
                0,
                (float) FORWARD_VEHICLE_SPEED_PERCENT * displayHeight
        );
    }


    // called when surface changes size
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height) {
    }

    // called when surface is first created
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Handler mHandler;

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                hideSystemBars();
                //TrafficView trvi = new TrafficView(getContext(), null);
                cannonThread = new CannonThread(holder); // create thread
                cannonThread.start(); // start the game loop thread
            }
        };

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Message message = mHandler.obtainMessage(1, "UI handle");
                message.sendToTarget();
            }
        });

    }

    // called when the surface is destroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        executor.shutdownNow();
        schedExecutor.shutdownNow();
    }

    // called when the user touches the screen in this activity
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        executor.shutdownNow();
        schedExecutor.shutdownNow();

        Intent i = new Intent(getContext(), Exit.class);
        getContext().startActivity(i);
        ExitApp(e);
        return true;
    }

    // Thread subclass to control the game loop
    private class CannonThread implements IBaseGpsListener {
        private SurfaceHolder surfaceHolder; // for manipulating canvas
        private boolean threadIsRunning = true; // running by default

        ExecutorService es;
        CLocation myGpsLocation;
        float nCurrentSpeed;

        // initializes the surface holder
        @RequiresApi(api = Build.VERSION_CODES.O)
        public CannonThread(SurfaceHolder holder) {
            surfaceHolder = holder;
            //setName("CannonThread");

            initCLocation();

            executor = Executors.newCachedThreadPool();
            schedExecutor = Executors.newScheduledThreadPool(5);
            es = Executors.newCachedThreadPool();
        }

        // changes running state
        public void setRunning(boolean running) {
            threadIsRunning = running;
        }

        private int toKmh(float speed) {return (int) (speed *= (0.001f / (1f/3600f)));}

        public void start() {

            Runnable rtask = new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {

                    Canvas canvas = null; // used for drawing

                    int clock = 1000;

//                    while (true) {

                        try {
                            // get Canvas for exclusive drawing from this thread
                            canvas = surfaceHolder.lockCanvas(null);

                            // lock the surfaceHolder for drawing
                        synchronized(surfaceHolder) {
                            initConstant();

                            updateActualDistance(); // update game state
                            updateActualSpeed(nCurrentSpeed);

                            drawBackground(canvas);
                            drawMotorcycle(canvas);
                            initForwardVehicle();

                            updateCarPosition(canvas); // draw using the canvas

                            drawSpeedMeter(canvas, String.valueOf(toKmh(nCurrentSpeed)));
                            drawPositionMeter(canvas);

                            drawSpeedAlert(canvas);

                            Thread.sleep(1);
                            System.out.println("threadlooper");
                        }
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        } finally {
                            // display canvas's contents on the CannonView
                            // and enable other threads to use the Canvas
                            if (canvas != null)
                                surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }

//                }
            };

            schedExecutor.scheduleAtFixedRate(rtask,0, 250, TimeUnit.MILLISECONDS);

        }


        private void initCLocation() {

                    LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        System.out.println("! NO GPS PERMISSION !");

                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private float updateSpeed(CLocation location) {

                    nCurrentSpeed = 9999;

                    if (location != null) {
                        location.setUseMetricunits(true);
                        nCurrentSpeed = location.getSpeed();
                    }

//                    int speedKmh = (int) (nCurrentSpeed *= (0.001f / (1f/3600f)));

                    System.out.println("SPEED " + nCurrentSpeed);
//                    nCurrentSpeed *= (0.001f / (1f/3600f));
//                    System.out.println("SPEED km/h" + nCurrentSpeed);

            return nCurrentSpeed;
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onLocationChanged(Location location) {

            es = Executors.newCachedThreadPool();

            es.execute(new Runnable() {
                @Override
                public void run() {
                    Log.v(TAG, "IN ON LOCATION CHANGE, lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
                    Log.v(TAG, "IN ON LOCATION CHANGE SPEED = " + location.getSpeed());

                    myGpsLocation = new CLocation(location, true);
                    nCurrentSpeed = updateSpeed(myGpsLocation);

                }
            });

            es.shutdown();


        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onGpsStatusChanged(int event) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    // hide system bars and app bar
    private void hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE);

    }
}


/*********************************************************************************
 * (C) Copyright 1992-2016 by Deitel & Associates, Inc. and * Pearson Education, *
 * Inc. All Rights Reserved. * * DISCLAIMER: The authors and publisher of this   *
 * book have used their * best efforts in preparing the book. These efforts      *
 * include the * development, research, and testing of the theories and programs *
 * * to determine their effectiveness. The authors and publisher make * no       *
 * warranty of any kind, expressed or implied, with regard to these * programs   *
 * or to the documentation contained in these books. The authors * and publisher *
 * shall not be liable in any event for incidental or * consequential damages in *
 * connection with, or arising out of, the * furnishing, performance, or use of  *
 * these programs.                                                               *
 *********************************************************************************/



