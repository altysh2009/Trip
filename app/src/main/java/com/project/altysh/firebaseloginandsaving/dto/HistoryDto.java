package com.project.altysh.firebaseloginandsaving.dto;

/**
 * Created by Altysh on 3/12/2018.
 */

public class HistoryDto {
    private Trip_DTO trip_dto;
    private long durtation;
    private long distance;
    private long avgSpeed;

    public HistoryDto() {
    }

    public Trip_DTO getTrip_dto() {
        return trip_dto;
    }

    public void setTrip_dto(Trip_DTO trip_dto) {
        this.trip_dto = trip_dto;
    }

    public long getDurtation() {
        return durtation;
    }

    public void setDurtation(long durtation) {
        this.durtation = durtation;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public long getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(long avgSpeed) {
        this.avgSpeed = avgSpeed;
    }
}
