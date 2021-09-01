//package pl.pjatk.softdrive.rest.controllers;
//
//import android.os.Build;
//import android.util.Log;
//
//import androidx.annotation.RequiresApi;
//
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executor;
//
//import pl.pjatk.softdrive.rest.IFromRestCallback;
//import pl.pjatk.softdrive.rest.domain.Distance;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Controller for read actual distance from remote sensor
// * @author Dominik Stec
// * @see RestCtrl
// * @see Callback
// * @see Distance
// */
//public class RestDistanceCtrl extends RestCtrl implements Callback<Distance> {
//
//    private Distance distance;
//
//    private  pl.pjatk.softdrive.rest.IFromRestCallback IFromRestCallback;
//
//    private int fourthIp = 2;
//    private int ipAddrStart;
//    private int ipAddrEnd;
//
//    private Executor executor;
//
//    public RestDistanceCtrl() {}
//
//    /**
//     * Initialize valid state of object
//     * @param executor Threads
//     * @param ipAddrStart start find IP address from this value
//     * @param ipAddrEnd stop find IP address forward this value
//     * @param IFromRestCallback Callback interface for rest data read and write into database
//     */
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public RestDistanceCtrl(Executor executor, int ipAddrStart, int ipAddrEnd, IFromRestCallback IFromRestCallback) {
//
//        this.executor = executor;
//
//        //callbacks intialization
//        this.IFromRestCallback = IFromRestCallback;
//
//
//        this.ipAddrStart = ipAddrStart;
//        this.ipAddrEnd = ipAddrEnd;
//        fourthIp = ipAddrStart;
//
//                // part 3 byte 123.123.123.fouthIp ip address preparation
//                prepareIp(fourthIp);
//
//
//        distance = new Distance();
//
//    }
//
//    /**
//     * Concat parts of IP address for set full IP address
//     * @param fourthIp four part of full IP address
//     * @return this class reference to this class object
//     */
////    @RequiresApi(api = Build.VERSION_CODES.N)
////    public RestDistanceCtrl prepareIp(int fourthIp) {
////        ip = new FindAddressIp();
////        thirdPartIp = ip.getIp();
////
////        DISTANCE_URL = "";
////        DISTANCE_URL += protocol;
////        DISTANCE_URL += thirdPartIp;
////        DISTANCE_URL += String.valueOf(fourthIp);
////        DISTANCE_URL += portDistance;
////
////        return this;
////    }
//
//    /**
//     * Initialization Retrofit library components
//     * @return this class reference to this class object
//     */
//    public RestDistanceCtrl prepareCall() {
//        start();
//        return this;
//    }
//
//    /**
//     * Start read data by rest callback
//     */
//    public void call() {
//        Call<Distance> call = restApiDistance.getDistanceEndpoint("application/json");
//        call.enqueue(this);
//    }
//
//    /**
//     * Read response from rest API and transfer it by callback interface
//     * @param call object for manipulate call properties
//     * @param response object for store response data from rest API
//     */
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    public void onResponse(Call<Distance> call, Response<Distance> response) {
//
//        Log.v("Response callback", "call callback");
//
//        // parse response to JSON
//        getGson().toJson(response.body());
//
//        if (response.isSuccessful()) {
//
//            // run with thread
//            if(executor!=null) {
//
//                executor.execute(new Runnable() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void run() {
//
//                        // prepare correct ip of remote host with rest api
//                        String ip3b = prepareIp(fourthIp).ip.getIp();
//
//                        // set callback for fourth byte of correct IP address
//                        IFromRestCallback.getDistanceRouterIp(fourthIp);
//                        try {
//                            // set callback for read distance from rest API remote host
//                            IFromRestCallback.getDistanceResponse(response.body());
//                        } catch (InterruptedException | ExecutionException e) {
//                            e.printStackTrace();
//                        }
//
//                        call.cancel();
//
//                        return;
//                    }
//                });
//
//                // run without thread
//            } else {
//
//                String ip3b = prepareIp(fourthIp).ip.getIp();
//
//                IFromRestCallback.getDistanceRouterIp(fourthIp);
//
//                try {
//                    IFromRestCallback.getDistanceResponse(response.body());
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//
//                call.cancel();
//
//                return;
//
//            }
//        }
//    }
//
//    /**
//     * Run if IP address not exist
//     * @param call object for manipulate call properties
//     * @param t exception handler
//     */
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    public void onFailure(Call<Distance> call, Throwable t){
//        Log.e("try find IP", "IP not exist");
//
//        // hold call reference for next IP address read try
//        call = call.clone();
//
//        // prepare for call rest API
//        setUrl(String.valueOf(fourthIp));
//        updateRetrofit();
//
//        // renew with new IP address value
//        fourthIp++;
//
//        // end of ip address range
//        if(fourthIp > this.ipAddrEnd) {
//            call.cancel();
//            return;
//        }
//
//        // prepare for call rest API and call
//        this.prepareIp(fourthIp).prepareCall().call();
//    }
//}
//
