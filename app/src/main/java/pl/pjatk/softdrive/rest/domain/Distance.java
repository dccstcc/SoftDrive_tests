package pl.pjatk.softdrive.rest.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Distance mapping value from rest API
 * @author Dominik Stec
 */
public class Distance implements Serializable {

    @SerializedName("distance")
    @Expose
    public int distance;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
