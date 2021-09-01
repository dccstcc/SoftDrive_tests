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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pjatk.softdrive.rest.controllers.GetDistanceCtrl;
import pl.pjatk.softdrive.view.MainViewActivity;

/**
 * Initialize rest and run view controller
 * @author Dominik Stec
 * @see AppCompatActivity
 * @see RestCtrlActivity
 */
public class RestCtrlActivity extends AppCompatActivity {

    private ExecutorService ipExec;
    private ExecutorService guiExec;
    /**
     * Check GPS permissions.
     * Initialize rest controller.
     * Run logo animation.
     * Run UI view
     * Control threads
     * @param savedInstanceState Android application Bundle
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrl_rest);

        manageLocationAccess();

        logoAnimation();

        searchIpAddressParallelThread();

        startGuiParallelThread(10000);
    }

    @NonNull
    public ExecutorService getExecutor() {
        ExecutorService e1 = Executors.newCachedThreadPool();
        return e1;
    }

    public Runnable getIpSearchRunnable() {
        Runnable rRest = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                new GetDistanceCtrl().findRtrIpB4(1);
            }
        };
        return rRest;
    }

    public void searchIpAddressParallelThread() {
        setIpExec(getExecutor());
        getIpExec().execute(getIpSearchRunnable());
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

    /**
     * Animation introduce logo
     */
    private void logoAnimation() {
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

    /**
     * Clear tasks and exit application
     */
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


