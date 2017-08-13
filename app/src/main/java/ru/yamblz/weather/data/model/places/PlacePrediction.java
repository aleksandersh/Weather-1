package ru.yamblz.weather.data.model.places;

/**
 * Created by AleksanderSh on 25.07.2017.
 * <p>
 * Предположительное искомое место в сервисе Google Places.
 */

public class PlacePrediction {
    private final String mId;
    private final String mText;
    private final boolean mFavorite;

    public PlacePrediction(String id, String text, boolean favorite) {
        mId = id;
        mText = text;
        mFavorite = favorite;
    }

    /**
     * @return Идентификатор места в сервисе Google Places.
     */
    public String getId() {
        return mId;
    }

    /**
     * @return Текстовое описание места.
     */
    public String getText() {
        return mText;
    }

    /**
     * @return {@code true}, если место находится в списке избранного, иначе {@code false}.
     */
    public boolean isFavorite() {
        return mFavorite;
    }
}
