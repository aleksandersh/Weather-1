package ru.yamblz.weather.di.module;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.yamblz.weather.data.database.WeatherDao;
import ru.yamblz.weather.data.database.WeatherDbHelper;
import ru.yamblz.weather.data.database.storio.WeatherDaoImpl;
import ru.yamblz.weather.data.database.storio.entity.CityDescriptor;
import ru.yamblz.weather.data.database.storio.entity.CityDescriptorSQLiteTypeMapping;
import ru.yamblz.weather.data.database.storio.entity.CityLocalization;
import ru.yamblz.weather.data.database.storio.entity.CityLocalizationSQLiteTypeMapping;
import ru.yamblz.weather.data.database.storio.resolver.WeatherAdvancedGetResolver;
import ru.yamblz.weather.data.database.storio.resolver.WeatherAdvancedPutResolver;
import ru.yamblz.weather.data.model.weather.Forecast;
import ru.yamblz.weather.data.model.weather.ForecastSQLiteTypeMapping;
import ru.yamblz.weather.data.model.weather.Weather;
import ru.yamblz.weather.data.model.weather.WeatherSQLiteTypeMapping;
import ru.yamblz.weather.di.ApplicationContext;

@Module
public class DatabaseModule {
    @Singleton
    @Provides
    SQLiteOpenHelper provideSQLiteOpenHelper(@ApplicationContext Context context) {
        return new WeatherDbHelper(context);
    }

    @Singleton
    @Provides
    StorIOSQLite provideStorIOSQLite(SQLiteOpenHelper sqLiteOpenHelper,
                                     WeatherSQLiteTypeMapping weatherMapping) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(CityDescriptor.class, new CityDescriptorSQLiteTypeMapping())
                .addTypeMapping(CityLocalization.class, new CityLocalizationSQLiteTypeMapping())
                .addTypeMapping(Weather.class, weatherMapping)
                .addTypeMapping(Forecast.class, new ForecastSQLiteTypeMapping())
                .build();
    }

    @Singleton
    @Provides
    WeatherDao provideWeatherDao(StorIOSQLite storIOSQLite,
                                 WeatherAdvancedGetResolver weatherAdvancedGetResolver,
                                 WeatherAdvancedPutResolver weatherAdvancedPutResolver) {
        return new WeatherDaoImpl(storIOSQLite, weatherAdvancedGetResolver, weatherAdvancedPutResolver);
    }

    @Singleton
    @Provides
    WeatherSQLiteTypeMapping provideWeatherSQLiteTypeMapping() {
        return new WeatherSQLiteTypeMapping();
    }

    @Singleton
    @Provides
    WeatherAdvancedGetResolver provideWeatherAdvancedGetResolver(WeatherSQLiteTypeMapping mapping) {
        return new WeatherAdvancedGetResolver(mapping.getResolver());
    }

    @Singleton
    @Provides
    WeatherAdvancedPutResolver provideWeatherAdvancedPutResolver() {
        return new WeatherAdvancedPutResolver();
    }
}
