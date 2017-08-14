package ru.yamblz.weather.data.model.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.yamblz.weather.data.model.response.Currently;
import ru.yamblz.weather.data.model.response.DataHourly;
import ru.yamblz.weather.data.model.response.Hourly;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.data.model.weather.Forecast;
import ru.yamblz.weather.data.model.weather.ForecastBuilder;
import ru.yamblz.weather.data.model.weather.WeatherBuilder;

public class WeatherConverter implements DtoToModelConverter<WeatherResponse, WeatherBuilder> {
    @Override
    public WeatherBuilder convert(WeatherResponse dto) {
        WeatherBuilder weatherBuilder = new WeatherBuilder();

        Currently currently = dto.getCurrently();
        if (currently != null) {
            weatherBuilder
                    .setTemperature(currently.getTemperature())
                    .setCondition(currently.getSummary())
                    .setApparent(currently.getApparentTemperature())
                    .setHumidity(currently.getHumidity())
                    .setClouds(currently.getCloudCover())
                    .setIcon(currently.getIcon());
        }

        List<Forecast> forecasts;
        Hourly hourly = dto.getHourly();
        if (hourly != null && hourly.getData() != null) {
            List<DataHourly> dataList = hourly.getData();
            forecasts = new ArrayList<>(dataList.size());
            for (DataHourly data : dataList) {
                forecasts.add(new ForecastBuilder()
                        .setTime(data.getTime().longValue())
                        .setTemperature(data.getTemperature())
                        .setCondition(data.getSummary())
                        .setApparent(data.getApparentTemperature())
                        .setHumidity(data.getHumidity())
                        .setClouds(data.getCloudCover())
                        .setIcon(data.getIcon())
                        .build());
            }
        } else {
            forecasts = Collections.emptyList();
        }

        weatherBuilder.setForecasts(forecasts);

        return weatherBuilder;
    }
}
