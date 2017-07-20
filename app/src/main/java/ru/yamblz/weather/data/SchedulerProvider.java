package ru.yamblz.weather.data;

import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;

public interface SchedulerProvider {
    <T> ObservableTransformer<T, T> applyIoSchedulers();
}
