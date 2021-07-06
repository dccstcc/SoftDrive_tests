package pl.pjatk.softdrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pl.pjatk.softdrive.rest.services.RestDistanceService;

//https://stackoverflow.com/questions/25332078/how-to-the-send-data-to-the-activity-from-background-service/25332288
public class ReadDataActivity extends AppCompatActivity {

    private RestDistanceService.AlarmReceiver serviceReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_read_data);

        serviceReceiver = new RestDistanceService().alarmReceiverConstruct();
        IntentFilter intentSFilter = new IntentFilter("SendDistanceDataAction");
        registerReceiver(serviceReceiver, intentSFilter);

    }

    public class ServiceToActivity extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle notificationData = intent.getExtras();
            assert notificationData != null;
            String newData  = notificationData.getString("DistanceData");

            System.out.println("test from activity");
            // newData is from the service

        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        unregisterReceiver(serviceReceiver);

    }
}
