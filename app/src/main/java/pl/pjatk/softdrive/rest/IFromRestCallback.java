package pl.pjatk.softdrive.rest;


import java.util.concurrent.ExecutionException;

import pl.pjatk.softdrive.rest.domain.Distance;

public interface IFromRestCallback {

    void getScan2dResponse(Float[] value);
    void getScan2dRouterIp(int partIpAddress);
    void getDistanceResponse(Distance value) throws InterruptedException, ExecutionException;
    void getDistanceRouterIp(int partIpAddress);

}
