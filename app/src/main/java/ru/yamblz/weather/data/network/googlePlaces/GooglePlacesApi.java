package ru.yamblz.weather.data.network.googlePlaces;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.yamblz.weather.data.model.googlePlacesResponse.details.PlaceDetailsResponseDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.PredictionsResponseDto;

/**
 * Created by AleksanderSh on 26.07.2017.
 * <p>
 * Google Places Web Service API.
 */

public interface GooglePlacesApi {
    /**
     * Получение предполагаемых мест на основе текста.
     *
     * @param apiKey Google places Api Key.
     * @param text   Подстрока, для которой ищется совпадение в названии местоположения.
     * @param types  Ограничение типа локаций.
     * @return Rx-источник для ответа сервиса.
     */
    @GET("autocomplete/json")
    Single<PredictionsResponseDto> getPredictions(@Query("key") String apiKey,
                                                  @Query("input") String text,
                                                  @Query("types") String types);

    /**
     * Получение детальной информации по месту с заданным идентификатором.
     *
     * @param apiKey  Google places Api Key.
     * @param placeId Однозначный идентификатор места в сервиса Google Places.
     * @param lang    Язык, на котором следует получить результат.
     * @return Rx-источик для ответа сервиса.
     */
    @GET("details/json")
    Single<PlaceDetailsResponseDto> getPlaceDetails(@Query("key") String apiKey,
                                                    @Query("placeid") String placeId,
                                                    @Query("language") String lang);
}
