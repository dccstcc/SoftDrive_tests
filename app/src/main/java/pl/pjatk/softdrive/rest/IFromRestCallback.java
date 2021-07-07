package pl.pjatk.softdrive.rest;


import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;

public interface IFromRestCallback {

    void getScan2dResponse(Float[] value);
    void getScan2dRouterIp(int partIpAddress);
    void getDistanceResponse(Distance value);
    void getDistanceRouterIp(int partIpAddress);
}
