package ru.yamblz.weather.data.usecase;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.response.WeatherResponse;

public interface OverviewUseCase {
    Observable<WeatherResponse> loadCurrentWeather(double lat, double lng, boolean force);

    /**
     * @return Текущая установленная в приложении локация.
     */
    Single<Location> getCurrentLocation();
}
