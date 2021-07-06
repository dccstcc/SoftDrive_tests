package pl.pjatk.softdrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class ReadDataActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        BroadcastReceiver distanceReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle notificationData = intent.getExtras();
                assert notificationData != null;
                String newData  = notificationData.getString("DistanceData");

                System.out.println("test from activity " + newData);

            }
        };


        IntentFilter intentFilter = new IntentFilter("SendDistanceDataAction");
        registerReceiver(distanceReceiver, intentFilter);

        setContentView(R.layout.activity_read_data);

    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
        //unregisterReceiver(serviceReceiver);
    }

}
