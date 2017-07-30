package ru.yamblz.weather.data.model.googlePlacesResponse.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksanderSh on 27.07.2017.
 */

public class PlaceDetailsResponseDto {
    @SerializedName("result")
    @Expose
    private PlaceDetailsDto placeDetails;
    @SerializedName("status")
    @Expose
    private String status;

    public PlaceDetailsResponseDto() {
    }

    public PlaceDetailsResponseDto(PlaceDetailsDto placeDetails, String status) {
        this.placeDetails = placeDetails;
        this.status = status;
    }

    public PlaceDetailsDto getPlaceDetails() {
        return placeDetails;
    }

    public String getStatus() {
        return status;
    }
}
