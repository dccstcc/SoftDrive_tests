package pl.pjatk.softdrive.rest;


import java.util.concurrent.ExecutionException;

import pl.pjatk.softdrive.rest.domain.Distance;

/**
 * Read data from rest as callbacks
 */
public interface IFromRestCallback {

    void getScan2dResponse(Float[] value);
    void getScan2dRouterIp(int partIpAddress);

    /**
     * Read distance from rest API
     * @param value distance domain object
     * @throws InterruptedException
     * @throws ExecutionException
     */
    void getDistanceResponse(Distance value) throws InterruptedException, ExecutionException;

    /**
     * Read fourth byte of remote host IP address
     * @param partIpAddress fourth byte of IP address
     */
    void getDistanceRouterIp(int partIpAddress);

}
