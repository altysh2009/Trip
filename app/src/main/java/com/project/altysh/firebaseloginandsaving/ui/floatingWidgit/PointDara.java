package com.project.altysh.firebaseloginandsaving.ui.floatingWidgit;

import java.io.Serializable;

/**
 * Created by Altysh on 3/14/2018.
 * holds the information about the gps cordnates and the time it was picked
 */

public class PointDara implements Serializable {
    private double longtute;
    private double latitue;
    private long time;

    public PointDara() {
    }

    public PointDara(double longtute, double latitue, long time) {
        this.longtute = longtute;
        this.latitue = latitue;
        this.time = time;
    }

    public double getLongtute() {
        return longtute;
    }

    public void setLongtute(double longtute) {
        this.longtute = longtute;
    }

    public double getLatitue() {
        return latitue;
    }

    public void setLatitue(double latitue) {
        this.latitue = latitue;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
