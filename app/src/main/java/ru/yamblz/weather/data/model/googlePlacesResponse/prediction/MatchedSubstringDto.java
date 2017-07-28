package ru.yamblz.weather.data.model.googlePlacesResponse.prediction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksanderSh on 27.07.2017.
 * <p>
 * Информация о найденных совпадениях в тексте.
 */

public class MatchedSubstringDto {
    @SerializedName("length")
    @Expose
    private Integer length;
    @SerializedName("offset")
    @Expose
    private Integer offset;

    public Integer getLength() {
        return length;
    }

    public Integer getOffset() {
        return offset;
    }
}
