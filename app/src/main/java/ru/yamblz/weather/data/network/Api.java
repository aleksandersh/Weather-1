package ru.yamblz.weather.data.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.yamblz.weather.data.model.response.WeatherResponse;

public interface Api {

    @GET("{api_key}/{lat},{lng}")
    Observable<WeatherResponse> getWeather(
            @Path("api_key") String apiKey,
            @Path("lat") double latitude,
            @Path("lng") double longitude);
}
