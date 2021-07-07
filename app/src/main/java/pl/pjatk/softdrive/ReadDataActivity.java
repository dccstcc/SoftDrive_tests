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

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;


public class ReadDataActivity extends AppCompatActivity {

    TextView distanceTxt;
    Button distanceBtn;

    String distanceIp;
    int distance;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_read_data);

        distanceBtn = (Button) findViewById(R.id.distanceBtn);
        distanceTxt = (TextView) findViewById(R.id.distanceTextView);

        // proper ip address receiver for distance read from rest service after proper ip search
        BroadcastReceiver properIpDistanceReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle notificationData = intent.getExtras();
                assert notificationData != null;

                // set global value of ip
                distanceIp  = notificationData.getString("PartIpData");
                System.out.println("read proper ip ######## " + distanceIp);

            }
        };
        // register proper ip address receiver
        IntentFilter intentFilterDistance = new IntentFilter("SendPartIpDataAction");
        registerReceiver(properIpDistanceReceiver, intentFilterDistance);


        // listener for distance read on demand from rest by button onClick simulation
        View.OnClickListener distanceListener =  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("on click listener");

                IntentFilter intentFilterDistanceIp = new IntentFilter("SendPartIpDataAction");

                // receive distance by proper ip address from rest call
                BroadcastReceiver distanceIpReceiver = new BroadcastReceiver() {

                    @Override
                    public void onReceive(Context context, Intent intent) {

                        Bundle notificationData = intent.getExtras();
                        assert notificationData != null;

                        // read proper ip address
                        distanceIp  = notificationData.getString("PartIpData");
                        System.out.println("test from activity ip data " + distanceIp);
                        int distanceIpInt = Integer.parseInt(distanceIp);

                        // call rest service for read distance data by proper ip
                        new RestDistanceCtrl(distanceIpInt, distanceIpInt, new IFromRestCallback() {

                            @Override
                            public void getScan2dResponse(Scan2d value) {

                            }

                            @Override
                            public void getDistanceResponse(Distance value) {
                                // send broadcast distance data after call RestDistanceCtrl
                                System.out.println("distance was found !!! : " + value.getDistance());
                                //sendDistance(String.valueOf(value.getDistance()));

                                // global value assign
                                distance = value.getDistance();
                            }

                            @Override
                            public void getDistanceRouterIp(int partIpAddress) {
                                // send broadcast 4th part of ip address
                                //System.out.println("part ip was found ???? : " + partIpAddress);


                            }

                        }).prepareCall().call();

                    }
                };

                // register data receiver for read distance on demand
                registerReceiver(distanceIpReceiver, intentFilterDistanceIp);

                // call broadcast send the ip
                sendPartIp(distanceIp);

                distanceTxt.setText("after call: " + distance);
            }
        };

        // onClick button simulation for get distance data on demand
        distanceBtn.setOnClickListener(distanceListener);
        //distanceBtn.callOnClick();

    }

    private void sendPartIp(String partIp){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("SendPartIpDataAction");
        broadcastIntent.putExtra("PartIpData", partIp);
        sendBroadcast(broadcastIntent);
    }

    private void sendDistance(String distance){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("SendDistanceDataAction");
        broadcastIntent.putExtra("DistanceData", distance);
        sendBroadcast(broadcastIntent);
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        //unregisterReceiver(serviceReceiver);
    }

}
