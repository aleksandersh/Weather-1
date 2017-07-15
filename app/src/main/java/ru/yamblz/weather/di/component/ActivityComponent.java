package ru.yamblz.weather.di.component;

import dagger.Component;
import ru.yamblz.weather.di.module.ActivityModule;
import ru.yamblz.weather.di.scope.PerActivity;
import ru.yamblz.weather.ui.overview.OverviewViewImpl;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(OverviewViewImpl overviewView);
}
