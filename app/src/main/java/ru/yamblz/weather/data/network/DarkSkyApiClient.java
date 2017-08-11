package ru.yamblz.weather.data.network;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.yamblz.weather.BuildConfig;
import ru.yamblz.weather.data.model.converter.DtoToModelConverter;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.data.model.weather.Weather;
import ru.yamblz.weather.data.model.weather.WeatherBuilder;

/**
 * Created by AleksanderSh on 08.08.2017.
 * <p>
 * Клиент для сервиса Dark Sky Api.
 */

public class DarkSkyApiClient implements WeatherApiClient {
    private static final String EXCLUDE_BLOCKS = "minutely,daily,alerts,flags";

    private Api api;
    private DtoToModelConverter<WeatherResponse, WeatherBuilder> weatherConverter;

    @Inject
    public DarkSkyApiClient(Api api, DtoToModelConverter<WeatherResponse, WeatherBuilder> weatherConverter) {
        this.api = api;
        this.weatherConverter = weatherConverter;
    }

    public Single<Weather> getWeather(double lat, double lng, String lang) {
        return api.getWeather(BuildConfig.API_KEY, lat, lng, lang, EXCLUDE_BLOCKS)
                .flatMap(weatherResponse ->
                        Single.fromCallable(() ->
                                weatherConverter.convert(weatherResponse)
                                        .setUpdateTime(new Date().getTime())
                                        .setLanguage(lang)
                                        .setLatitude(lat)
                                        .setLongitude(lng)
                                        .build()));
    }
}
