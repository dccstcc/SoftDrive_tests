package pl.pjatk.softdrive;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import pl.pjatk.softdrive.rest.services.RestDistanceService;

//https://stackoverflow.com/questions/25332078/how-to-the-send-data-to-the-activity-from-background-service/25332288
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

//    public class ServiceToActivity extends BroadcastReceiver
//    {
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//            Bundle notificationData = intent.getExtras();
//            assert notificationData != null;
//            String newData  = notificationData.getString("DistanceData");
//
//            System.out.println("test from activity");
//            // newData is from the service
//
//        }
//    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        //unregisterReceiver(serviceReceiver);

    }


//    public class AlarmReceiver extends  {
//
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//
//            // Your code to execute when the alarm triggers
//            // and the broadcast is received.
//            // perform your operations here.
//
//            // we need to reschedule again
//            // set the timeToFetchData
//            // call scheduleAlarm
//
//            Bundle notificationData = intent.getExtras();
//            assert notificationData != null;
//            String newData  = notificationData.getString("DistanceData");
//
//            System.out.println("test from activity " + newData);
//
//            ////timeToFetchData = 5000;
//            //scheduleAlarm(10000L);
//        }
//    }


//    public void startAlert(View view) {
//        //EditText text = (EditText) findViewById(R.id.time);
//        //int i = Integer.parseInt(text.getText().toString());
//        int i = 1;
//        Intent intent = new Intent(this, MyBroadcastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                this.getApplicationContext(), 234324243, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//                + (i * 1000), pendingIntent);
//        Toast.makeText(this, "Alarm set in " + i + " seconds",
//                Toast.LENGTH_LONG).show();
//    }
}
