package ru.yamblz.weather.data.local;

import com.defaultapps.preferenceshelper.DefaultPreferencesManager;
import com.defaultapps.preferenceshelper.PreferencesHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import ru.yamblz.weather.data.model.places.Location;

@Singleton
public class AppPreferenceManager extends DefaultPreferencesManager {

    private final String BACKGROUND_SWITCH = "b_update";
    private final String UPDATE_INTERVAL = "b_interval";
    private final String CURRENT_UNITS = "temperature_units";
    private final String LOCATION_LONGITUDE_KEY = "location_longitude";
    private final String LOCATION_LATITUDE_KEY = "location_latitude";

    private BehaviorSubject<Location> mLocationBehaviorSubject;

    @Inject
    public AppPreferenceManager() {
    }

    public boolean isBackgroundUpdateEnabled() {
        return getPreferencesHelper().getBoolean(BACKGROUND_SWITCH, true);
    }

    public String getCurrentUpdateInterval() {
        return getPreferencesHelper().getString(UPDATE_INTERVAL, "3600000");
    }

    public String getCurrentUnits() {
        return getPreferencesHelper().getString(CURRENT_UNITS, "C");
    }

    /**
     * @return Сохраненные данные о местоположении.
     */
    public Location getLocation() {
        PreferencesHelper ph = getPreferencesHelper();
        double lng = Double.parseDouble(ph.getString(LOCATION_LONGITUDE_KEY, "37.6172999"));
        double lat = Double.parseDouble(ph.getString(LOCATION_LATITUDE_KEY, "55.755826"));

        return new Location(lat, lng);
    }

    /**
     * Сохранение данных о местоположении.
     */
    public void setCurrentLocation(Location location) {
        PreferencesHelper ph = getPreferencesHelper();
        ph.putString(LOCATION_LONGITUDE_KEY, String.valueOf(location.getLongitude()));
        ph.putString(LOCATION_LATITUDE_KEY, String.valueOf(location.getLatitude()));

        if (mLocationBehaviorSubject != null) {
            mLocationBehaviorSubject.onNext(location);
        }
    }

    /**
     * @return Источник информации о текущей локации типа {@link Observable<Location>}.
     */
    public Observable<Location> getCurrentLocationObservable() {
        if (mLocationBehaviorSubject == null) {
            mLocationBehaviorSubject = BehaviorSubject.createDefault(getLocation());
        }

        return mLocationBehaviorSubject;
    }
}
