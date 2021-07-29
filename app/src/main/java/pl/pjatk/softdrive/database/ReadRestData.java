package pl.pjatk.softdrive.database;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.view.MainViewActivity;


public class ReadRestData extends AppCompatActivity {

    private final int restFrequency = 2000;

    ExecutorService executorService;

    Intent i;
    int ip;

    DbManager db;

    Intent runView;

    int distance;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        i = getIntent();
        ip = i.getIntExtra("ProperIPDistance", 0);

        executorService = Executors.newFixedThreadPool(2);

        db = new DbManager(this);

        executorService.execute(new Runnable() {
            @Override
            public void run() {


                while(ip!=0) {

                    new RestDistanceCtrl(executorService, ip, ip, new IFromRestCallback() {

                        @Override
                        public void getScan2dResponse(Float[] value) {

                        }

                        @Override
                        public void getDistanceResponse(Distance value) {

                            distance = value.getDistance();

                            System.out.println("correct distance was found : " + distance);

                            db.setDistance(distance);
                            db.dbCommit();


                            //System.out.println("from db : " + db.getDbDistance());
                        }

                        @Override
                        public void getDistanceRouterIp(int partIpAddress) {
                            System.out.println("from ip address");
                        }

                        @Override
                        public void getScan2dRouterIp(int partIpAddress) {

                        }

                    }).prepareCall().call();

                    try {
                        Thread.sleep(restFrequency);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                runView = new Intent(getApplicationContext(), MainViewActivity.class);
                do {
                    if(distance!=0)
                        startActivity(runView);
                }
                while(distance==0);


            }
        });




    }
}
