package ru.yamblz.weather.data.local;

import com.defaultapps.preferenceshelper.DefaultPreferencesManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppPreferenceManager extends DefaultPreferencesManager {

    private final String BACKGROUND_SWITCH = "b_update";
    private final String UPDATE_INTERVAL = "b_interval";
    private final String CURRENT_UNITS = "temperature_units";

    @Inject
    public AppPreferenceManager() {}

    public boolean isBackgroundUpdateEnabled() {
        return getPreferencesHelper().getBoolean(BACKGROUND_SWITCH, true);
    }

    public String getCurrentUpdateInterval() {
        return getPreferencesHelper().getString(UPDATE_INTERVAL, "3600000");
    }

    public String getCurrentUntis() {
        return getPreferencesHelper().getString(CURRENT_UNITS, "C");
    }
}
