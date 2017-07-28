package ru.yamblz.weather.data.model.googlePlacesResponse.prediction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AleksanderSh on 26.07.2017.
 * <p>
 * Объект передачи данных предпологаемого места поиска сервиса Google Places. Некоторые,
 * поставляемые сервисом поля исключены из объекта.
 */

public class PredictionDto {
    @SerializedName("description")
    @Expose
    private String mDescription;
    @SerializedName("id")
    @Expose
    private String mId;
    @SerializedName("place_id")
    @Expose
    private String mPlaceId;
    @SerializedName("reference")
    @Expose
    private String mReference;
    @SerializedName("matched_substrings")
    @Expose
    private List<MatchedSubstringDto> matchedSubstrings = null;
    @SerializedName("terms")
    @Expose
    private List<TermDto> terms = null;
    @SerializedName("types")
    @Expose
    private List<String> mTypes = null;

    public String getDescription() {
        return mDescription;
    }

    public String getId() {
        return mId;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public List<MatchedSubstringDto> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    public List<TermDto> getTerms() {
        return terms;
    }

    public String getReference() {
        return mReference;
    }

    public List<String> getTypes() {
        return mTypes;
    }
}
