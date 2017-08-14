package ru.yamblz.weather.data.model.weather;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;

        if (id != null ? !id.equals(weather.id) : weather.id != null) return false;
        if (updateTime != null ? !updateTime.equals(weather.updateTime) : weather.updateTime != null)
            return false;
        if (language != null ? !language.equals(weather.language) : weather.language != null)
            return false;
        if (latitude != null ? !latitude.equals(weather.latitude) : weather.latitude != null)
            return false;
        if (longitude != null ? !longitude.equals(weather.longitude) : weather.longitude != null)
            return false;
        if (temperature != null ? !temperature.equals(weather.temperature) : weather.temperature != null)
            return false;
        if (condition != null ? !condition.equals(weather.condition) : weather.condition != null)
            return false;
        if (apparent != null ? !apparent.equals(weather.apparent) : weather.apparent != null)
            return false;
        if (humidity != null ? !humidity.equals(weather.humidity) : weather.humidity != null)
            return false;
        if (clouds != null ? !clouds.equals(weather.clouds) : weather.clouds != null) return false;
        if (icon != null ? !icon.equals(weather.icon) : weather.icon != null) return false;
        return forecasts != null ? forecasts.equals(weather.forecasts) : weather.forecasts == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (temperature != null ? temperature.hashCode() : 0);
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        result = 31 * result + (apparent != null ? apparent.hashCode() : 0);
        result = 31 * result + (humidity != null ? humidity.hashCode() : 0);
        result = 31 * result + (clouds != null ? clouds.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (forecasts != null ? forecasts.hashCode() : 0);
        return result;
    }
}
