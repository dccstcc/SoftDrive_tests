package pl.pjatk.softdrive.database;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;


public class ReadRestData extends AppCompatActivity {

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    TextView distanceTxt;
    Button distanceBtn;

    String distanceIp;
    int distance;

    String scan2dIp;
    float[] scan2d;

    ExecutorService executorService;

    Intent i;
    int ip;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        i = getIntent();
        ip = i.getIntExtra("ProperIPDistance", 0);

        executorService = Executors.newFixedThreadPool(1);

        new RestDistanceCtrl(executorService, ip, ip, new IFromRestCallback() {

            @Override
            public void getScan2dResponse(Float[] value) {

            }

            @Override
            public void getDistanceResponse(Distance value) {

                System.out.println("correct distance was found : " + value.getDistance());


            }

            @Override
            public void getDistanceRouterIp(int partIpAddress) {

            }

            @Override
            public void getScan2dRouterIp(int partIpAddress) {

            }

//            }).prepareCall().call();
        }).prepareCall().call();

//        setContentView(R.layout.activity_read_data);
//
//        executorService = Executors.newFixedThreadPool(1);
//
//
//        distanceBtn = (Button) findViewById(R.id.distanceBtn);
//        distanceTxt = (TextView) findViewById(R.id.distanceTextView);
//
//        Intent ip;
//
//        // proper ip address receiver for distance read from rest service after proper ip search
//        BroadcastReceiver properIpDistanceReceiver = new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                Bundle notificationData = intent.getExtras();
//                assert notificationData != null;
//
//                // set global value of ip for firstSendPoperIp(ip) method
//                distanceIp  = notificationData.getString("PartIpData");
//            }
//        };
//        // register proper ip address receiver
//        IntentFilter intentFilterDistance = new IntentFilter("SendPartIpDataAction");
//        registerReceiver(properIpDistanceReceiver, intentFilterDistance);
//
//
//        // listener for distance read on demand from rest by button onClick simulation
//        View.OnClickListener distanceListener =  new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // for read proper ip broadcast
//                IntentFilter intentFilterDistanceIp = new IntentFilter("SendPartIpDataAction");
//
//                // receive distance by proper ip address from rest call
//                BroadcastReceiver distanceIpReceiver = new BroadcastReceiver() {
//
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//
//                        Bundle notificationData = intent.getExtras();
//                        assert notificationData != null;
//
//                        // read proper ip address from firstSendPoperIp(ip)
//                        distanceIp  = notificationData.getString("PartIpData");
//                        int distanceIpInt = Integer.parseInt(distanceIp);
//
//                        // call rest service for read distance data by proper ip
//                        new RestDistanceCtrl(executorService, distanceIpInt, distanceIpInt, new IFromRestCallback() {
//
//
//                            @Override
//                            public void getScan2dResponse(Float[] value) {
//
//                            }
//
//                            @Override
//                            public void getDistanceResponse(Distance value) {
//                                // global value assign
//                                distance = value.getDistance();
//                            }
//
//                            @Override
//                            public void getDistanceRouterIp(String partIpAddress) {
//
//                            }
//
//                            @Override
//                            public void getScan2dRouterIp(int partIpAddress) {
//
//                            }
//
////                        }).prepareCall().call();
//                        }).prepareCall().call();
//
//                    }
//                };
//
//                // register data receiver for read distance on demand
//                registerReceiver(distanceIpReceiver, intentFilterDistanceIp);
//
//                // run all receiver callback logic
//                // call once broadcast send the proper ip
//                firstSendPoperIp(distanceIp);
//
//                // distance is here
//                distanceTxt.setText("after call: " + distance);
//
//                System.out.println("distance !!!!!!!!! : " + distance);
//
//
//
//                //run view activity
//                //Intent drawView = new Intent(getApplicationContext(), MainViewActivity.class);
//                //startActivity(drawView);
//
//            }
//        };
//
//        // onClick button simulation for get distance data on demand
//        distanceBtn.setOnClickListener(distanceListener);
//        //distanceBtn.callOnClick();
//
//
//    }
//
//    private void firstSendPoperIp(String partIp){
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("SendPartIpDataAction");
//        broadcastIntent.putExtra("PartIpData", partIp);
//        sendBroadcast(broadcastIntent);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
//    }

    }
}
