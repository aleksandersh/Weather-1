package ru.yamblz.weather.ui.overview;


import java.util.List;

import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.data.model.weather.Weather;
import ru.yamblz.weather.ui.base.MvpPresenter;
import ru.yamblz.weather.ui.base.MvpView;
import ru.yamblz.weather.ui.overview.model.DailyForecast;

public interface OverviewContract {
    interface OverviewView extends MvpView {
        @Deprecated
        void displayWeatherData(WeatherResponse weatherResponse);
        void displayWeatherData(Weather weather);
        void displayCityName(String name);
        void showError();
        /**
         * Устанавливает локацию для фрагмента.
         *
         * @param location Новая локация.
         */
        void setCurrentLocation(Location location);
        /**
         * Устанавливает прогноз погоды.
         */
        void setForecasts(List<DailyForecast> forecasts);
        /**
         * @param favorite Устанавливает флаг избранного.
         */
        void setFavorite(boolean favorite);
    }

    interface OverviewPresenter extends MvpPresenter<OverviewView> {
        /**
         * Запрос погоды.
         *
         * @param location Локация.
         * @param force    Загрузка из сети.
         */
        void requestWeather(Location location, boolean force);
        /**
         * Запрашивает данные для начального заполнения.
         */
        void onViewCreated();
        /**
         * Запрашивает данные города по заданной локации.
         *
         * @param location Локация.
         */
        void requestCityByLocation(Location location);
        /**
         * Сохраняет/удаляет локацию в избранном.
         * @param location Локация.
         * @param favorite Флаг.
         */
        void setFavorite(Location location, boolean favorite);
    }
}
