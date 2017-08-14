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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Forecast forecast = (Forecast) o;

        if (id != null ? !id.equals(forecast.id) : forecast.id != null) return false;
        if (weatherId != null ? !weatherId.equals(forecast.weatherId) : forecast.weatherId != null)
            return false;
        if (time != null ? !time.equals(forecast.time) : forecast.time != null) return false;
        if (temperature != null ? !temperature.equals(forecast.temperature) : forecast.temperature != null)
            return false;
        if (condition != null ? !condition.equals(forecast.condition) : forecast.condition != null)
            return false;
        if (apparent != null ? !apparent.equals(forecast.apparent) : forecast.apparent != null)
            return false;
        if (humidity != null ? !humidity.equals(forecast.humidity) : forecast.humidity != null)
            return false;
        if (clouds != null ? !clouds.equals(forecast.clouds) : forecast.clouds != null)
            return false;
        return icon != null ? icon.equals(forecast.icon) : forecast.icon == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (weatherId != null ? weatherId.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (temperature != null ? temperature.hashCode() : 0);
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        result = 31 * result + (apparent != null ? apparent.hashCode() : 0);
        result = 31 * result + (humidity != null ? humidity.hashCode() : 0);
        result = 31 * result + (clouds != null ? clouds.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }
}
