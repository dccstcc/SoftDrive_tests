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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.pjatk.softdrive.Exit;
import pl.pjatk.softdrive.database.DbManager;
import pl.pjatk.softdrive.gps.CLocation;
import pl.pjatk.softdrive.gps.IBaseGpsListener;

/**
 * View controller
 * @author Dominik Stec
 * @see SurfaceView
 * @see SurfaceHolder.Callback
 * @see Display
 * @see ForwardVehicle
 * @see Motorcycle
 * configuration base come from following
 * @link https://www.pearson.com/uk/educators/higher-education-educators/program/Deitel-Android-How-to-Program-International-Edition-With-an-Introduction-to-Java/PGM1022783.html?tab=contents [18.08.2021]
 */
public class UiView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "UiView"; // for logging errors

    private UiViewThread uiViewThread; // controls the UI loop

    private Activity activity; // to display Game Over dialog in GUI thread

    // dimension variables
    private int screenWidth;
    private int screenHeight;
    private Display display;
    private int displayWidth;
    private int displayHeight;
    private int width;
    private int height;

    // Paint variables used when drawing each item on the screen
    private Paint textPaint; // Paint used to draw text
    private Paint backgroundPaint; // Paint used to clear the drawing area
    private Paint tooFastAlarmPaint; // Paint used to draw speed alert
    private Paint ptConnAlert; // Paint used to draw connection alert

    // Vehicles classes
    private Motorcycle motor;
    private ForwardVehicle forwardVehicle;

    // constants for the forward vehicle
    private static final double FORWARD_VEHICLE_WIDTH_PERCENT = 1.0 / 5;
    private static final double FORWARD_VEHICLE_HEIGHT_PERCENT = 1.0 / 7;
    private static final double FORWARD_VEHICLE_SPEED_PERCENT = 0.2;

    // constants for the motorcycle
    private static final double MOTORCYCLE_WIDTH_PERCENT = 1.0 / 6;
    private static final double MOTORCYCLE_HEIGHT_PERCENT = 1.0 / 8;

    // meters text size 1/23 of screen width
    private static final double TEXT_SIZE_PERCENT = 1.0 / 23;

    // database
    private DbManager db;

    // threads
    private ExecutorService executor;
    private ScheduledExecutorService schedExecutor;

    // helpers and temporary
    private int forwardDist = 0;
    private String motorcycleSpeed = "none";
    private static float speed = 0f;
    private int distRegulator;
    private int motorHeight = 0;
    private int count;

    private static float nCurrentSpeed;

    /**
     * Main view initialization
     * @param context this Android application Context object
     * @param attrs TODO attributes of object state
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public UiView(Context context, AttributeSet attrs) {
        super(context, attrs);

        activity = (Activity) context; // store reference to MainViewActivity

        getHolder().addCallback(this); // register SurfaceHolder.Callback listener

        textPaint = new Paint();
        backgroundPaint = new Paint();
        tooFastAlarmPaint = new Paint();
        ptConnAlert = new Paint();

        db = new DbManager(getContext());

        executor = Executors.newFixedThreadPool(7);

        display = new Display(getContext());
        displayWidth = display.getDisplayWidth();
        displayHeight = display.getDisplayHeight();

        motorcycleSpeed = "none";

        distRegulator = 1;

        count = 0;

        screenWidth = displayWidth; // store view width
        screenHeight = displayHeight; // store view height

        nCurrentSpeed = 0.2f;
    }

    /**
     * Initialize object constants and values
     */
    protected synchronized void initConstant() {
        this.width = (int) (FORWARD_VEHICLE_WIDTH_PERCENT * displayWidth);
        this.height = (int) (FORWARD_VEHICLE_HEIGHT_PERCENT * displayHeight);
        this.motorHeight = (int) (MOTORCYCLE_HEIGHT_PERCENT * displayHeight);
        textPaint.setTextSize((int) (TEXT_SIZE_PERCENT * screenHeight));
        backgroundPaint.setColor(Color.GRAY);
        tooFastAlarmPaint.setColor(Color.YELLOW);
        ptConnAlert.setColor(Color.WHITE);
        ptConnAlert.setTextSize(120);
    }

    /**
     * Set distance of forward vehicle read from database
     * @throws InterruptedException
     */
    protected synchronized void updateActualDistance() throws InterruptedException {

        distRegulator = db.getDbDistance() > 0 ? db.getDbDistance() : distRegulator;

        forwardDist = distRegulator < 4000 ? distRegulator : 4000;

        Log.v("car distance regulate", forwardDist + " meter");
    }

    /**
     * Set actual motorcycle driver speed
     * @param motorcycleSpeed speed of driver move
     */
    protected synchronized void updateActualSpeed(float motorcycleSpeed) {
        this.speed = motorcycleSpeed;
        Log.v("actual speed", speed + " m/s");
    }

    /**
     * Set screen background for objects
     * @param canvas draw object space
     */
    protected synchronized void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
    }

    /**
     * Draw motorcycle object on canvas
     * @param canvas draw object space
     */
    protected synchronized void drawMotorcycle(Canvas canvas) {
        motor = new Motorcycle(getContext(),
                canvas,
                (int) (MOTORCYCLE_WIDTH_PERCENT * displayWidth),
                (int) (MOTORCYCLE_HEIGHT_PERCENT * displayHeight));
    }

    /**
     * Forward vehicle initialize configuration
     */
    protected synchronized void initForwardVehicle() {
        int xCenter = displayWidth / 2 - width / 2;

        forwardVehicle = new ForwardVehicle(
                getContext(),
                this,
                Color.GREEN,
                0,
                xCenter,
                10,
                width,
                height,
                0,
                (float) FORWARD_VEHICLE_SPEED_PERCENT * displayHeight
        );
    }

    /**
     * Show to big speed or to low distance alert
     * @param canvas draw object space
     */
    protected synchronized void drawSpeedAlert(Canvas canvas) {

        boolean isTooFast = false;
        if (speed > 0) isTooFast = isTooFast(speed, forwardDist/100);

        if (isTooFast) {
            int safeSpeed = getSafeSpeed(forwardDist/100);
            tooFastAlarmPaint.setTextSize(140);
            if(safeSpeed > 0 && toKmh(speed) - safeSpeed > 5) canvas.drawText("reduce: " + safeSpeed + " km/h", 30, 1600, tooFastAlarmPaint);
        }
    }

    /**
     * Check if speed or distance is dangerous
     * @param speed speed of driver move
     * @param distance distance between driver and forward vehicle
     * @return true if situation is risk or false if it is safe
     */
    protected boolean isTooFast(float speed, int distance) {
        boolean isTooFast = false;

        // 9 m/s^2
        float breakLatency = 9f;
        // V = at
        float breakTime = speed / breakLatency;
        // S = Vt
        int breakDistance = (int) (speed * breakTime);
        if (breakDistance > distance) isTooFast = true;

        return isTooFast;
    }

    /**
     * Calculate safe speed of move according to forward vehicle distance
     * @param distance distance between driver and forward vehicle
     * @return safe speed value
     */
    protected int getSafeSpeed(int distance) {
        // 9 m/s^2
        float breakLatency = 9f;
        // S = at^2 / 2
        // 2S = at^2
        // t = sqrt(2S / a)
        float breakTime = (float) Math.sqrt((2*distance) / breakLatency);
        // V = S/t
        if(breakTime == 0) return 0;
        float safeSpeed = distance / breakTime;
        safeSpeed = Math.round(safeSpeed);

        return toKmh(safeSpeed);
    }

    /**
     * Set position and draw forward vehicle
     * @param canvas draw object space
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected synchronized void updateCarPosition(Canvas canvas) {
        forwardVehicle.updateForwardVehiclePosition(forwardDist, motor.getyCoord(), motor.getHeight());
        forwardVehicle.draw(canvas);

        Log.v("car distance rest: ", forwardDist + " c_meter");
    }

    /**
     * Draw actual speed on screen
     * @param canvas draw object space
     * @param text speed value to draw
     */
    protected synchronized void drawSpeedMeter(Canvas canvas, String text) {
        canvas.drawText("V:  " + text + " km/h", 50, 100, textPaint);
    }

    /**
     * Draw actual position between driver and forward vehicle
     * @param canvas draw object space
     * @throws InterruptedException
     */
    protected synchronized void drawPositionMeter(Canvas canvas) throws InterruptedException {

        canvas.drawText("dist: " + forwardDist, 600, 100, textPaint);

        if (db.getDbDistance() < 0) {
            ++count;
            if (count > 20) {
                canvas.drawText("..car detection..", 120, 300, ptConnAlert);
                forwardVehicle.draw(canvas);
            }
        } else {
            count = 0;
        }
    }

    /**
     * Called when surface is first created and start user interface view thread
     * @param holder surface object holder
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Handler mHandler;

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                hideSystemBars();
                uiViewThread = new UiViewThread(holder); // create thread
                uiViewThread.start(); // start the game loop thread
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

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
    }

    // called when surface changes size
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height) {
    }

    // called when the surface is destroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    /**
     * Called when the user touches the screen in this activity and exit application
     * @param e touch screen event handler
     * @return true if detect touch screen
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        ExitApp(e);
        return true;
    }

    /**
     * Parse from m/s to km/h
     * @param speed speed in m/s to parse
     * @return parsed speed in km/h
     */
    protected int toKmh(float speed) {
        return speed > 0 ? (int) (speed *= (0.001f / (1f / 3600f))) : (int) speed;
    }

    /**
     * hide system bars and app bar
     */
    protected void hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /**
     * Close application preparation
     * @param event touch screen event handler
     */
    protected void ExitApp(MotionEvent event) {
        executor.shutdownNow();
        schedExecutor.shutdownNow();

        Intent i = new Intent(getContext(), Exit.class);
        i.putExtra("EXTRA_EXIT", true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getContext().startActivity(i);
    }

    /**
     * Thread subclass to control the UI view loop
     * @author Dominik Stec
     * @see IBaseGpsListener
     * configuration base come from following
     * @link https://www.pearson.com/uk/educators/higher-education-educators/program/Deitel-Android-How-to-Program-International-Edition-With-an-Introduction-to-Java/PGM1022783.html?tab=contents [18.08.2021]
     */
    private class UiViewThread implements IBaseGpsListener{

        private SurfaceHolder surfaceHolder; // for manipulating canvas

        private ExecutorService es;
        private CLocation myGpsLocation;

        /**
         * initializes the surface holder
         * @param holder hold surface object
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        public UiViewThread(SurfaceHolder holder) {
            surfaceHolder = holder;

            initCLocation();

            schedExecutor = Executors.newScheduledThreadPool(20);
            es = Executors.newCachedThreadPool();

            nCurrentSpeed = -10f;
        }

        /**
         * Run main UI view thread with synchronized methods for control UI states
         */
        public void start() {

            Runnable rtask = new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {

                    Canvas canvas = null; // used for drawing

                    try {
                        // get Canvas for exclusive drawing from this thread
                        canvas = surfaceHolder.lockCanvas(null);

                        synchronized (canvas) {

                            initConstant();

                            updateActualDistance();

                            updateActualSpeed(nCurrentSpeed);

                            drawBackground(canvas);

                            initForwardVehicle();

                            drawMotorcycle(canvas);

                            drawSpeedAlert(canvas);

                            updateCarPosition(canvas);

                            drawSpeedMeter(canvas, String.valueOf(toKmh(nCurrentSpeed)));

                            drawPositionMeter(canvas);

                            Thread.sleep(1); // threads deadlock control
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (canvas != null)
                            surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            };

            schedExecutor.scheduleAtFixedRate(rtask, 0, 250, TimeUnit.MILLISECONDS);
        }

        /**
         * Set actual speed of driver move
         * @param location GPS state callback object
         * @return actual river speed
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        private synchronized float updateSpeed(CLocation location) {

            if (location != null) {
                location.setUseMetricunits(true);
                nCurrentSpeed = location.getSpeed();
            }

            return nCurrentSpeed > 0 ? nCurrentSpeed : -0.1f;
        }

        /**
         * Initialise GPS service
         */
        private void initCLocation() {

                    LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        Log.v("GPS", "! NO GPS PERMISSION !");

                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }

        /**
         * GPS location change event listener
         * @param location GPS state callback object
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public synchronized void onLocationChanged(Location location) {

            es.execute(new Runnable() {
                @Override
                public void run() {
                    synchronized(location) {
                        Log.v(TAG, "IN ON LOCATION CHANGE, lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
                        Log.v(TAG, "IN ON LOCATION CHANGE SPEED = " + location.getSpeed());

                        myGpsLocation = new CLocation(location, true);
                        nCurrentSpeed = updateSpeed(myGpsLocation);
                    }

                }
            });
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
}