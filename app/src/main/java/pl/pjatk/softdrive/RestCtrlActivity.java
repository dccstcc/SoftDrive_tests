package pl.pjatk.softdrive;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.pjatk.softdrive.rest.controllers.RestGetDistanceCtrl;
import pl.pjatk.softdrive.rest.controllers.RestSearchIpCtrl;
import pl.pjatk.softdrive.view.MainViewActivity;

public class RestCtrlActivity extends AppCompatActivity {

    private ExecutorService ipExec;
    private ScheduledExecutorService distanceExec;
    private ExecutorService guiExec;

    private RestSearchIpCtrl ipCtrl;
    private RestGetDistanceCtrl distCtrl;

    String ip4Byte;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrl_rest);

        getExecutor().execute(new Runnable() {
            @Override
            public void run() {

                manageLocationAccess();

                logoAnimation();

                ipCtrl = new RestSearchIpCtrl();

                try {
                    ipCtrl.startSearchIpLoop();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ip4Byte = ipCtrl.getIp4Byte();

                distCtrl = new RestGetDistanceCtrl();

                assert ip4Byte != null;
                readDistanceParallelThread(Integer.parseInt(ip4Byte), distCtrl);

                startGuiParallelThread(8000);
            }
        });


    }

    @NonNull
    public ExecutorService getExecutor() {
        ExecutorService e1 = Executors.newCachedThreadPool();
        return e1;
    }

    @NonNull
    public ScheduledExecutorService getScheduleExecutor() {
        ScheduledExecutorService e2 = Executors.newScheduledThreadPool(5);
        return e2;
    }

    public Runnable getIpSearchRunnable() {
        Runnable rRest = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    ipCtrl.startSearchIp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        return rRest;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String searchByte4IpAddressParallelThread() {
        setIpExec(getExecutor());
        getIpExec().execute(getIpSearchRunnable());
        return ipCtrl.getIp4Byte();
    }

    public Runnable getDistanceReadRunnable(int ip4Byte, RestGetDistanceCtrl distCtrl) {
        Runnable rDist = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                distCtrl.startRest(ip4Byte);
            }
        };
        return rDist;
    }

    public void readDistanceParallelThread(int ip4Byte, RestGetDistanceCtrl distCtrl) {
        setDistanceExec(getScheduleExecutor());
        getDistanceExec().scheduleAtFixedRate(getDistanceReadRunnable(ip4Byte, distCtrl), 3000, 250, TimeUnit.MILLISECONDS);
    }

    public Runnable getGuiRunnable(int startDelayMilis) {
        Runnable rView = new Runnable() {
            @Override
            public void run() {
                delay(startDelayMilis);
                Intent intent = new Intent(RestCtrlActivity.this, MainViewActivity.class);
                startActivity(intent);
            }
        };
        return rView;
    }

    public void delay(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startGuiParallelThread(int delayMilis) {
        setGuiExec(getExecutor());
        getGuiExec().execute(getGuiRunnable(delayMilis));
    }

    public void manageLocationAccess() {

        if( ! isLocationGrant()) {
            Log.e("GPS","NO GPS PERMISSION");
            ActivityCompat.requestPermissions(RestCtrlActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }

    }

    public boolean isLocationGrant() {
        if (ActivityCompat.checkSelfPermission(RestCtrlActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(RestCtrlActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void logoAnimation() {
        ExecutorService ex = Executors.newCachedThreadPool();
        ex.execute(new Runnable() {
            @Override
            public void run() {
                FrameLayout layout = findViewById(R.id.logo_anim_layout);
                layout.animate().translationYBy(-1000f).setDuration(10000L);
                layout.animate().rotationYBy(1000f).setDuration(10000L);
                layout.setBackgroundColor(Color.BLACK);
            }
        });
    }

    public ExecutorService getIpExec() {
        return ipExec;
    }

    public void setIpExec(ExecutorService ipExec) {
        this.ipExec = ipExec;
    }

    public ExecutorService getGuiExec() {
        return guiExec;
    }

    public void setGuiExec(ExecutorService guiExec) {
        this.guiExec = guiExec;
    }

    public ScheduledExecutorService getDistanceExec() {
        return distanceExec;
    }

    public void setDistanceExec(ScheduledExecutorService distanceExec) {
        this.distanceExec = distanceExec;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getIpExec().shutdownNow();
        getGuiExec().shutdownNow();

        Intent i = new Intent(this, Exit.class);
        i.putExtra("EXTRA_EXIT", true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}


