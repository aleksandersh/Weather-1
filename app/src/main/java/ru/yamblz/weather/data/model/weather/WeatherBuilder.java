package ru.yamblz.weather.data.model.weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.yamblz.weather.data.model.ModelBuilder;

public class WeatherBuilder implements ModelBuilder<Weather> {
    private Long id;
    private long updateTime;
    private String language;
    private double latitude;
    private double longitude;
    private double temperature;
    private String condition;
    private double apparent;
    private double humidity;
    private double clouds;
    private String icon;
    private List<Forecast> forecasts = Collections.emptyList();

    public WeatherBuilder() {
    }

    public WeatherBuilder(Weather weather) {
        id = weather.getId();
        updateTime = weather.getUpdateTime();
        language = weather.getLanguage();
        latitude = weather.getLatitude();
        longitude = weather.getLongitude();
        temperature = weather.getTemperature();
        condition = weather.getCondition();
        apparent = weather.getApparent();
        humidity = weather.getHumidity();
        clouds = weather.getClouds();
        icon = weather.getIcon();
        if (weather.getForecasts() != null) setForecasts(weather.getForecasts());
    }

    @Override
    public Weather build() {
        return new Weather(
                id,
                updateTime,
                language,
                latitude,
                longitude,
                temperature,
                condition,
                apparent,
                humidity,
                clouds,
                icon,
                forecasts
        );
    }

    public WeatherBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public WeatherBuilder setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public WeatherBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }

    public WeatherBuilder setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public WeatherBuilder setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public WeatherBuilder setTemperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }

    public WeatherBuilder setCondition(String condition) {
        this.condition = condition;
        return this;
    }

    public WeatherBuilder setApparent(Double apparent) {
        this.apparent = apparent;
        return this;
    }

    public WeatherBuilder setHumidity(Double humidity) {
        this.humidity = humidity;
        return this;
    }

    public WeatherBuilder setClouds(Double clouds) {
        this.clouds = clouds;
        return this;
    }

    public WeatherBuilder setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public WeatherBuilder setForecasts(List<Forecast> forecasts) {
        this.forecasts = new ArrayList<>(forecasts);
//        Collections.copy(f, forecasts);
//        this.forecasts = f;
        return this;
    }
}
