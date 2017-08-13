package ru.yamblz.weather.data.usecase;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.yamblz.weather.data.SchedulerProvider;
import ru.yamblz.weather.data.database.WeatherDao;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.weather.Weather;
import ru.yamblz.weather.data.network.PlacesApiClient;
import ru.yamblz.weather.data.network.WeatherApiClient;

@Singleton
public class OverviewUseCaseImpl implements OverviewUseCase {
    private SchedulerProvider schedulerProvider;
    private AppPreferenceManager appPreferenceManager;
    private WeatherDao dao;
    private WeatherApiClient apiClient;
    private PlacesApiClient placesClient;

    private Weather cache;

    @Inject
    OverviewUseCaseImpl(SchedulerProvider schedulerProvider,
                        AppPreferenceManager preferenceManager,
                        WeatherDao dao,
                        WeatherApiClient apiClient,
                        PlacesApiClient placesClient) {
        this.schedulerProvider = schedulerProvider;
        this.appPreferenceManager = preferenceManager;
        this.dao = dao;
        this.apiClient = apiClient;
        this.placesClient = placesClient;
    }

    @Override
    public Single<Weather> loadWeather(Location location, String lang, boolean force) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Single<Weather> single;

        // Постарался оставить побольше от старого метода, насколько я понимаю, такой кэш должен
        // использоваться, если force!=true. При этом убрал публикацию через RxBus, так как тогда
        // view будет обновляться несколько раз.
        if (!force
                && cache != null
                && cache.getLatitude().equals(lat)
                && cache.getLongitude().equals(lng)
                && cache.getLanguage().equals(lang)) {
            single = Single.just(cache);
        } else {
            single = force ? network(lat, lng, lang) :
                    local(lat, lng, lang)
                            .onErrorResumeNext(network(lat, lng, lang));
            single.doOnSuccess(weather -> cache = weather);
        }

        return single;
    }

    @Override
    public Observable<Location> getCurrentLocation() {
        return appPreferenceManager
                .getCurrentLocationObservable();
    }

    @Override
    public Single<City> getCityByCoordinates(double latitude, double longitude, String lang) {
        return dao.getCityByCoordinates(latitude, longitude, lang)
                .onErrorResumeNext(dao.getCityByCoordinates(latitude, longitude)
                        .flatMap(cityDb -> placesClient
                                .loadCityByPlaceId(
                                        cityDb.getGooglePlacesId(),
                                        lang)
                                .doOnSuccess(cityGp -> dao.saveCity(cityGp))))
                .compose(schedulerProvider.applyIoSchedulers());
    }

    @Override
    public Completable setFavorite(Location location, boolean favorite) {
        return Completable.fromRunnable(() ->
                dao.setFavoriteCity(location.getLatitude(), location.getLongitude(), favorite));
    }

    private Single<Weather> network(double lat, double lng, String lang) {
        return apiClient.getWeather(lat, lng, lang)
                .doOnSuccess(dao::saveWeather)
                .compose(schedulerProvider.applyIoSchedulers());
    }

    private Single<Weather> local(double lat, double lng, String lang) {
        return dao.getWeatherByCoordinates(lat, lng, lang)
                .compose(schedulerProvider.applyIoSchedulers());
    }
}
