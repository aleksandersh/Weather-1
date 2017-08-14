package ru.yamblz.weather.data.network;

import java.util.List;

import io.reactivex.Single;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.PlacePrediction;

/**
 * Created by AleksanderSh on 11.08.2017.
 * <p>
 * Клиент для сервиса, отвечающего за поставку данных о локациях. Обрабатывает данные сервиса
 * и конвертирует их в объекты контекста приложения.
 */

public interface PlacesApiClient {
    /**
     * Получение списка предположительных мест, соответствующих поисковому запросу.
     *
     * @param text Текст в названии места.
     * @return Список предположительных мест.
     */
    Single<List<PlacePrediction>> loadPlacePredictions(String text);

    /**
     * Загрузка детальных данных о местоположении.
     *
     * @param placeId Идентификатор места.
     * @param lang    Язык локализации, на котором необходимо получить описание места.
     * @return Объект-город.
     */
    Single<City> loadCityByPlaceId(String placeId, String lang);
}
