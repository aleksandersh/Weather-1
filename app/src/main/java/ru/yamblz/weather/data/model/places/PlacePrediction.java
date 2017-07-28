package ru.yamblz.weather.data.model.places;

/**
 * Created by AleksanderSh on 25.07.2017.
 * <p>
 * Предположительное искомое место в сервисе Google Places.
 */

public class PlacePrediction {
    private String mId;
    private String mName;
    private String mText;

    public PlacePrediction(String id, String name, String text) {
        mId = id;
        mName = name;
        mText = text;
    }

    /**
     * @return Идентификатор места в сервисе Google Places.
     */
    public String getId() {
        return mId;
    }

    /**
     * @return Наименование местоположения, зависит от запроса.
     */
    public String getName() {
        return mName;
    }

    /**
     * @return Текстовое описание места.
     */
    public String getText() {
        return mText;
    }
}
