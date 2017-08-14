package ru.yamblz.weather.data.model.googlePlacesResponse.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeometryDto {
    @SerializedName("location")
    @Expose
    private LocationDto location;

    public GeometryDto() {
    }

    public GeometryDto(LocationDto location) {
        this.location = location;
    }

    public LocationDto getLocation() {
        return location;
    }
}
