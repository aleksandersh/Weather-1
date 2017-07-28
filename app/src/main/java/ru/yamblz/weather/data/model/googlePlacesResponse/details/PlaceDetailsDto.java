package ru.yamblz.weather.data.model.googlePlacesResponse.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksanderSh on 27.07.2017.
 */

public class PlaceDetailsDto {
    @SerializedName("geometry")
    @Expose
    private GeometryDto geometry;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("reference")
    @Expose
    private String reference;

    public GeometryDto getGeometry() {
        return geometry;
    }

    public String getName() {
        return name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getReference() {
        return reference;
    }
}
