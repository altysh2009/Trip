package com.project.altysh.firebaseloginandsaving.dto;

import java.util.ArrayList;

/**
 * Created by Altysh on 3/7/2018.
 */

public class Trip_DTO {
    private int id;
    private String tripName;
    private String startPoint;
    private double StartLatitude;
    private double StartLongitude;
    private ArrayList<String> endPoint;
    private ArrayList<Double> EndLatitude;
    private ArrayList<Double> EndLongitude;
    private long DateTime;
    private ArrayList<String> tripNotes;
    private boolean TripType;
    private String imageWithRoute;
    private String imageWithoutRoute;

    public Trip_DTO()

    {
        endPoint = new ArrayList<>();
        tripNotes = new ArrayList<>();
        tripName = "";
        startPoint = "";
    }

    public String getImageWithRoute() {
        return imageWithRoute;
    }

    public void setImageWithRoute(String imageWithRoute) {
        this.imageWithRoute = imageWithRoute;
    }

    public String getImageWithoutRoute() {
        return imageWithoutRoute;
    }

    public void setImageWithoutRoute(String imageWithoutRoute) {
        this.imageWithoutRoute = imageWithoutRoute;
    }

    public double getStartLatitude() {
        return StartLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        StartLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return StartLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        StartLongitude = startLongitude;
    }

    public ArrayList<Double> getEndLatitude() {
        return EndLatitude;
    }

    public void setEndLatitude(ArrayList<Double> endLatitude) {
        EndLatitude = endLatitude;
    }

    public ArrayList<Double> getEndLongitude() {
        return EndLongitude;
    }

    public void setEndLongitude(ArrayList<Double> endLongitude) {
        EndLongitude = endLongitude;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public ArrayList<String> getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(ArrayList<String> endPoint) {
        this.endPoint = endPoint;
    }

    public long getDateTime() {
        return DateTime;
    }

    public void setDateTime(long dateTime) {
        DateTime = dateTime;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public ArrayList<String> getTripNotes() {
        return tripNotes;
    }

    public void setTripNotes(ArrayList<String> tripNotes) {
        this.tripNotes = tripNotes;
    }

    public boolean isTripType() {
        return TripType;
    }

    public void setTripType(boolean tripType) {
        TripType = tripType;
    }
}

