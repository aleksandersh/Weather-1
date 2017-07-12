package ru.yamblz.weather.di.component;

import javax.inject.Singleton;

import dagger.Component;
import ru.yamblz.weather.data.network.NetworkModule;
import ru.yamblz.weather.di.module.AppModule;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {}
