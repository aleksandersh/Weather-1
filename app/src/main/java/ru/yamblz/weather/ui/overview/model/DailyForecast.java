package ru.yamblz.weather.ui.overview.model;

import java.util.Date;

/**
 * Created by AleksanderSh on 11.08.2017.
 * <p>
 * Модель прогноза на день, используется только в UI.
 */

public class DailyForecast {
    final private Date date;
    final private String temperatureDay;
    final private String temperatureNight;
    final private String icon;

    public DailyForecast(Date date, String temperatureDay, String temperatureNight, String icon) {
        this.date = date;
        this.temperatureDay = temperatureDay;
        this.temperatureNight = temperatureNight;
        this.icon = icon;
    }

    public Date getDate() {
        return date;
    }

    public String getTemperatureDay() {
        return temperatureDay;
    }

    public String getTemperatureNight() {
        return temperatureNight;
    }

    public String getIconRes() {
        return icon;
    }
}
