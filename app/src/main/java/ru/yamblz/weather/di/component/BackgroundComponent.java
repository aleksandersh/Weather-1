package ru.yamblz.weather.di.component;

import javax.inject.Singleton;

import dagger.Component;
import ru.yamblz.weather.data.network.NetworkModule;
import ru.yamblz.weather.di.module.AppModule;
import ru.yamblz.weather.di.module.DatabaseModule;
import ru.yamblz.weather.di.module.GooglePlacesModule;
import ru.yamblz.weather.utils.UpdateTaskService;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, DatabaseModule.class, GooglePlacesModule.class})
public interface BackgroundComponent {
    void inject(UpdateTaskService updateTaskService);
}
