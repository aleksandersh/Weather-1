package ru.yamblz.weather.data.model.googlePlacesResponse.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationDto {
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    public LocationDto() {
    }

    public LocationDto(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
