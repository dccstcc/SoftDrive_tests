package pl.pjatk.softdrive.database;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.pjatk.softdrive.rest.IFromRestCallback;
import pl.pjatk.softdrive.rest.controllers.RestDistanceCtrl;
import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.view.MainViewActivity;


public class ReadRestDataService extends Worker {

    private final int restFrequency = 2000;

    ExecutorService executorService;

    Intent i;
    int ip;

    DbManager db;

    Intent runView;

    int distance;

    boolean runViewActivity;

    public ReadRestDataService(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Result doWork() {


        System.out.println("readrestdata run");


//        assert intent != null;
//        ip = intent.getIntExtra("ProperIPDistance",0);
        int ip = getInputData().getInt("ProperIPDistance",0);

        System.out.println("ProperIPDistance " + ip);

        executorService = Executors.newFixedThreadPool(1);

        db = new DbManager(getApplicationContext());

        runView = new Intent(getApplicationContext(), MainViewActivity.class);

        runViewActivity = true;

//        executorService.execute(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void run() {

//                runView = new Intent(getApplicationContext(), MainViewActivity.class);
        //runViewActivity = true;

//                do {
//                    if(distance!=0) {
//                        startActivity(runView);
//                        break;
//                    }
//                }
//                while(distance==0);

        while (ip != 0) {

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


//
//                            do {
//                                if(distance!=0) {
//                                    startActivity(runView);
//                                    break;
//                                }
//                            }
//                            while(distance==0);


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
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //startActivity(runView);

//
//                    if (runViewActivity) {
//                        runViewActivity = false;
//                        runView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        getApplicationContext().startActivity(runView);
//                    }

        }



        return Result.success();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//
//
//
//
//        //String userID = intent.getStringExtra("UserID");
//
////        assert intent != null;
////        ip = intent.getIntExtra("ProperIPDistance", 0);
////
////        executorService = Executors.newFixedThreadPool(2);
////
////        db = new DbManager(this);
////
////        runViewActivity = true;
//
//        return START_STICKY;
//
//    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//        i = getIntent();
//        ip = i.getIntExtra("ProperIPDistance", 0);
//
//        executorService = Executors.newFixedThreadPool(2);
//
//        db = new DbManager(this);
//
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//
////                runView = new Intent(getApplicationContext(), MainViewActivity.class);
//                runViewActivity = true;
//
////                do {
////                    if(distance!=0) {
////                        startActivity(runView);
////                        break;
////                    }
////                }
////                while(distance==0);
//
//                while (ip != 0) {
//
//                    new RestDistanceCtrl(executorService, ip, ip, new IFromRestCallback() {
//
//                        @Override
//                        public void getScan2dResponse(Float[] value) {
//
//                        }
//
//                        @Override
//                        public void getDistanceResponse(Distance value) {
//
//                            distance = value.getDistance();
//
//                            System.out.println("correct distance was found : " + distance);
//
//                            db.setDistance(distance);
//                            db.dbCommit();
//
//
////
////                            do {
////                                if(distance!=0) {
////                                    startActivity(runView);
////                                    break;
////                                }
////                            }
////                            while(distance==0);
//
//
//                            //System.out.println("from db : " + db.getDbDistance());
//                        }
//
//                        @Override
//                        public void getDistanceRouterIp(int partIpAddress) {
//                            System.out.println("from ip address");
//                        }
//
//                        @Override
//                        public void getScan2dRouterIp(int partIpAddress) {
//
//                        }
//
//                    }).prepareCall().call();
//
//                    try {
//                        Thread.sleep(restFrequency);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (runViewActivity) {
//                        runViewActivity = false;
////                        startActivity(runView);
//                    }
//
//                }
//
//            }
//
//        });
//
//
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
////        runView = new Intent(getApplicationContext(), MainViewActivity.class);
////        startActivity(runView);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//
//        System.out.println("readrestdata run");
//
//
//        assert intent != null;
//        ip = intent.getIntExtra("ProperIPDistance",0);
//
//        System.out.println("ProperIPDistance " + ip);
//
//        executorService = Executors.newFixedThreadPool(1);
//
//        db = new DbManager(this);
//
//        runView = new Intent(getApplicationContext(), MainViewActivity.class);
//
//        runViewActivity = true;
//
////        executorService.execute(new Runnable() {
////            @RequiresApi(api = Build.VERSION_CODES.N)
////            @Override
////            public void run() {
//
////                runView = new Intent(getApplicationContext(), MainViewActivity.class);
//                //runViewActivity = true;
//
////                do {
////                    if(distance!=0) {
////                        startActivity(runView);
////                        break;
////                    }
////                }
////                while(distance==0);
//
//                while (ip != 0) {
//
//                    new RestDistanceCtrl(executorService, ip, ip, new IFromRestCallback() {
//
//                        @Override
//                        public void getScan2dResponse(Float[] value) {
//
//                        }
//
//                        @Override
//                        public void getDistanceResponse(Distance value) {
//
//                            distance = value.getDistance();
//
//                            System.out.println("correct distance was found : " + distance);
//
//                            db.setDistance(distance);
//                            db.dbCommit();
//
//
////
////                            do {
////                                if(distance!=0) {
////                                    startActivity(runView);
////                                    break;
////                                }
////                            }
////                            while(distance==0);
//
//
//                            //System.out.println("from db : " + db.getDbDistance());
//                        }
//
//                        @Override
//                        public void getDistanceRouterIp(int partIpAddress) {
//                            System.out.println("from ip address");
//                        }
//
//                        @Override
//                        public void getScan2dRouterIp(int partIpAddress) {
//
//                        }
//
//                    }).prepareCall().call();
//
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    //startActivity(runView);
//
//
////                    if (runViewActivity) {
////                        runViewActivity = false;
////                        runView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////
////                        startActivity(runView);
////                    }
//
//                }
//
////        runView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////
////        startActivity(runView);
//
//
//    }

//        });


//        assert intent != null;
//        ip = intent.getIntExtra("ProperIPDistance", 0);
//
//        executorService = Executors.newFixedThreadPool(2);
//
//        db = new DbManager(this);
//
//        runViewActivity = true;
//
//        executorService.execute(new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void run() {
//
////                runView = new Intent(getApplicationContext(), MainViewActivity.class);
//                //runViewActivity = true;
//
////                do {
////                    if(distance!=0) {
////                        startActivity(runView);
////                        break;
////                    }
////                }
////                while(distance==0);
//
//                while (ip != 0) {
//
//                    new RestDistanceCtrl(executorService, ip, ip, new IFromRestCallback() {
//
//                        @Override
//                        public void getScan2dResponse(Float[] value) {
//
//                        }
//
//                        @Override
//                        public void getDistanceResponse(Distance value) {
//
//                            distance = value.getDistance();
//
//                            System.out.println("correct distance was found : " + distance);
//
//                            db.setDistance(distance);
//                            db.dbCommit();
//
//
////
////                            do {
////                                if(distance!=0) {
////                                    startActivity(runView);
////                                    break;
////                                }
////                            }
////                            while(distance==0);
//
//
//                            //System.out.println("from db : " + db.getDbDistance());
//                        }
//
//                        @Override
//                        public void getDistanceRouterIp(int partIpAddress) {
//                            System.out.println("from ip address");
//                        }
//
//                        @Override
//                        public void getScan2dRouterIp(int partIpAddress) {
//
//                        }
//
//                    }).prepareCall().call();
//
//                    try {
//                        Thread.sleep(restFrequency);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (runViewActivity) {
//                        runViewActivity = false;
//                        startActivity(runView);
//                    }
//
//                }
//
//            }
//
//        });
//
//
    }

