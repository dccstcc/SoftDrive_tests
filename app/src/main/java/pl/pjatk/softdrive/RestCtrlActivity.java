package pl.pjatk.softdrive;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

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

    ExecutorService e1;
    ExecutorService e2;

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

        if (ActivityCompat.checkSelfPermission(RestCtrlActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(RestCtrlActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.e("GPS","NO GPS PERMISSION");

            ActivityCompat.requestPermissions(RestCtrlActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }

        logoAnimation();

        e1 = Executors.newCachedThreadPool();
                Runnable rRest = new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        new GetDistanceCtrl().findRtrIpB4(1);
                    }
                };

                e2 = Executors.newCachedThreadPool();
                Runnable rView = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(RestCtrlActivity.this, MainViewActivity.class);
                                startActivity(intent);
                            }
                        };

        e1.execute(rRest);
        e2.execute(rView);
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

    /**
     * Clear tasks and exit application
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        e1.shutdownNow();
        e2.shutdownNow();

        Intent i = new Intent(this, Exit.class);
        i.putExtra("EXTRA_EXIT", true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}


