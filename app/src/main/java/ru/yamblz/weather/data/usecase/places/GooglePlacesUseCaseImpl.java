package ru.yamblz.weather.data.usecase.places;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.yamblz.weather.data.database.WeatherDao;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.data.network.PlacesApiClient;

/**
 * Created by AleksanderSh on 25.07.2017.
 * <p>
 * Класс бизнес-логики блока выбора городов.
 */

public class GooglePlacesUseCaseImpl implements CitiesUseCase {
    private PlacesApiClient apiClient;
    private AppPreferenceManager preferenceManager;
    private WeatherDao weatherDao;

    @Inject
    public GooglePlacesUseCaseImpl(PlacesApiClient apiClient,
                                   AppPreferenceManager preferenceManager,
                                   WeatherDao weatherDao) {
        this.apiClient = apiClient;
        this.preferenceManager = preferenceManager;
        this.weatherDao = weatherDao;
    }

    @Override
    public Single<List<PlacePrediction>> loadPlacePredictions(String text) {
        return apiClient.loadPlacePredictions(text)
                .map(predictions -> {
                    for (int i = 0; i < predictions.size(); i++) {
                        PlacePrediction prediction = predictions.get(i);
                        try {
                            City city = weatherDao.getCityByGooglePlaceId(prediction.getId())
                                    .blockingGet();
                            if (city.isFavorite()) {
                                predictions.set(i, new PlacePrediction(
                                        prediction.getId(),
                                        prediction.getText(),
                                        true));
                            }
                        } catch (NoSuchElementException e) {
                            // Исключение возникает, если такой город не загружался в базу данных.
                            // Эту ситуацию обрабатывать не нужно.
                        }
                    }

                    Collections.sort(predictions, (p1, p2) -> {
                        if (p1.isFavorite() && !p2.isFavorite()) return -1;
                        else if (!p1.isFavorite() && p2.isFavorite()) return 1;
                        else return 0;
                    });

                    return predictions;
                });
    }

    @Override
    public Completable setCurrentLocationByPrediction(PlacePrediction prediction, String lang) {
        return weatherDao.getCityByGooglePlaceId(prediction.getId(), lang)
                .onErrorResumeNext(apiClient.loadCityByPlaceId(prediction.getId(), lang)
                        .doOnSuccess(city -> weatherDao.saveCity(city)))
                .doAfterSuccess(city -> preferenceManager.setCurrentLocation(
                        new Location(city.getLatitude(), city.getLongitude())))
                .toCompletable();
    }

    @Override
    public Single<Location> getCurrentLocation() {
        return Single.fromCallable(() -> preferenceManager.getLocation());
    }

    @Override
    public Single<City> getCurrentCity(String lang) {
        return Single.fromCallable(preferenceManager::getLocation)
                .flatMap(location -> weatherDao.getCityByCoordinates(
                        location.getLatitude(),
                        location.getLongitude(),
                        lang));
    }
}
