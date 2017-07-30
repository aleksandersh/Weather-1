package ru.yamblz.weather.data.model.googlePlacesResponse.prediction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksanderSh on 27.07.2017.
 * <p>
 * Поисковые слова, определяющие описание местоположение.
 */

public class TermDto {
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("value")
    @Expose
    private String value;

    public TermDto() {
    }

    public TermDto(Integer offset, String value) {
        this.offset = offset;
        this.value = value;
    }

    public Integer getOffset() {
        return offset;
    }

    public String getValue() {
        return value;
    }
}
