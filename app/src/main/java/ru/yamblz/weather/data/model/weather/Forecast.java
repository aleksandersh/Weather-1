package ru.yamblz.weather.data.model.weather;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import ru.yamblz.weather.data.database.WeatherDbSchema.ForecastTable;

/**
 * Created by AleksanderSh on 04.08.2017.
 * <p>
 * Прогноз.
 */

@StorIOSQLiteType(table = ForecastTable.NAME)
public class Forecast {
    @StorIOSQLiteColumn(name = ForecastTable.Cols._ID, key = true)
    Long id;
    @StorIOSQLiteColumn(name = ForecastTable.Cols.WEATHER_ID)
    Long weatherId;
    @StorIOSQLiteColumn(name = ForecastTable.Cols.TIME)
    Long time;
    @StorIOSQLiteColumn(name = ForecastTable.Cols.TEMPERATURE)
    Double temperature;
    @StorIOSQLiteColumn(name = ForecastTable.Cols.CONDITION)
    String condition;
    @StorIOSQLiteColumn(name = ForecastTable.Cols.APPARENT)
    Double apparent;
    @StorIOSQLiteColumn(name = ForecastTable.Cols.HUMIDITY)
    Double humidity;
    @StorIOSQLiteColumn(name = ForecastTable.Cols.CLOUDS)
    Double clouds;
    @StorIOSQLiteColumn(name = ForecastTable.Cols.ICON)
    String icon;

    Forecast() {
    }

    Forecast(Long id,
             Long weatherId,
             Long time,
             Double temperature,
             String condition,
             Double apparent,
             Double humidity,
             Double clouds,
             String icon) {
        this.id = id;
        this.weatherId = weatherId;
        this.time = time;
        this.temperature = temperature;
        this.condition = condition;
        this.apparent = apparent;
        this.humidity = humidity;
        this.clouds = clouds;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public Long getWeatherId() {
        return weatherId;
    }

    public Long getTime() {
        return time;
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
}
