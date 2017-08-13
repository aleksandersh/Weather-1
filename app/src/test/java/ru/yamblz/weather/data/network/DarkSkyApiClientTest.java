package ru.yamblz.weather.data.network;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import ru.yamblz.weather.data.model.converter.WeatherConverter;
import ru.yamblz.weather.data.model.response.Currently;
import ru.yamblz.weather.data.model.response.DataHourly;
import ru.yamblz.weather.data.model.response.Hourly;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.data.model.weather.Forecast;
import ru.yamblz.weather.data.model.weather.ForecastBuilder;
import ru.yamblz.weather.data.model.weather.Weather;
import ru.yamblz.weather.data.model.weather.WeatherBuilder;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class DarkSkyApiClientTest {
    @Mock
    Api api;

    DarkSkyApiClient client;
    WeatherConverter converter;
    Date date;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        converter = new WeatherConverter();
        client = new DarkSkyApiClient(api, converter);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        date = calendar.getTime();
    }

    /**
     * Хотел протестировать другой конвертер, но перепутал, этот тестировать не стоит.
     */
    @Test
    public void getWeather() {
        final double lat = 20;
        final double lng = 30;
        final String lang = "ru";

        final WeatherResponse weatherResponse = getWeatherResponseModel();
        final WeatherBuilder weatherBuilder = getWeatherModelBuilder()
                .setLatitude(lat)
                .setLongitude(lng)
                .setLanguage(lang);

        final TestObserver<Weather> testObserver = TestObserver.create();

        when(api.getWeather(anyString(), anyDouble(), anyDouble(), eq(lang), anyString(), anyString()))
                .thenReturn(Single.just(weatherResponse));

        client.getWeather(lat, lng, lang).subscribe(testObserver);

        weatherBuilder.setUpdateTime(testObserver.values().get(0).getUpdateTime());
        Weather weather = weatherBuilder.build();
        testObserver.assertValue(weather);
    }

    private WeatherResponse getWeatherResponseModel() {
        Currently currently = new Currently();
        currently.setTemperature(20D);
        currently.setHumidity(30D);
        currently.setApparentTemperature(40D);
        currently.setCloudCover(50D);

        List<DataHourly> data = new ArrayList<>(3);
        DataHourly dataHourly;
        for (int i = 0; i < 48; i++) {
            dataHourly = new DataHourly();
            dataHourly.setTemperature(20D + i);
            dataHourly.setHumidity(30D + i);
            dataHourly.setApparentTemperature(40D + i);
            dataHourly.setCloudCover(50D);
            dataHourly.setTime(date.getTime() + 3600 * i);
            data.add(dataHourly);
        }

        Hourly hourly = new Hourly();
        hourly.setData(data);

        WeatherResponse response = new WeatherResponse();
        response.setCurrently(currently);
        response.setHourly(hourly);

        return response;
    }

    private WeatherBuilder getWeatherModelBuilder() {
        List<Forecast> forecasts = new ArrayList<>();
        Forecast forecast;
        for (int i = 0; i < 48; i++) {
            forecasts.add(new ForecastBuilder()
                    .setTemperature(20D + i)
                    .setHumidity(30D + i)
                    .setApparent(40D + i)
                    .setClouds(50D)
                    .setTime(date.getTime() + 3600 * i)
                    .build());
        }

        return new WeatherBuilder()
                .setTemperature(20D)
                .setHumidity(30D)
                .setApparent(40D)
                .setClouds(50D)
                .setForecasts(forecasts);
    }
}
