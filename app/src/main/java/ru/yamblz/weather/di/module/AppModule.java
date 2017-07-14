package ru.yamblz.weather.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.yamblz.weather.di.ApplicationContext;

@Module
public class AppModule {

    private final Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @ApplicationContext
    @Singleton
    @Provides
    Context provideAppContext() {
        return app;
    }
}
