package ru.yamblz.weather.di.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.yamblz.weather.BuildConfig;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.network.GooglePlacesApi;
import ru.yamblz.weather.data.usecase.places.CitiesUseCase;
import ru.yamblz.weather.data.usecase.places.GooglePlacesUseCaseImpl;

/**
 * Created by AleksanderSh on 25.07.2017.
 */

@Module
public class GooglePlacesModule {
    @Singleton
    @Provides
    GooglePlacesApi provideNetworkCall(@Named("GooglePlacesRetrofit") Retrofit retrofit) {
        return retrofit.create(GooglePlacesApi.class);
    }

    @Singleton
    @Provides
    @Named("GooglePlacesRetrofit")
    Retrofit provideGooglePlacesRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.GOOGLE_PLACES_BASE_URL)
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    CitiesUseCase provideGooglePlacesUseCase(GooglePlacesApi api,
                                             AppPreferenceManager preferenceManager) {
        return new GooglePlacesUseCaseImpl(api, preferenceManager);
    }
}
