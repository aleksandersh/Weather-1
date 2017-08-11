package ru.yamblz.weather.data.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.yamblz.weather.data.model.response.WeatherResponse;

public interface Api {

    @GET("{api_key}/{lat},{lng}")
    Single<WeatherResponse> getWeather(
            @Path("api_key") String apiKey,
            @Path("lat") double latitude,
            @Path("lng") double longitude,
            @Query("lang") String lang,
            @Query("exclude") String blocks);
}
