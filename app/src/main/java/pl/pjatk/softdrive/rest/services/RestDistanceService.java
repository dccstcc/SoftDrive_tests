package pl.pjatk.softdrive.rest.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;

public class RestDistanceService extends IntentService {

    String routerIp = "no read";

    public RestDistanceService(){
        super("RestDistanceService");
    }

    private String FindRouterIp(int ratio) {

        int startIp = 1;
        int endIp = startIp + ratio-1;

        for(;endIp<255;) {

            new RestDistanceCtrl(startIp, endIp, new IFromRestCallback() {

                @Override
                public void getScan2dResponse(Scan2d value) {

                }

                @Override
                public void getDistanceResponse(Distance value) {
                    // send broadcast distance data after call RestDistanceCtrl
                    System.out.println("distance was found : " + value.getDistance());

                    sendDistance(String.valueOf(value.getDistance()));

                }

                @Override
                public void getDistanceRouterIp(int partIpAddress) {
                    // send broadcast 4th part of ip address
                    System.out.println("part ip was found : " + partIpAddress);
                    sendPartIp(String.valueOf(partIpAddress));
                }

            }).prepareCall().call();

            startIp += ratio;
            endIp += ratio;
            if(endIp>255) endIp=255;
            if(startIp>254) startIp=254;
        }

        return routerIp;
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        FindRouterIp(1);

    }

    private void sendDistance(String distance){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("SendDistanceDataAction");
        broadcastIntent.putExtra("DistanceData", distance);
        sendBroadcast(broadcastIntent);
    }

    private void sendPartIp(String partIp){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("SendPartIpDataAction");
        broadcastIntent.putExtra("PartIpData", partIp);
        sendBroadcast(broadcastIntent);
    }

    public void scheduleAlarm(Long timeToFetchData)
    {
        final String TAG = "schedulerAlarm:WakeLogTag";
        // acquire wakelock
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.acquire( /*10 minutes*/);
        try{
            // The time at which the alarm will be scheduled.
            // example every 30 secs = 30000 long as time
            Long time = timeToFetchData;

            // Create an Intent and set the class that will execute when the Alarm triggers. Here we have
            // specified AlarmReceiver in the Intent. The onReceive() method of this class will execute when the broadcast from your alarm is received.
            Intent intentAlarm = new Intent(getApplicationContext(), AlarmReceiver.class);

            // Get the Alarm Service.
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            // Set the alarm for a particular time.
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(getApplicationContext(), 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            Toast.makeText(getApplicationContext(), "Alarm Scheduled for 30 seconds", Toast.LENGTH_LONG).show();
        }catch(Exception ex){}
        finally{
            // release wake lock
            mWakeLock.release();

        }
    }

    public class AlarmReceiver extends BroadcastReceiver {

        public AlarmReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent)
        {

            // Your code to execute when the alarm triggers
            // and the broadcast is received.
            // perform your operations here.

            // we need to reschedule again
            // set the timeToFetchData
            // call scheduleAlarm

            Bundle notificationData = intent.getExtras();
            assert notificationData != null;
            String newData  = notificationData.getString("DistanceData");

            System.out.println("test from service time " + newData);

            ////timeToFetchData = 5000;
            //scheduleAlarm(10000L);
        }
    }

    public AlarmReceiver alarmReceiverConstruct() {
        return new AlarmReceiver();
    }

}