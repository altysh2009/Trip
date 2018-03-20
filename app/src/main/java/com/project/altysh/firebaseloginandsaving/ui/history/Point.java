package com.project.altysh.firebaseloginandsaving.ui.history;

import java.io.Serializable;

/**
 * Created by Altysh on 3/14/2018.
 */

public class Point implements Serializable {
    private double longtute;
    private double latitue;
    private long time;

    public Point(double longtute, double latitue, long time) {
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
