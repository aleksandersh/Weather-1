package ru.yamblz.weather.di.component;

import javax.inject.Singleton;

import dagger.Component;
import ru.yamblz.weather.data.network.NetworkModule;
import ru.yamblz.weather.data.usecase.OverviewUseCase;
import ru.yamblz.weather.di.module.AppModule;
import ru.yamblz.weather.di.module.UseCaseModule;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, UseCaseModule.class})
public interface AppComponent {
    OverviewUseCase overviewUseCase();
}
