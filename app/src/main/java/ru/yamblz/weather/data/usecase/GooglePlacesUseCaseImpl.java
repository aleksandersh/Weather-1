package ru.yamblz.weather.data.usecase;

import java.util.ArrayList;
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

public class GooglePlacesUseCaseImpl implements GooglePlacesUseCase {
    private static final String TAG = "GooglePlacesUseCaseImpl";
    private static final String PLACE_TYPE = "(cities)";
    private static final String STATUS_OK = "OK";

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
                .subscribeOn(Schedulers.io())
                .flatMap(responseDto ->
                        Single.create(subscriber -> {
                            if (responseDto.getStatus().equals(STATUS_OK)) {
                                subscriber.onSuccess(predictionsFromResponseDto(responseDto));
                            } else {
                                subscriber.onError(null);
                            }
                        })
                );
    }

    @Override
    public Completable setCurrentLocationByPrediction(PlacePrediction prediction) {
        return mPlacesApi.getPlaceDetails(BuildConfig.GOOGLE_PLACES_API_KEY, prediction.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(placeDetailsDto ->
                        Single.create(subscriber -> {
                            if (placeDetailsDto.getStatus().equals(STATUS_OK)) {
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
                            } else {
                                subscriber.onError(null);
                            }
                        })
                ).toCompletable();
    }

    private List<PlacePrediction> predictionsFromResponseDto(PredictionsResponseDto responseDto) {
        List<PlacePrediction> predictions = new ArrayList<>();
        List<PredictionDto> predictionDtoList =
                responseDto.getPredictions();
        for (PredictionDto predictionDto : predictionDtoList) {
            // Чтобы выделить ожидаемое пользователем наименование местоположения,
            // за него принимается часть определения, соответствующая поисковому запросу.
            String name = predictionDto.getDescription();
            int offset = 0;
            List<MatchedSubstringDto> matchedSubstringDtos =
                    predictionDto.getMatchedSubstrings();
            if (matchedSubstringDtos != null && !matchedSubstringDtos.isEmpty()) {
                offset = matchedSubstringDtos.get(0).getOffset();
            }
            List<TermDto> termDtos = predictionDto.getTerms();
            if (termDtos != null && !termDtos.isEmpty()) {
                for (TermDto termDto : termDtos) {
                    if (termDto.getOffset().equals(offset)) {
                        name = termDto.getValue();
                        break;
                    }
                }
            }
            predictions.add(new PlacePrediction(
                    predictionDto.getPlaceId(),
                    name,
                    predictionDto.getDescription()));
        }
        return predictions;
    }
}
