package ru.yamblz.weather.data.usecase.places;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.yamblz.weather.BuildConfig;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.model.googlePlacesResponse.details.LocationDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.MatchedSubstringDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.PredictionDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.PredictionsResponseDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.TermDto;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.data.network.GooglePlacesApi;

/**
 * Created by AleksanderSh on 25.07.2017.
 */

public class GooglePlacesUseCaseImpl implements CitiesUseCase {
    private static final String TAG = "GooglePlacesUseCaseImpl";
    private static final String PLACE_TYPE = "(cities)";
    private static final String STATUS_OK = "OK";
    private static final String STATUS_ZERO_RESULTS = "ZERO_RESULTS";
    private static final String STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    private static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    private static final String STATUS_INVALID_REQUEST = "INVALID_REQUEST";
    private static final String STATUS_NOT_FOUND = "NOT_FOUND";
    private static final String STATUS_UNKNOWN_ERROR = "UNKNOWN_ERROR";

    private GooglePlacesApi mPlacesApi;
    private AppPreferenceManager mPreferenceManager;

    @Inject
    public GooglePlacesUseCaseImpl(GooglePlacesApi placesApi, AppPreferenceManager preferenceManager) {
        mPlacesApi = placesApi;
        mPreferenceManager = preferenceManager;
    }

    @Override
    public Single<List<PlacePrediction>> loadPlacePredictions(String text) {
        return mPlacesApi.getPredictions(BuildConfig.GOOGLE_PLACES_API_KEY, text, PLACE_TYPE)
                .flatMap(responseDto ->
                        Single.create(subscriber -> {
                            switch (responseDto.getStatus()) {
                                case STATUS_OK:
                                    subscriber.onSuccess(predictionsFromResponseDto(responseDto));
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

    @Override
    public Completable setCurrentLocationByPrediction(PlacePrediction prediction) {
        return mPlacesApi.getPlaceDetails(BuildConfig.GOOGLE_PLACES_API_KEY, prediction.getId())
                .flatMap(placeDetailsDto ->
                        Single.create(subscriber -> {
                            switch (placeDetailsDto.getStatus()) {
                                case STATUS_OK:
                                    LocationDto locationDto = placeDetailsDto
                                            .getPlaceDetails()
                                            .getGeometry()
                                            .getLocation();
                                    Location location = new Location(
                                            prediction.getName(),
                                            locationDto.getLat(),
                                            locationDto.getLng());
                                    mPreferenceManager.setCurrentLocation(location);
                                    subscriber.onSuccess(location);
                                    break;
                                case STATUS_ZERO_RESULTS:
                                    subscriber.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.NOT_FOUND));
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
                                case STATUS_NOT_FOUND:
                                    subscriber.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.NOT_FOUND));
                                    break;
                                case STATUS_UNKNOWN_ERROR:
                                    subscriber.onError(new GooglePlacesException(
                                            GooglePlacesException.ErrorDescription.UNKNOWN_ERROR));
                                    break;
                                default:
                                    subscriber.onError(null);
                            }
                        })
                ).toCompletable();
    }

    @Override
    public Single<Location> getCurrentLocation() {
        return Single.fromCallable(() -> mPreferenceManager.getLocation());
    }

    private List<PlacePrediction> predictionsFromResponseDto(PredictionsResponseDto responseDto) {
        List<PlacePrediction> predictions = new ArrayList<>();
        List<PredictionDto> predictionDtoList =
                responseDto.getPredictions();
        if (predictionDtoList != null) {
            for (PredictionDto predictionDto : predictionDtoList) {
                String description = predictionDto.getDescription();
                String placeId = predictionDto.getPlaceId();
                String name = predictionDto.getDescription();
                if (name == null || placeId == null || description == null) continue;

                // Чтобы выделить ожидаемое пользователем наименование местоположения,
                // за него принимается часть определения, соответствующая поисковому запросу.
                int offset = 0;
                List<MatchedSubstringDto> matchedSubstringDtos =
                        predictionDto.getMatchedSubstrings();
                if (matchedSubstringDtos != null && !matchedSubstringDtos.isEmpty()) {
                    offset = matchedSubstringDtos.get(0).getOffset();

                    List<TermDto> termDtos = predictionDto.getTerms();
                    if (termDtos != null && !termDtos.isEmpty()) {
                        for (TermDto termDto : termDtos) {
                            if (termDto.getOffset().equals(offset)) {
                                name = termDto.getValue();
                                break;
                            }
                        }
                    }
                }

                predictions.add(new PlacePrediction(
                        placeId,
                        name,
                        description));
            }
        }
        return predictions;
    }
}
