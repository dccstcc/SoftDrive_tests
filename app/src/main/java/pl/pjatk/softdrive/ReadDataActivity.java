package pl.pjatk.softdrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ReadDataActivity extends AppCompatActivity {

    TextView distanceTxt;
    Button distanceBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_read_data);

        distanceBtn = (Button) findViewById(R.id.distanceBtn);
        distanceTxt = (TextView) findViewById(R.id.distanceTextView);

        BroadcastReceiver distanceReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle notificationData = intent.getExtras();
                assert notificationData != null;
                String distance  = notificationData.getString("DistanceData");
                System.out.println("test from activity " + distance);

                String distanceIp  = notificationData.getString("PartIpData");
                System.out.println("test from activity ip data " + distanceIp);

            }
        };


        IntentFilter intentFilterDistance = new IntentFilter("SendDistanceDataAction");
        registerReceiver(distanceReceiver, intentFilterDistance);

        IntentFilter intentFilterDistanceIp = new IntentFilter("SendPartIpDataAction");
        registerReceiver(distanceReceiver, intentFilterDistanceIp);


        View.OnClickListener distanceListener =  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("on click listener");
                distanceTxt.setText("after call");
            }
        };

        distanceBtn.setOnClickListener(distanceListener);
        //distanceBtn.callOnClick();

    }





    @Override
    protected void onDestroy() {

        super.onDestroy();
        //unregisterReceiver(serviceReceiver);
    }

}
