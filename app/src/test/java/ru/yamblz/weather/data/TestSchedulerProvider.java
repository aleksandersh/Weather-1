package ru.yamblz.weather.data;

import io.reactivex.SingleTransformer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

public class TestSchedulerProvider implements SchedulerProvider {

    private TestScheduler testScheduler;

    public TestSchedulerProvider(TestScheduler testScheduler) {
        this.testScheduler = testScheduler;
    }

    @Override
    public <T> SingleTransformer<T, T> applyIoSchedulers() {
        return single -> single
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline());
    }
}
