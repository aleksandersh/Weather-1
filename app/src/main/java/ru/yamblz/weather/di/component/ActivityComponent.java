package ru.yamblz.weather.di.component;

import dagger.Component;
import ru.yamblz.weather.di.module.ActivityModule;
import ru.yamblz.weather.di.scope.PerActivity;

@PerActivity
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {}
