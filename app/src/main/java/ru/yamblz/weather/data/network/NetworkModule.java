package ru.yamblz.weather.data.network;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.yamblz.weather.BuildConfig;


@Module
public class NetworkModule {

    @Provides
    @Singleton
    Api provideWeatherApi(OkHttpClient client) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .build()
                .create(Api.class);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
    }
}
