package pl.pjatk.softdrive.gps;

import android.location.Location;

/**
 * Controller for Location object from GPS service
 * @author Dominik Stec
 * Customized by author version come from following link
 * @link https://stackoverflow.com/questions/15570542/determining-the-speed-of-a-vehicle-using-gps-in-android [17.08.2021]
 */
public class CLocation extends Location {

    private boolean bUseMetricUnits = true;

    public CLocation(Location location, boolean bUseMetricUnits) {
        super(location);
        this.bUseMetricUnits = bUseMetricUnits;
    }

    public boolean getUseMetricUnits()
    {
        return this.bUseMetricUnits;
    }

    public void setUseMetricunits(boolean bUseMetricUntis)
    {
        this.bUseMetricUnits = bUseMetricUntis;
    }

    @Override
    public float distanceTo(Location dest) {
        float nDistance = super.distanceTo(dest);
        if(!this.getUseMetricUnits())
        {
            //Convert meters to feet
            nDistance = nDistance * 3.28083989501312f;
        }
        return nDistance;
    }

    @Override
    public float getAccuracy() {
        float nAccuracy = super.getAccuracy();
        if(!this.getUseMetricUnits())
        {
            //Convert meters to feet
            nAccuracy = nAccuracy * 3.28083989501312f;
        }
        return nAccuracy;
    }

    @Override
    public double getAltitude() {
        double nAltitude = super.getAltitude();
        if(!this.getUseMetricUnits())
        {
            //Convert meters to feet
            nAltitude = nAltitude * 3.28083989501312d;
        }
        return nAltitude;
    }

    /**
     * Actual speed from super class getter with thread synchronization
     * @return Actual speed based on GPS distance change
     */
    @Override
    public synchronized float getSpeed() {
        float nSpeed = super.getSpeed();
        if(!this.getUseMetricUnits())
        {
            //Convert meters/second to miles/hour
            nSpeed = nSpeed * 2.2369362920544f/3.6f;
        }
        return nSpeed;
    }
}
