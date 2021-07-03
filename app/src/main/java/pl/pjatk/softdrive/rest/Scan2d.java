package pl.pjatk.softdrive.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Scan2d implements Serializable {

    @SerializedName("")
    @Expose
    Float[] scan2d;

    public Float[] getScan2d() {
        return scan2d;
    }

    public void setScan2d(Float[] scan2d) {
        this.scan2d = scan2d;
    }
}
