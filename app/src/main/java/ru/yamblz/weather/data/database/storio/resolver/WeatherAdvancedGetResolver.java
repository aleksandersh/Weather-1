package ru.yamblz.weather.data.database.storio.resolver;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import java.util.List;

import ru.yamblz.weather.data.database.WeatherDbSchema;
import ru.yamblz.weather.data.model.weather.Forecast;
import ru.yamblz.weather.data.model.weather.Weather;
import ru.yamblz.weather.data.model.weather.WeatherBuilder;

/**
 * Created by AleksanderSh on 07.08.2017.
 * <p>
 * Дополнительный Get-resolver для погоды. Позволяет одним GET-запросом к Storio для погоды,
 * получить данные с прогнозом.
 */

public class WeatherAdvancedGetResolver extends GetResolver<Weather> {
    @NonNull
    private final GetResolver<Weather> mDefaultWeatherGetResolver;
    @NonNull
    private final ThreadLocal<StorIOSQLite> storIOSQLiteFromPerformGet = new ThreadLocal<>();

    public WeatherAdvancedGetResolver(@NonNull GetResolver<Weather> weatherGetResolver) {
        mDefaultWeatherGetResolver = weatherGetResolver;
    }

    @NonNull
    @Override
    public Weather mapFromCursor(@NonNull Cursor cursor) {
        final StorIOSQLite storIOSQLite = storIOSQLiteFromPerformGet.get();

        Weather weather = mDefaultWeatherGetResolver.mapFromCursor(cursor);

        String selection = WeatherDbSchema.ForecastTable.Cols.WEATHER_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(weather.getId())};
        List<Forecast> forecasts = storIOSQLite
                .get()
                .listOfObjects(Forecast.class)
                .withQuery(Query.builder()
                        .table(WeatherDbSchema.ForecastTable.NAME)
                        .where(selection)
                        .whereArgs(selectionArgs)
                        .build())
                .prepare()
                .executeAsBlocking();

        return new WeatherBuilder(weather).setForecasts(forecasts).build();
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull RawQuery rawQuery) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.lowLevel().rawQuery(rawQuery);
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull Query query) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.lowLevel().query(query);
    }
}
