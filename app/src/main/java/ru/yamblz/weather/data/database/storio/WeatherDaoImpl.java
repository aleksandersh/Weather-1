package ru.yamblz.weather.data.database.storio;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import ru.yamblz.weather.data.database.WeatherDao;
import ru.yamblz.weather.data.database.WeatherDbSchema.CityDescriptorTable;
import ru.yamblz.weather.data.database.WeatherDbSchema.CityLocalizationTable;
import ru.yamblz.weather.data.database.WeatherDbSchema.WeatherTable;
import ru.yamblz.weather.data.database.storio.entity.CityDescriptor;
import ru.yamblz.weather.data.database.storio.entity.CityLocalization;
import ru.yamblz.weather.data.database.storio.resolver.WeatherAdvancedGetResolver;
import ru.yamblz.weather.data.database.storio.resolver.WeatherAdvancedPutResolver;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.CityBuilder;
import ru.yamblz.weather.data.model.weather.Weather;

/**
 * Created by AleksanderSh on 04.08.2017.
 * <p>
 * Реализация {@link WeatherDao} для Storio.
 */

public class WeatherDaoImpl implements WeatherDao {
    private static final String TAG = "WeatherDaoImpl";
    private static final String DEFAULT_LANGUAGE = "en";

    private final StorIOSQLite storIOSQLite;
    private final WeatherAdvancedGetResolver weatherAdvancedGetResolver;
    private final WeatherAdvancedPutResolver weatherAdvancedPutResolver;

    private Map<String, BehaviorSubject<List<City>>> favoriteCities = new HashMap<>();

    @Inject
    public WeatherDaoImpl(StorIOSQLite storIOSQLite,
                          WeatherAdvancedGetResolver weatherAdvancedGetResolver,
                          WeatherAdvancedPutResolver weatherAdvancedPutResolver) {
        this.storIOSQLite = storIOSQLite;
        this.weatherAdvancedGetResolver = weatherAdvancedGetResolver;
        this.weatherAdvancedPutResolver = weatherAdvancedPutResolver;
    }

    @Override
    public Single<City> getCityByCoordinates(double latitude, double longitude, String lang) {
        City city = null;

        CityDescriptor descriptor = getCityDescriptor(latitude, longitude);
        if (descriptor != null) {
            CityLocalization localisation = getCityLocalisation(descriptor.getId(), lang);
            if (localisation != null) {
                city = setupCity(descriptor, localisation);
            }
        }

        return createSingle(city);
    }

    @Override
    public Single<City> getCityByCoordinates(double latitude, double longitude) {
        City city = null;

        CityDescriptor descriptor = getCityDescriptor(latitude, longitude);
        if (descriptor != null) {
            CityLocalization localisation = getCityLocalisation(descriptor.getId());
            city = setupCity(descriptor, localisation);
        }

        return createSingle(city);
    }

    @Override
    public Single<City> getCityByGooglePlaceId(String placeId, String lang) {
        City city = null;

        CityDescriptor descriptor = getCityDescriptor(placeId);
        if (descriptor != null) {
            CityLocalization localisation = getCityLocalisation(descriptor.getId());
            if (localisation != null) {
                city = setupCity(descriptor, localisation);
            }
        }

        return createSingle(city);
    }

    @Override
    public Single<City> getCityByGooglePlaceId(String placeId) {
        City city = null;

        CityDescriptor descriptor = getCityDescriptor(placeId);
        if (descriptor != null) {
            CityLocalization localisation = getCityLocalisation(descriptor.getId());
            city = setupCity(descriptor, localisation);
        }

        return createSingle(city);
    }

    @Override
    public Observable<List<City>> getFavoriteCitiesObservable(String lang) {
        BehaviorSubject<List<City>> subject = favoriteCities.get(lang);
        if (subject == null) {
            subject = BehaviorSubject.createDefault(getFavoriteCities(lang));
            favoriteCities.put(lang, subject);
        }

        return subject;
    }

