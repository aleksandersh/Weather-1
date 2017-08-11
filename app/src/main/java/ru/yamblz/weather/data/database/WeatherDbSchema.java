package ru.yamblz.weather.data.database;

import android.provider.BaseColumns;

/**
 * Created by AleksanderSh on 03.08.2017.
 * <p>
 * Схема базы данных приложения.
 */

public class WeatherDbSchema {
    public static final class WeatherTable {
        public static final String NAME = "weather";

        public static final class Cols implements BaseColumns {
            public static final String LATITUDE = "lat";
            public static final String LONGITUDE = "lng";
            public static final String UPDATE_TIME = "update_time";
            public static final String LANGUAGE = "lang";

            public static final String TEMPERATURE = "temperature";
            public static final String CONDITION = "condition";
            public static final String APPARENT = "apparent";
            public static final String HUMIDITY = "humidity";
            public static final String CLOUDS = "clouds";
            public static final String ICON = "icon";
        }

        public static String getCreateTableQuery() {
            return "CREATE TABLE " + NAME + "(" +
                    Cols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Cols.LATITUDE + " REAL NOT NULL, " +
                    Cols.LONGITUDE + " REAL NOT NULL, " +
                    Cols.UPDATE_TIME + " INTEGER NOT NULL, " +
                    Cols.LANGUAGE + " TEXT NOT NULL, " +

                    Cols.TEMPERATURE + " REAL NOT NULL, " +
                    Cols.CONDITION + " TEXT NOT NULL, " +
                    Cols.APPARENT + " REAL NOT NULL, " +
                    Cols.HUMIDITY + " REAL NOT NULL, " +
                    Cols.CLOUDS + " REAL NOT NULL, " +
                    Cols.ICON + " TEXT NOT NULL " +
                    ")";
        }
    }

    public static final class ForecastTable {
        public static final String NAME = "forecast_weather";

        public static final class Cols implements BaseColumns {
            public static final String WEATHER_ID = "weather_id";
            public static final String TIME = "time";
            public static final String TEMPERATURE = "temperature";
            public static final String CONDITION = "condition";
            public static final String APPARENT = "apparent";
            public static final String HUMIDITY = "humidity";
            public static final String CLOUDS = "clouds";
            public static final String ICON = "icon";
        }

        public static String getCreateTableQuery() {
            return "CREATE TABLE " + NAME + "(" +
                    Cols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Cols.WEATHER_ID + " INTEGER NOT NULL, " +
                    Cols.TIME + " INTEGER NOT NULL, " +
                    Cols.TEMPERATURE + " REAL NOT NULL, " +
                    Cols.CONDITION + " TEXT NOT NULL, " +
                    Cols.APPARENT + " REAL NOT NULL, " +
                    Cols.HUMIDITY + " REAL NOT NULL, " +
                    Cols.CLOUDS + " REAL NOT NULL, " +
                    Cols.ICON + " TEXT NOT NULL " +
                    ")";
        }
    }

    @Deprecated
    public static final class CityTable {
        public static final String NAME = "cities";

        public static final class Cols implements BaseColumns {
            public static final String NAME = "name";
            public static final String ADDRESS = "address";
            public static final String LANGUAGE = "lang";
            public static final String LATITUDE = "lat";
            public static final String LONGITUDE = "lng";
            public static final String FAVORITE = "fav";
            public static final String GP_ID = "gp_id"; // Google Places place id.
        }

        public static String getCreateTableQuery() {
            return "CREATE TABLE " + NAME + "(" +
                    Cols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Cols.NAME + " TEXT NOT NULL, " +
                    Cols.ADDRESS + " TEXT NOT NULL, " +
                    Cols.LANGUAGE + " TEXT NOT NULL, " +
                    Cols.LATITUDE + " REAL NOT NULL, " +
                    Cols.LONGITUDE + " REAL NOT NULL, " +
                    Cols.FAVORITE + " INTEGER, " +
                    Cols.GP_ID + " TEXT NOT NULL " +
                    ")";
        }
    }

    public static final class CityDescriptorTable {
        public static final String NAME = "cities_descriptors";

        public static final class Cols implements BaseColumns {
            public static final String LATITUDE = "lat";
            public static final String LONGITUDE = "lng";
            public static final String FAVORITE = "fav";
            public static final String GP_ID = "gp_id"; // Google Places place id.
        }

        public static String getCreateTableQuery() {
            return "CREATE TABLE " + NAME + "(" +
                    Cols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Cols.LATITUDE + " REAL NOT NULL, " +
                    Cols.LONGITUDE + " REAL NOT NULL, " +
                    Cols.FAVORITE + " INTEGER, " +
                    Cols.GP_ID + " TEXT NOT NULL " +
                    ")";
        }
    }

    public static final class CityLocalizationTable {
        public static final String NAME = "cities_localisations";

        public static final class Cols implements BaseColumns {
            public static final String CITY_ID = "city_id";
            public static final String NAME = "name";
            public static final String ADDRESS = "address";
            public static final String LANGUAGE = "lang";
        }

        public static String getCreateTableQuery() {
            return "CREATE TABLE " + NAME + "(" +
                    Cols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Cols.CITY_ID + " INTEGER NOT NULL, " +
                    Cols.NAME + " TEXT NOT NULL, " +
                    Cols.ADDRESS + " TEXT NOT NULL, " +
                    Cols.LANGUAGE + " TEXT NOT NULL " +
                    ")";
        }
    }
}