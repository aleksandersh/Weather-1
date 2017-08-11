package ru.yamblz.weather.data.database.storio.resolver;


import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.yamblz.weather.data.model.weather.Forecast;
import ru.yamblz.weather.data.model.weather.ForecastBuilder;
import ru.yamblz.weather.data.model.weather.Weather;

import static ru.yamblz.weather.data.database.WeatherDbSchema.ForecastTable;
import static ru.yamblz.weather.data.database.WeatherDbSchema.WeatherTable;

/**
 * Created by AleksanderSh on 07.08.2017.
 * <p>
 * Дополнительный Put-resolver для погоды.
 * 1) Удаляет устаревшие данные погоды.
 * 2) Дополнительно заносит данные о прогнозе из объекта.
 */

public class WeatherAdvancedPutResolver extends PutResolver<Weather> {
    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull Weather weather) {
        removeWeatherFromDb(storIOSQLite, weather);

        PutResult putResult = storIOSQLite.put()
                .object(weather)
                .prepare()
                .executeAsBlocking();

        List<Forecast> forecastsOld = weather.getForecasts();
        Long weatherId = putResult.insertedId();
        if (weatherId != null && forecastsOld != null) {
            final List<Forecast> forecasts = new ArrayList<>(forecastsOld.size());
            forecastsOld.forEach(forecast ->
                    forecasts.add(new ForecastBuilder(forecast).setWeatherId(weatherId).build()));

            storIOSQLite.put()
                    .objects(forecasts)
                    .prepare()
                    .executeAsBlocking();
        }

        final Set<String> affectedTables = new HashSet<>(2);
        affectedTables.add(WeatherTable.NAME);
        affectedTables.add(ForecastTable.NAME);

        long id = 0;
        if (weatherId != null) id = weatherId;

        return PutResult.newInsertResult(id, affectedTables);
    }

    private void removeWeatherFromDb(StorIOSQLite storIOSQLite, Weather weather) {
        String selectionWeather = WeatherTable.Cols.LATITUDE + " = ? AND " +
                WeatherTable.Cols.LONGITUDE + " = ?";
        String[] selectionWeatherArgs = new String[]{
                String.valueOf(weather.getLatitude()),
                String.valueOf(weather.getLongitude())};

        Weather weatherDb = storIOSQLite
                .get()
                .object(Weather.class)
                .withQuery(Query.builder()
                        .table(WeatherTable.NAME)
                        .where(selectionWeather)
                        .whereArgs(selectionWeatherArgs)
                        .build())
                .prepare()
                .executeAsBlocking();

        if (weatherDb != null && weatherDb.getId() != null) {
            String selectionForecastToRemove = ForecastTable.Cols.WEATHER_ID + " = ?";
            String[] selectionArgsForecastToRemove = new String[]{String.valueOf(weatherDb.getId())};
            storIOSQLite
                    .delete()
                    .byQuery(DeleteQuery.builder()
                            .table(ForecastTable.NAME)
                            .where(selectionForecastToRemove)
                            .whereArgs(selectionArgsForecastToRemove)
                            .build())
                    .prepare()
                    .executeAsBlocking();

            String selectionWeatherToRemove = WeatherTable.Cols._ID + " = ?";
            String[] selectionArgsWeatherToRemove = new String[]{String.valueOf(weatherDb.getId())};
            storIOSQLite
                    .delete()
                    .byQuery(DeleteQuery.builder()
                            .table(WeatherTable.NAME)
                            .where(selectionWeatherToRemove)
                            .whereArgs(selectionArgsWeatherToRemove)
                            .build())
                    .prepare()
                    .executeAsBlocking();
        }
    }
}
