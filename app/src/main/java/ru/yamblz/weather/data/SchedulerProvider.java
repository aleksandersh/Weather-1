package ru.yamblz.weather.data;

import io.reactivex.SingleTransformer;

public interface SchedulerProvider {
    <T> SingleTransformer<T, T> applyIoSchedulers();
}
