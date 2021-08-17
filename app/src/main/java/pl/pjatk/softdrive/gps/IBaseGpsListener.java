package pl.pjatk.softdrive.gps;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Interface for implementation of handle GPS events
 * @author Dominik Stec
 * Imlementation come from following link
 * @link https://stackoverflow.com/questions/15570542/determining-the-speed-of-a-vehicle-using-gps-in-android [17.08.2021]
 */
public interface IBaseGpsListener extends LocationListener, GpsStatus.Listener {

    void onLocationChanged(Location location);

    void onProviderDisabled(String provider);

    void onProviderEnabled(String provider);

    void onStatusChanged(String provider, int status, Bundle extras);

    void onGpsStatusChanged(int event);

}