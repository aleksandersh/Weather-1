package ru.yamblz.weather.data.model.weather;

import ru.yamblz.weather.data.model.ModelBuilder;

public class ForecastBuilder implements ModelBuilder<Forecast> {
    private Long id;
    private Long weatherId;
    private Long time;
    private Double temperature;
    private String condition;
    private Double apparent;
    private Double humidity;
    private Double clouds;
    private String icon;

    public ForecastBuilder() {
    }

    public ForecastBuilder(Forecast forecast) {
        id = forecast.getId();
        weatherId = forecast.getWeatherId();
        time = forecast.getTime();
        temperature = forecast.getTemperature();
        condition = forecast.getCondition();
        apparent = forecast.getApparent();
        humidity = forecast.getHumidity();
        clouds = forecast.getClouds();
        icon = forecast.getIcon();
    }

    @Override
    public Forecast build() {
        return new Forecast(
                id,
                weatherId,
                time,
                temperature,
                condition,
                apparent,
                humidity,
                clouds,
                icon
        );
    }

    public ForecastBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public ForecastBuilder setWeatherId(Long weatherId) {
        this.weatherId = weatherId;
        return this;
    }

    public ForecastBuilder setTime(Long time) {
        this.time = time;
        return this;
    }

    public ForecastBuilder setTemperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }

    public ForecastBuilder setCondition(String condition) {
        this.condition = condition;
        return this;
    }

    public ForecastBuilder setApparent(Double apparent) {
        this.apparent = apparent;
        return this;
    }

    public ForecastBuilder setHumidity(Double humidity) {
        this.humidity = humidity;
        return this;
    }

    public ForecastBuilder setClouds(Double clouds) {
        this.clouds = clouds;
        return this;
    }

    public ForecastBuilder setIcon(String icon) {
        this.icon = icon;
        return this;
    }
}
