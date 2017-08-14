package ru.yamblz.weather.data.usecase.places;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.places.PlacePrediction;

public interface CitiesUseCase {
    /**
     * Получение предполагаемых городов по тексту.
     *
     * @param text Текст, содержащийся в названии города.
     * @return Rx-источник списка предполагаемых мест.
     */
    Single<List<PlacePrediction>> loadPlacePredictions(String text);

    /**
     * Подгружает и сохраняет необходимые данные о новом местоположении.
     *
     * @param prediction Предполагаемое местоположение.
     * @return Rx-источник с информацией об успешности операции.
     */
    Completable setCurrentLocationByPrediction(PlacePrediction prediction, String lang);

    /**
     * @param city Город, который устанавливается текущим.
     */
    void setCurrentLocationByCity(City city);

    /**
     * @return Текущая установленная в приложении локация.
     * @see AppPreferenceManager#getLocation()
     * @see #getCurrentCity(String)
     * @deprecated
     */
    @Deprecated
    Single<Location> getCurrentLocation();

    /**
     * @param lang Язык, на котором необходимо получить данные.
     * @return Данные об установленном городе.
     */
    Single<City> getCurrentCity(String lang);

    /**
     * @param lang Язык, на котором необходимо получить данные.
     * @return Список избранных городов.
     */
    Single<List<City>> getFavoriteCities(String lang);
}
