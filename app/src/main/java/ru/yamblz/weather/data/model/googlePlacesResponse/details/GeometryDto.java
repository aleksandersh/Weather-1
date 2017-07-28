package ru.yamblz.weather.data.model.googlePlacesResponse.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksanderSh on 27.07.2017.
 */

public class GeometryDto {
    @SerializedName("location")
    @Expose
    private LocationDto location;

    public LocationDto getLocation() {
        return location;
    }
}
