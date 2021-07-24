package pl.pjatk.softdrive.rest;


import pl.pjatk.softdrive.rest.domain.Distance;

public interface IFromRestCallback {

    void getScan2dResponse(Float[] value);
    void getScan2dRouterIp(int partIpAddress);
    void getDistanceResponse(Distance value);
//    void getDistanceRouterIp(int partIpAddress);
    void getDistanceRouterIp(int partIpAddress);

}
