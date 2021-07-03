package pl.pjatk.softdrive.rest;


import pl.pjatk.softdrive.rest.domain.Distance;
import pl.pjatk.softdrive.rest.domain.Scan2d;

public interface IFromRestCallback {

    void getScan2dResponse(Scan2d value);
    void getDistanceResponse(Distance value);


}
