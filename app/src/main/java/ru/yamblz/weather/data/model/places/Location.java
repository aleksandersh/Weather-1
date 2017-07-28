package ru.yamblz.weather.data.model.places;

/**
 * Created by AleksanderSh on 26.07.2017.
 * <p>
 * Локация.
 */

public class Location {
    private String title;
    private double latitude;
    private double longitude;

    public Location(String title, double latitude, double longitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
