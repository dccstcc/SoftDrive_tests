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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.pjatk.softdrive.Exit;
import pl.pjatk.softdrive.database.DbManager;
import pl.pjatk.softdrive.gps.CLocation;
import pl.pjatk.softdrive.gps.IBaseGpsListener;

public class TrafficView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "SurfaceView"; // for logging errors

    private CannonThread cannonThread; // controls the UI loop

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
    private static final double FORWARD_VEHICLE_WIDTH_PERCENT = 1.0 / 6;
    private static final double FORWARD_VEHICLE_HEIGHT_PERCENT = 1.0 / 8;
    private static final double FORWARD_VEHICLE_SPEED_PERCENT = 0.2;

    // constants for the motorcycle
    private static final double MOTORCYCLE_WIDTH_PERCENT = 1.0 / 9;
    private static final double MOTORCYCLE_HEIGHT_PERCENT = 1.0 / 11;

    // meters text size 1/23 of screen width
    private static final double TEXT_SIZE_PERCENT = 1.0 / 23;

    // database
    private DbManager db;

    // threads
    private ExecutorService executor;
    private ScheduledExecutorService schedExecutor;

    // helpers and temporary
    private int forwardDistance = 0;
    private String motorcycleSpeed = "none";
    private float speed = 0f;
    private int distRegulator;
    private int motorHeight = 0;
    private int count;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public TrafficView(Context context, AttributeSet attrs) {
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
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w; // store view width
        screenHeight = h; // store view height
    }

    protected void initConstant() {
        this.width = (int) (FORWARD_VEHICLE_WIDTH_PERCENT * displayWidth);
        this.height = (int) (FORWARD_VEHICLE_HEIGHT_PERCENT * displayHeight);
        this.motorHeight = (int) (MOTORCYCLE_HEIGHT_PERCENT * displayHeight);
        textPaint.setTextSize((int) (TEXT_SIZE_PERCENT * screenHeight));
        textPaint.setAntiAlias(true); // smoothes the text
        backgroundPaint.setColor(Color.WHITE);
        tooFastAlarmPaint.setColor(Color.RED);
        ptConnAlert.setColor(Color.argb(255, 200, 50, 200));
        ptConnAlert.setTextSize(120);
    }

    protected void updateActualDistance() throws ExecutionException, InterruptedException {

        distRegulator = db.getDbDistance() > 0 ? db.getDbDistance() : distRegulator;

        forwardDistance = distRegulator;

        Log.v("car distance regulate", forwardDistance/100 + " meter");
    }

    protected void updateActualSpeed(float motorcycleSpeed) {
        this.speed = motorcycleSpeed;
        Log.v("actual speed", speed + " m/s");
    }

    protected void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
    }

    protected void drawMotorcycle(Canvas canvas) {
        motor = new Motorcycle(getContext(),
                canvas,
                (int) (MOTORCYCLE_WIDTH_PERCENT * displayWidth),
                (int) (MOTORCYCLE_HEIGHT_PERCENT * displayHeight));
    }

    protected void initForwardVehicle() {
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

    protected boolean isTooFast(float speed, int distance) {
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

    protected int getSafeSpeed(float speed, int distance) {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void updateCarPosition(Canvas canvas) throws InterruptedException, ExecutionException {
        forwardVehicle.updateForwardVehiclePosition(forwardDistance, motor.getyCoord(), motor.getHeight());
        forwardVehicle.draw(canvas);

        Log.v("car distance: ", forwardDistance/100 + " meter");
    }

    protected void drawSpeedMeter(Canvas canvas, String text) {
        canvas.drawText("V:  " + text + " km/h", 50, 100, textPaint);
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

    // called when surface is first created
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Handler mHandler;

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                hideSystemBars();
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

    // called when surface changes size
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height) {}

    // called when the surface is destroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    // called when the user touches the screen in this activity
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        ExitApp(e);
        return true;
    }

    protected int toKmh(float speed) {return (int) (speed *= (0.001f / (1f/3600f)));}

    // hide system bars and app bar
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


    // Thread subclass to control the UI loop
    private class CannonThread implements IBaseGpsListener {

        private SurfaceHolder surfaceHolder; // for manipulating canvas

        private ExecutorService es;
        private CLocation myGpsLocation;
        private float nCurrentSpeed;

        // initializes the surface holder
        @RequiresApi(api = Build.VERSION_CODES.O)
        public CannonThread(SurfaceHolder holder) {
            surfaceHolder = holder;

            initCLocation();

            schedExecutor = Executors.newScheduledThreadPool(5);
            es = Executors.newCachedThreadPool();
        }

        public void start() {

            Runnable rtask = new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {

                    Canvas canvas = null; // used for drawing

                        try {
                            // get Canvas for exclusive drawing from this thread
                            canvas = surfaceHolder.lockCanvas(null);

                        synchronized(surfaceHolder) {
                            initConstant();

                            updateActualDistance();
                            updateActualSpeed(nCurrentSpeed);

                            drawBackground(canvas);
                            drawMotorcycle(canvas);
                            initForwardVehicle();

                            drawSpeedAlert(canvas);

                            updateCarPosition(canvas);

                            drawSpeedMeter(canvas, String.valueOf(toKmh(nCurrentSpeed)));
                            drawPositionMeter(canvas);

                            Thread.sleep(1); // threads deadlock control
                        }
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        } finally {
                            if (canvas != null)
                                surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
            };

            schedExecutor.scheduleAtFixedRate(rtask,0, 250, TimeUnit.MILLISECONDS);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private float updateSpeed(CLocation location) {

            nCurrentSpeed = 9999;

            if (location != null) {
                location.setUseMetricunits(true);
                nCurrentSpeed = location.getSpeed();
            }

            return nCurrentSpeed;
        }

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
}