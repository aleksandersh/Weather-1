package ru.yamblz.weather.di.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.yamblz.weather.di.ActivityContext;
import ru.yamblz.weather.di.scope.PerActivity;

@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @PerActivity
    @ActivityContext
    @Provides
    Context provideActivityContext() {
        return activity;
    }
}
