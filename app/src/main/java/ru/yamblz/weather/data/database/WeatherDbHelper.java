package ru.yamblz.weather.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import ru.yamblz.weather.data.database.WeatherDbSchema.CityDescriptorTable;
import ru.yamblz.weather.data.database.WeatherDbSchema.CityLocalizationTable;
import ru.yamblz.weather.data.database.WeatherDbSchema.ForecastTable;
import ru.yamblz.weather.data.database.WeatherDbSchema.WeatherTable;

public class WeatherDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "WeatherDbHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "weather_database.db";

    public WeatherDbHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CityDescriptorTable.getCreateTableQuery());
        db.execSQL(CityLocalizationTable.getCreateTableQuery());
        db.execSQL(ForecastTable.getCreateTableQuery());
        db.execSQL(WeatherTable.getCreateTableQuery());

        initializeData(db);
        Log.d(TAG, "Database created: " + DATABASE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.d(TAG, "Database upgraded: " + DATABASE_NAME + " v" + i + "-v" + i1);
    }

    private void initializeData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // TODO: 11.08.2017 Можно вынести данные в отдельное место и добавить к ним локализацию.
        values.put(CityDescriptorTable.Cols.LATITUDE, 55.755826);
        values.put(CityDescriptorTable.Cols.LONGITUDE, 37.6172999);
        values.put(CityDescriptorTable.Cols.FAVORITE, false);
        values.put(CityDescriptorTable.Cols.GP_ID, "ChIJybDUc_xKtUYRTM9XV8zWRD0");

        db.insert(CityDescriptorTable.NAME, null, values);

        Log.d(TAG, "Database default data initialized.");
    }
}
