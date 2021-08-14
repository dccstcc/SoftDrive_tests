package pl.pjatk.softdrive;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pjatk.softdrive.rest.controllers.GetDistanceCtrl;
import pl.pjatk.softdrive.view.MainViewActivity;

//import pl.pjatk.softdrive.rest.services.RestScan2dService;

public class RestCtrlActivity extends AppCompatActivity {

    Button btnHitDist;
    TextView txtJsonDist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrl_rest);

        btnHitDist = (Button) findViewById(R.id.btnHitDist);
        txtJsonDist = (TextView) findViewById(R.id.JsonDistTxt);

        if (ActivityCompat.checkSelfPermission(RestCtrlActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(RestCtrlActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            System.out.println("! NO GPS PERMISSION Rest CTRL !");

            ActivityCompat.requestPermissions(RestCtrlActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }


        setContentView(R.layout.activity_main);


        btnHitDist.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                ExecutorService e1 = Executors.newCachedThreadPool();
                e1.execute(new Runnable() {
                    @Override
                    public void run() {
                        new GetDistanceCtrl().findRtrIpB4(1);
                    }
                });

                ExecutorService e2 = Executors.newCachedThreadPool();
                e2.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(7000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(RestCtrlActivity.this, MainViewActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });

        try {
            Thread.sleep(10000);
            btnHitDist.callOnClick();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        //https://developer.android.com/training/system-ui/navigation
//        View decorView = getWindow().getDecorView();
//        // Hide both the navigation bar and the status bar.
//        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
//        // a general rule, you should design your app to hide the status bar whenever you
//        // hide the navigation bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
    }
}


