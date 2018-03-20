package com.project.altysh.firebaseloginandsaving.dto;

import com.project.altysh.firebaseloginandsaving.ui.floatingWidgit.PointDara;

import java.util.List;

/**
 * Created by Altysh on 3/12/2018.
 */

public class HistoryDto {
    public final String DONE = "done";
    public final String CANCLED = "cancled";
    private Trip_DTO trip_dto;
    private double durtation;
    private double distance;
    private double avgSpeed;
    private long startTime;
    private long endTime;
    private String status;
    private List<Boolean> checked;
    private List<PointDara> points;

    public HistoryDto() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Boolean> getChecked() {
        return checked;
    }

    public void setChecked(List<Boolean> checked) {
        this.checked = checked;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<PointDara> getPoints() {
        return points;
    }

    public void setPoints(List<PointDara> points) {
        this.points = points;
    }

    public Trip_DTO getTrip_dto() {
        return trip_dto;
    }

    public void setTrip_dto(Trip_DTO trip_dto) {
        this.trip_dto = trip_dto;
    }

    public double getDurtation() {
        return durtation;
    }

    public void setDurtation(double durtation) {
        this.durtation = durtation;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }
}
