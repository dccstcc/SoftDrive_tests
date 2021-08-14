package pl.pjatk.softdrive;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
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

        if (ActivityCompat.checkSelfPermission(RestCtrlActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(RestCtrlActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            System.out.println("! NO GPS PERMISSION Rest CTRL !");

            ActivityCompat.requestPermissions(RestCtrlActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }


        setContentView(R.layout.activity_ctrl_rest);

        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.scene_root);


        ExecutorService e1 = Executors.newCachedThreadPool();
                Runnable rRest = new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        new GetDistanceCtrl().findRtrIpB4(1);
                    }
                };

                ExecutorService e2 = Executors.newCachedThreadPool();
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


