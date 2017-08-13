package ru.yamblz.weather.data.network.googlePlaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.yamblz.weather.BuildConfig;
import ru.yamblz.weather.data.model.converter.PlaceDetailsToCityConverter;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.PredictionDto;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.data.network.PlacesApiClient;
import ru.yamblz.weather.data.usecase.places.GooglePlacesException;

/**
 * Created by AleksanderSh on 07.08.2017.
 * <p>
 * Реализация {@link PlacesApiClient} для сервиса Google Places.
 */

public class GooglePlacesClient implements PlacesApiClient {
    private static final String PLACE_TYPE = "(cities)";
    private static final String STATUS_OK = "OK";
    private static final String STATUS_ZERO_RESULTS = "ZERO_RESULTS";
    private static final String STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    private static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    private static final String STATUS_INVALID_REQUEST = "INVALID_REQUEST";
    private static final String STATUS_NOT_FOUND = "NOT_FOUND";
    private static final String STATUS_UNKNOWN_ERROR = "UNKNOWN_ERROR";

    private final GooglePlacesApi mPlacesApi;
    private final PlaceDetailsToCityConverter mPlaceDetailsToCityConverter;

    @Inject
    public GooglePlacesClient(GooglePlacesApi placesApi, PlaceDetailsToCityConverter placeDetailsToCityConverter) {
        mPlacesApi = placesApi;
        mPlaceDetailsToCityConverter = placeDetailsToCityConverter;
    }

    public Single<List<PlacePrediction>> loadPlacePredictions(String text) {
        return mPlacesApi.getPredictions(BuildConfig.GOOGLE_PLACES_API_KEY, text, PLACE_TYPE)
                .flatMap(responseDto ->
                        Single.create(subscriber -> {
                            switch (responseDto.getStatus()) {
                                case STATUS_OK:
                                    List<PredictionDto> dtoList = responseDto.getPredictions();
                                    List<PlacePrediction> predictions = new ArrayList<>(dtoList.size());
                                    for (PredictionDto dto : dtoList) {
                                        predictions.add(new PlacePrediction(
                                                dto.getPlaceId(),
                                                dto.getDescription(),
                                                false));
                                    }

                                    subscriber.onSuccess(predictions);
                                    break;
                                case STATUS_ZERO_RESULTS:
                                    subscriber.onSuccess(Collections.emptyList());
                                    break;
                                case STATUS_OVER_QUERY_LIMIT:
                                    subscriber.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.OVER_QUERY_LIMIT));
                                    break;
                                case STATUS_REQUEST_DENIED:
                                    subscriber.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.REQUEST_DENIED));
                                    break;
                                case STATUS_INVALID_REQUEST:
                                    subscriber.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.INVALID_REQUEST));
                                    break;
                                default:
                                    subscriber.onError(null);
                            }
                        })
                );
    }

    public Single<City> loadCityByPlaceId(String placeId, String lang) {
        return mPlacesApi.getPlaceDetails(BuildConfig.GOOGLE_PLACES_API_KEY, placeId, lang)
                .flatMap(placeDetailsDto ->
                        Single.create(emitter -> {
                            switch (placeDetailsDto.getStatus()) {
                                case STATUS_OK:
                                    City city = mPlaceDetailsToCityConverter
                                            .convert(placeDetailsDto.getPlaceDetails())
                                            .setLang(lang)
                                            .build();
                                    emitter.onSuccess(city);
                                    break;
                                case STATUS_ZERO_RESULTS:
                                    emitter.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.NOT_FOUND));
                                    break;
                                case STATUS_OVER_QUERY_LIMIT:
                                    emitter.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.OVER_QUERY_LIMIT));
                                    break;
                                case STATUS_REQUEST_DENIED:
                                    emitter.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.REQUEST_DENIED));
                                    break;
                                case STATUS_INVALID_REQUEST:
                                    emitter.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.INVALID_REQUEST));
                                    break;
                                case STATUS_NOT_FOUND:
                                    emitter.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.NOT_FOUND));
                                    break;
                                case STATUS_UNKNOWN_ERROR:
                                    emitter.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.UNKNOWN_ERROR));
                                    break;
                                default:
                                    emitter.onError(null);
                            }
                        })
                );
    }
}
