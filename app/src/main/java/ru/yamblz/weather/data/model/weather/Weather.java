package ru.yamblz.weather.data.model.weather;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import java.util.Date;
import java.util.List;

import ru.yamblz.weather.data.database.WeatherDbSchema.WeatherTable;

/**
 * Created by AleksanderSh on 04.08.2017.
 * <p>
 * Содержит данные о погоде и обновлении.
 */

@StorIOSQLiteType(table = WeatherTable.NAME)
public class Weather {
    @StorIOSQLiteColumn(name = WeatherTable.Cols._ID, key = true)
    Long id;
    @StorIOSQLiteColumn(name = WeatherTable.Cols.UPDATE_TIME)
    Long updateTime;
    @StorIOSQLiteColumn(name = WeatherTable.Cols.LANGUAGE)
    String language;
    @StorIOSQLiteColumn(name = WeatherTable.Cols.LATITUDE)
    Double latitude;
    @StorIOSQLiteColumn(name = WeatherTable.Cols.LONGITUDE)
    Double longitude;
    @StorIOSQLiteColumn(name = WeatherTable.Cols.TEMPERATURE)
    Double temperature;
    @StorIOSQLiteColumn(name = WeatherTable.Cols.CONDITION)
    String condition;
    @StorIOSQLiteColumn(name = WeatherTable.Cols.APPARENT)
    Double apparent;
    @StorIOSQLiteColumn(name = WeatherTable.Cols.HUMIDITY)
    Double humidity;
    @StorIOSQLiteColumn(name = WeatherTable.Cols.CLOUDS)
    Double clouds;
    @StorIOSQLiteColumn(name = WeatherTable.Cols.ICON)
    String icon;

    List<Forecast> forecasts;

    public Weather() {
    }

    public Weather(Long id,
                   Long updateTime,
                   String language,
                   Double latitude,
                   Double longitude,
                   Double temperature,
                   String condition,
                   Double apparent,
                   Double humidity,
                   Double clouds,
                   String icon,
                   List<Forecast> forecasts) {
        this.id = id;
        this.updateTime = updateTime;
        this.language = language;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
        this.condition = condition;
        this.apparent = apparent;
        this.humidity = humidity;
        this.clouds = clouds;
        this.icon = icon;
        this.forecasts = forecasts;
    }

    public Long getId() {
        return id;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public String getLanguage() {
        return language;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getTemperature() {
        return temperature;
    }

    public String getCondition() {
        return condition;
    }

    public Double getApparent() {
        return apparent;
    }

    public Double getHumidity() {
        return humidity;
    }

    public Double getClouds() {
        return clouds;
    }

    public String getIcon() {
        return icon;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }
}
