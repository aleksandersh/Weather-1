package ru.yamblz.weather.data.database;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.weather.Forecast;
import ru.yamblz.weather.data.model.weather.Weather;

/**
 * Created by AleksanderSh on 03.08.2017.
 * <p>
 * Интерфейс для организации работы с хранилищем данных.
 */

public interface WeatherDao {
    /**
     * Получение экземпляра {@link City} по заданным координатам.
     *
     * @param latitude  Широта.
     * @param longitude Долгота.
     * @param lang      Идентификатор языка, на котором записаны данные о городе.
     * @return {@link Single<City>}. {@code OnError} с передачей исключения
     * {@link java.util.NoSuchElementException}, если данные о городе не найдены.
     */
    Single<City> getCityByCoordinates(double latitude, double longitude, String lang);

    /**
     * Получение экземпляра {@link City} по заданным координатам. Данные локализации
     * могут отсутствовать.
     *
     * @param latitude  Широта.
     * @param longitude Долгота.
     * @return {@link Single<City>}. {@code OnError} с передачей исключения
     * {@link java.util.NoSuchElementException}, если данные о городе не найдены.
     */
    Single<City> getCityByCoordinates(double latitude, double longitude);

    /**
     * Получение экземпляра {@link City} по заданному идентификатору.
     *
     * @param placeId Идентификатор города в Google Places.
     * @param lang    Идентификатор языка, на котором записаны данные о городе.
     * @return {@link Single<City>}. {@code OnError} с передачей исключения
     * {@link java.util.NoSuchElementException}, если данные о городе не найдены.
     */
    Single<City> getCityByGooglePlaceId(String placeId, String lang);

    /**
     * Получение экземпляра {@link City} по заданному идентификатору. Данные локализации
     * могут отсутствовать.
     *
     * @param placeId Идентификатор города в Google Places.
     * @return {@link Single<City>}. {@code OnError} с передачей исключения
     * {@link java.util.NoSuchElementException}, если данные о городе не найдены.
     */
    Single<City> getCityByGooglePlaceId(String placeId);

    /**
     * Получение Rx-источника списка избранных городов для заданного языка локализации. Приоритет
     * выбора локализации данных о городе:
     * 1) Код языка, переданный в параметре.
     * 2) Язык по-умолчанию.
     * 3) Любая другая локализация, присутствующая в базе данных.
     * <p>
     * В случае, если локализации отсутствуют в базе данных, город останется в списке с данными
     * дескриптора, но соответствующие данные локализации установлены не будут,
     * а код языка будет {@code null}.
     *
     * @param lang Язык локализации, для которой необходимо получить источник.
     * @return Rx-observable.
     */
    Observable<List<City>> getFavoriteCitiesObservable(String lang);

    /**
     * Устанавливает флаг для города с заданными координатами означающий, что город
     * является избранным.
     *
     * @param latitude  Широта.
     * @param longitude Долгота.
     * @param favorite  Избранный.
     */
    void setFavoriteCity(double latitude, double longitude, boolean favorite);

    /**
     * Сохранение экземпляра {@link City} в хранилище.
     */
    void saveCity(City city);

    /**
     * @param latitude  Широта.
     * @param longitude Долгота.
     * @param lang      Код языка локализации данных.
     * @return Погода для заданных параметров.
     */
    Single<Weather> getWeatherByCoordinates(double latitude, double longitude, String lang);

    /**
     * Сохранение экземпляра погоды в хранилище.
     *
     * @param weather Сохраняемый объект.
     */
    void saveWeather(Weather weather);
}