    @Override
    public void setFavoriteCity(double latitude, double longitude, boolean favorite) {
        CityDescriptor descriptor = getCityDescriptor(latitude, longitude);
        if (descriptor != null && descriptor.getFavorite() != favorite) {
            descriptor = new CityDescriptor(descriptor.getId(),
                    descriptor.getLatitude(),
                    descriptor.getLongitude(),
                    favorite,
                    descriptor.getGooglePlacesId());
            storIOSQLite
                    .put()
                    .object(descriptor)
                    .prepare()
                    .executeAsBlocking();

            favoriteCities.forEach((lang, subject) -> subject.onNext(getFavoriteCities(lang)));
        } else {
            throw new NoSuchElementException("City by latitude=" + latitude
                    + ", longitude=" + longitude + " not found.");
        }
    }

    @Override
    public void saveCity(City city) {
        boolean addedDesc = false;
        boolean addedLoc = false;

        CityDescriptor descriptor = getCityDescriptor(city.getLatitude(), city.getLongitude());
        Long descriptorId;
        if (descriptor == null) {
            descriptor = new CityDescriptor(
                    null,
                    city.getLatitude(),
                    city.getLongitude(),
                    city.isFavorite(),
                    city.getGooglePlacesId());
            PutResult result = storIOSQLite
                    .put()
                    .object(descriptor)
                    .prepare()
                    .executeAsBlocking();

            addedDesc = result.wasInserted();

            descriptorId = result.insertedId();
        } else {
            descriptorId = descriptor.getId();
        }

        if (descriptorId == null) {
            throw new RuntimeException("Id cannot be null.");
        }

        CityLocalization localization = getCityLocalisation(descriptorId, city.getLang());
        if (localization == null) {
            localization = new CityLocalization(
                    null,
                    descriptorId,
                    city.getLang(),
                    city.getName(),
                    city.getAddress());
            PutResult result = storIOSQLite
                    .put()
                    .object(localization)
                    .prepare()
                    .executeAsBlocking();

            addedLoc = result.wasInserted();
        }

        // TODO: 10.08.2017 Подумать еще
        if (descriptor.getFavorite() && addedLoc) {
            if (addedDesc) {
                favoriteCities.forEach((lang, subject) -> subject.onNext(getFavoriteCities(lang)));
            } else {
                BehaviorSubject<List<City>> subject = favoriteCities.get(localization.getLang());
                if (subject != null) {
                    subject.onNext(getFavoriteCities(localization.getLang()));
                }
            }
        }
    }

    @Override
    public Single<Weather> getWeatherByCoordinates(double latitude, double longitude, String lang) {
        String selection =
                WeatherTable.Cols.LATITUDE + " = ? AND " +
                        WeatherTable.Cols.LONGITUDE + " = ? AND " +
                        WeatherTable.Cols.LANGUAGE + " = ?";
        String[] selectionArgs = new String[]{
                String.valueOf(latitude),
                String.valueOf(longitude),
                lang
        };

        return createSingle(getWeatherBySelection(selection, selectionArgs));
    }

    @Override
    public void saveWeather(Weather weather) {
        storIOSQLite
                .put()
                .object(weather)
                .withPutResolver(weatherAdvancedPutResolver)
                .prepare()
                .executeAsBlocking();
    }

    /**
     * Оборачивает данные из базы данных в источник {@link Single<T>}. Если аргумент object -
     * {@code null}, тогда в источник передается исключение {@link NoSuchElementException}.
     *
     * @param object Оборачиваемый объект.
     * @return Rx-источник с объектом.
     */
    @NonNull
    private <T> Single<T> createSingle(@Nullable T object) {
        return Single.create(emitter -> {
            if (object != null) emitter.onSuccess(object);
            else emitter.onError(new NoSuchElementException());
        });
    }

    private Weather getWeatherBySelection(String selection, String[] selectionArgs) {
        return storIOSQLite
                .get()
                .object(Weather.class)
                .withQuery(Query.builder()
                        .table(WeatherTable.NAME)
                        .where(selection)
                        .whereArgs(selectionArgs)
                        .build())
                .withGetResolver(weatherAdvancedGetResolver)
                .prepare()
                .executeAsBlocking();
    }

