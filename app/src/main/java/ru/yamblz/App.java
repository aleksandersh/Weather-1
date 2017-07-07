package ru.yamblz;


import android.app.Application;

import ru.yamblz.weather.di.component.AppComponent;
import ru.yamblz.weather.di.component.DaggerAppComponent;
import ru.yamblz.weather.di.module.AppModule;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDaggerAppComonent();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private AppComponent initDaggerAppComonent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

}
