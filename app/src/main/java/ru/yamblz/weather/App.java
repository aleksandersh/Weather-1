package ru.yamblz.weather;


import android.app.Application;
import com.defaultapps.preferenceshelper.PreferencesHelper;

import ru.yamblz.weather.di.component.AppComponent;
import ru.yamblz.weather.di.component.DaggerAppComponent;
import ru.yamblz.weather.di.module.AppModule;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initPrefsHelper();
        appComponent = initDaggerAppComponent().build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    protected DaggerAppComponent.Builder initDaggerAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this));
    }

    private void initPrefsHelper() {
        new PreferencesHelper.Builder(this)
                .build();
    }
}
