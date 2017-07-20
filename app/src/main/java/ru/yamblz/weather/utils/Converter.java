package ru.yamblz.weather.utils;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.yamblz.weather.R;
import ru.yamblz.weather.data.local.AppPreferenceManager;

@Singleton
public class Converter {

    private AppPreferenceManager preferenceManager;

    @Inject
    Converter(AppPreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public String convertTemperature(double temperature) {
        String currentUnits = preferenceManager.getCurrentUnits();
        if (currentUnits.equals("F")) {
            int rounded = (int) Math.round(temperature);
            return String.valueOf(rounded);
        }
        temperature = ((temperature - 32)*5)/9;
        int rounded = (int) Math.round(temperature);
        return String.valueOf(rounded);
    }

    public String convertToPercentage(double value) {
        int result = (int) Math.round((1-value)*100);
        return String.valueOf(result);
    }

    public int convertIconToRes(String iconName) {
        switch (iconName) {
            case "clear-day":
                return R.drawable.ic_clear_day;
            case "clear-night":
                return R.drawable.ic_clear_night;
            case "rain":
                return R.drawable.ic_rain;
            case "snow":
                return R.drawable.ic_snow;
            case "sleet":
                return R.drawable.ic_sleet;
            case "wind":
                return R.drawable.ic_wind;
            case "fog":
                return R.drawable.ic_fog;
            case "cloudy":
                return R.drawable.ic_cloudy;
            case "partly-cloudy-day":
                return R.drawable.ic_partly_cloudy_day;
            case "partly-cloudy-night":
                return R.drawable.ic_partly_cloudy_night;
            default:
                return R.drawable.ic_cloudy;
        }
    }
}
