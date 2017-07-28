package ru.yamblz.weather.data.model.googlePlacesResponse.prediction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AleksanderSh on 26.07.2017.
 * <p>
 * Объект передачи подсказки мест сервиса Google Places.
 */

public class PredictionsResponseDto {
    @SerializedName("status")
    @Expose
    private String mStatus;
    @SerializedName("predictions")
    @Expose
    private List<PredictionDto> mPredictions = null;

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public List<PredictionDto> getPredictions() {
        return mPredictions;
    }

    public void setPredictions(List<PredictionDto> predictions) {
        this.mPredictions = predictions;
    }
}
