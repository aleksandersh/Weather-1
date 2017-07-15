package ru.yamblz.weather.utils;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.yamblz.weather.data.local.AppPreferenceManager;

@Singleton
public class Converter {

    private AppPreferenceManager preferenceManager;

    @Inject
    public Converter(AppPreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public String convertTemperature(double temperature) {
        String currentUnits = preferenceManager.getCurrentUntis();
        if (currentUnits.equals("F")) {
            int rounded = (int) Math.round(temperature);
            return String.valueOf(rounded);
        }
        temperature = ((temperature - 32)*5)/9;
        int rounded = (int) Math.round(temperature);
        return String.valueOf(rounded);
    }
}
