package com.project.altysh.firebaseloginandsaving.dto;

/**
 * Created by Altysh on 3/12/2018.
 */

public class PointL {
    private double lon;

    private double lan;

    public PointL(double lon, double lan) {
        this.lon = lon;
        this.lan = lan;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }
}
