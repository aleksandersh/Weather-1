package ru.yamblz.weather.data.model.googlePlacesResponse.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceDetailsDto {
    @SerializedName("geometry")
    @Expose
    private GeometryDto geometry;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("formatted_address")
    @Expose
    private String address;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("reference")
    @Expose
    private String reference;

    public PlaceDetailsDto() {
    }

    public PlaceDetailsDto(GeometryDto geometry, String name, String address, String placeId, String reference) {
        this.geometry = geometry;
        this.name = name;
        this.address = address;
        this.placeId = placeId;
        this.reference = reference;
    }

    public GeometryDto getGeometry() {
        return geometry;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getReference() {
        return reference;
    }
}
