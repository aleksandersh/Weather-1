package ru.yamblz.weather.data.usecase;

import io.reactivex.Observable;
import ru.yamblz.weather.data.model.response.WeatherResponse;

public interface OverviewUseCase {
    Observable<WeatherResponse> loadCurrentWeather(double lat, double lng, boolean force);
}
