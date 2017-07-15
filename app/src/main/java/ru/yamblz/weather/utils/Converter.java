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
}
