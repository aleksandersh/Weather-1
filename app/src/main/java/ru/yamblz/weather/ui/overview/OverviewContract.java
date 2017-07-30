package ru.yamblz.weather.ui.overview;


import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.ui.base.MvpPresenter;
import ru.yamblz.weather.ui.base.MvpView;

public interface OverviewContract {
    interface OverviewView extends MvpView {
        void displayWeatherData(WeatherResponse weatherResponse);
        void displayCityName(String name);
        void showError();
    }
    interface OverviewPresenter extends MvpPresenter<OverviewView> {
        void requestCurrentWeather(double lat, double lng, boolean force);
    }
}
