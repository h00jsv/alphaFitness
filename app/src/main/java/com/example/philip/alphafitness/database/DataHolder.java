package com.example.philip.alphafitness.database;

import java.util.Date;

public class DataHolder {
    private Double longitude;
    private Double latitude;
    private Date date;
    private Integer id;

    public DataHolder(Double longitude, Double latitude, Date date, Integer id) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
