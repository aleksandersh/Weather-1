package ru.yamblz.weather.di.component;

import dagger.Component;
import ru.yamblz.weather.di.module.ActivityModule;
import ru.yamblz.weather.di.scope.PerActivity;
import ru.yamblz.weather.ui.cities.CitiesActivity;
import ru.yamblz.weather.ui.cities.CitiesViewImpl;
import ru.yamblz.weather.ui.main.MainActivity;
import ru.yamblz.weather.ui.overview.OverviewViewImpl;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(CitiesActivity activity);
    void inject(OverviewViewImpl overviewView);
    void inject(CitiesViewImpl citiesView);
}
