package ru.yamblz.weather.data.usecase;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.weather.Weather;

public interface OverviewUseCase {
    /**
     * @param location Локация.
     * @param lang     Локализация.
     * @param force    Загрузка из сети.
     * @return Погода по заданным параметрам.
     */
    Single<Weather> loadWeather(Location location, String lang, boolean force);

    /**
     * @return Текущая установленная в приложении локация.
     */
    Observable<Location> getCurrentLocation();

    /**
     * @param latitude  Широта
     * @param longitude Долгота
     * @param lang      Локализация
     * @return Город по координатам.
     */
    Single<City> getCityByCoordinates(double latitude, double longitude, String lang);

    /**
     * Устанавливает локации флаг избранной.
     *
     * @param location Локация.
     * @param favorite Флаг.
     * @return Completable.
     */
    Completable setFavorite(Location location, boolean favorite);
}
