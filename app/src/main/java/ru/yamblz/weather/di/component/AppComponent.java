package ru.yamblz.weather.di.component;

import javax.inject.Singleton;

import dagger.Component;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.network.NetworkModule;
import ru.yamblz.weather.data.usecase.GooglePlacesUseCase;
import ru.yamblz.weather.data.usecase.OverviewUseCase;
import ru.yamblz.weather.di.module.AppModule;
import ru.yamblz.weather.di.module.GooglePlacesModule;
import ru.yamblz.weather.di.module.UseCaseModule;
import ru.yamblz.weather.utils.Converter;
import ru.yamblz.weather.utils.RxBus;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, UseCaseModule.class, GooglePlacesModule.class})
public interface AppComponent {
    RxBus rxBus();
    Converter converter();
    AppPreferenceManager preferenceManager();
    OverviewUseCase overviewUseCase();
    GooglePlacesUseCase googlePlacesUseCase();
}
