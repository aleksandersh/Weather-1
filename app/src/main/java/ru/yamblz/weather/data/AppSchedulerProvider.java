package ru.yamblz.weather.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AppSchedulerProvider implements SchedulerProvider {

    @Inject
    public AppSchedulerProvider() {}

    @Override
    public <T> SingleTransformer<T, T> applyIoSchedulers() {
        return single -> single
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
