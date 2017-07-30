package ru.yamblz.weather.data.usecase.places;

import android.support.annotation.StringRes;

import ru.yamblz.weather.R;

/**
 * Created by AleksanderSh on 29.07.2017.
 * <p>
 * Исключения, возникаемые при работе с сервисом Google Places.
 */

public class GooglePlacesException extends RuntimeException {
    private ErrorDescription description;

    public GooglePlacesException(ErrorDescription description) {
        this.description = description;
    }

    /**
     * @return Идентификатор ресурса с описанием ошибки.
     */
    @StringRes
    public int getDescriptionResId() {
        return description.getDescriptionResId();
    }

    /**
     * Задает тип ошибки.
     */
    public enum ErrorDescription {
        ZERO_RESULTS(R.string.error_places_zero_results),
        OVER_QUERY_LIMIT(R.string.error_places_over_query_limit),
        REQUEST_DENIED(R.string.error_places_request_denied),
        INVALID_REQUEST(R.string.error_places_invalid_request),
        NOT_FOUND(R.string.error_places_not_found),
        UNKNOWN_ERROR(R.string.error_places_unknown_error);

        @StringRes
        int descriptionResId;

        ErrorDescription(@StringRes int descriptionResId) {
            this.descriptionResId = descriptionResId;
        }

        /**
         * @return Идентификатор ресурса с описанием ошибки.
         */
        @StringRes
        public int getDescriptionResId() {
            return descriptionResId;
        }
    }
}
