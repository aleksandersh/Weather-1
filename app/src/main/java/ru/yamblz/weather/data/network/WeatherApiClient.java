package ru.yamblz.weather.data.network;

import io.reactivex.Single;
import ru.yamblz.weather.data.model.weather.Weather;

/**
 * Created by AleksanderSh on 11.08.2017.
 * <p>
 * Клиент для сервиса, отвечающего за поставку данных о погоде. Обрабатывает данные сервиса
 * и конвертирует их в объекты контекста приложения.
 */

public interface WeatherApiClient {
    /**
     * Получает данные о погоде по заданным параметрам.
     *
     * @param latitude  Широта.
     * @param longitude Долгота.
     * @param lang      Язык локализации, на котором будут получены сведения.
     * @return Данные о погоде.
     */
    Single<Weather> getWeather(double latitude, double longitude, String lang);
}