    private CityLocalization getCityLocalisation(long descriptorId, String lang) {
        final String selection = CityLocalizationTable.Cols.CITY_ID + " = ? AND " +
                CityLocalizationTable.Cols.LANGUAGE + " = ?";
        final String[] selectionArgs = new String[]{String.valueOf(descriptorId), lang};
        return getCityLocalisationBySelection(selection, selectionArgs);
    }

    private CityLocalization getCityLocalisation(long descriptorId) {
        final String selection = CityLocalizationTable.Cols.CITY_ID + " = ?";
        final String[] selectionArgs = new String[]{String.valueOf(descriptorId)};
        return getCityLocalisationBySelection(selection, selectionArgs);
    }

    private CityLocalization getCityLocalisationBySelection(String selection, String[] selectionArgs) {
        return storIOSQLite
                .get()
                .object(CityLocalization.class)
                .withQuery(Query.builder()
                        .table(CityLocalizationTable.NAME)
                        .where(selection)
                        .whereArgs(selectionArgs)
                        .limit(1)
                        .build())
                .prepare()
                .executeAsBlocking();
    }

    private CityDescriptor getCityDescriptor(double latitude, double longitude) {
        String selection = CityDescriptorTable.Cols.LATITUDE + " = ? AND " +
                CityDescriptorTable.Cols.LONGITUDE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(latitude), String.valueOf(longitude)};

        return getCityDescriptorBySelection(selection, selectionArgs);
    }

    private CityDescriptor getCityDescriptor(String placeId) {
        String selection = CityDescriptorTable.Cols.GP_ID + " = ? ";
        String[] selectionArgs = new String[]{placeId};

        return getCityDescriptorBySelection(selection, selectionArgs);
    }

    private CityDescriptor getCityDescriptorBySelection(String selection, String[] selectionArgs) {
        return storIOSQLite
                .get()
                .object(CityDescriptor.class)
                .withQuery(Query.builder()
                        .table(CityDescriptorTable.NAME)
                        .where(selection)
                        .whereArgs(selectionArgs)
                        .build())
                .prepare()
                .executeAsBlocking();
    }

    private List<City> getFavoriteCities(String lang) {
        final String selection = CityDescriptorTable.Cols.FAVORITE + " = ?";
        final String[] selectionArgs = new String[]{String.valueOf(1)};

        final List<CityDescriptor> citiesDesc = storIOSQLite
                .get()
                .listOfObjects(CityDescriptor.class)
                .withQuery(Query.builder()
                        .table(CityDescriptorTable.NAME)
                        .where(selection)
                        .whereArgs(selectionArgs)
                        .build())
                .prepare()
                .executeAsBlocking();

        final List<City> cities = new ArrayList<>(citiesDesc.size());

        for (CityDescriptor descriptor : citiesDesc) {
            CityLocalization localisation = getCityLocalisation(descriptor.getId(), lang);
            if (localisation == null && !lang.equals(DEFAULT_LANGUAGE)) {
                localisation = getCityLocalisation(descriptor.getId(), DEFAULT_LANGUAGE);
            }
            if (localisation == null) {
                localisation = getCityLocalisation(descriptor.getId());
            }
            if (localisation != null) {
                cities.add(setupCity(descriptor, localisation));
            } else {
                Log.d(TAG, "City descriptor without some localization.");
            }
        }

        return cities;
    }

    @Nullable
    private City setupCity(@Nullable CityDescriptor descriptor, @Nullable CityLocalization localisation) {
        CityBuilder builder = new CityBuilder();

        if (descriptor != null) setupCityBuilder(builder, descriptor);
        else return null;

        if (localisation != null) setupCityBuilder(builder, localisation);

        return builder.build();
    }

    private CityBuilder setupCityBuilder(CityBuilder builder, CityDescriptor descriptor) {
        return builder
                .setLatitude(descriptor.getLatitude())
                .setLongitude(descriptor.getLongitude())
                .setFavorite(descriptor.getFavorite())
                .setGooglePlacesId(descriptor.getGooglePlacesId());
    }

    private CityBuilder setupCityBuilder(CityBuilder builder, CityLocalization localisation) {
        return builder
                .setName(localisation.getName())
                .setAddress(localisation.getAddress())
                .setLang(localisation.getLang());
    }
}
