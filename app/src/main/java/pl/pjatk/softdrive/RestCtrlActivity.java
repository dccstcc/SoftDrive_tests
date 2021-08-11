package pl.pjatk.softdrive;

import android.Manifest;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
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
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import pl.pjatk.softdrive.rest.controllers.IpAddressCtrl;
import pl.pjatk.softdrive.rest.services.RestScan2dService;
import pl.pjatk.softdrive.view.MainViewActivity;

public class RestCtrlActivity extends AppCompatActivity {

    Button btnHitDist;
    Button btnHitSpace;
    TextView txtJsonDist;
    TextView txtJsonSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrl_rest);

        btnHitDist = (Button) findViewById(R.id.btnHitDist);
        btnHitSpace = (Button) findViewById(R.id.btnHitSpace);
        txtJsonDist = (TextView) findViewById(R.id.JsonDistTxt);
        txtJsonSpace = (TextView) findViewById(R.id.JsonSpaceTxt);

        if (ActivityCompat.checkSelfPermission(RestCtrlActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(RestCtrlActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            System.out.println("! NO GPS PERMISSION Rest CTRL !");

            ActivityCompat.requestPermissions(RestCtrlActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);

        }


        btnHitDist.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                new IpAddressCtrl().findRtrIpB4(1);
            }
        });

        btnHitSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                WorkRequest distanceWorkRequest =
//                        new OneTimeWorkRequest.Builder(RestDistanceService.class).build();
//                WorkManager
//                        .getInstance(getApplicationContext())
//                        .enqueue(distanceWorkRequest);

                WorkRequest scan2dWorkRequest =
                        new OneTimeWorkRequest.Builder(RestScan2dService.class).build();
                WorkManager
                        .getInstance(getApplicationContext())
                        .enqueue(scan2dWorkRequest);


                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(RestCtrlActivity.this);
                Intent intent = new Intent(RestCtrlActivity.this, MainViewActivity.class);
                taskStackBuilder.addNextIntentWithParentStack(intent);
                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                taskStackBuilder.startActivities();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //https://developer.android.com/training/system-ui/navigation
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


}


