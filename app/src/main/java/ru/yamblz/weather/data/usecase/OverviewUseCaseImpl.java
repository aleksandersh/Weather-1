package ru.yamblz.weather.data.usecase;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.ReplaySubject;
import ru.yamblz.weather.BuildConfig;
import ru.yamblz.weather.data.SchedulerProvider;
import ru.yamblz.weather.data.local.LocalService;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.data.network.Api;

@Singleton
public class OverviewUseCaseImpl implements OverviewUseCase {

    private Api api;
    private LocalService localService;
    private SchedulerProvider schedulerProvider;

    private Disposable weatherDisposable;
    private ReplaySubject<WeatherResponse> weatherReplaySubject;

    @Inject
    OverviewUseCaseImpl(Api api,
                        LocalService localService,
                        SchedulerProvider schedulerProvider) {
        this.api = api;
        this.localService = localService;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Observable<WeatherResponse> loadCurrentWeather(double lat, double lng, boolean force) {
        if (force && weatherDisposable != null) {
            weatherDisposable.dispose();
        }
        if (weatherDisposable == null || weatherDisposable.isDisposed()) {
            weatherReplaySubject = ReplaySubject.create();

            Single<WeatherResponse> single = force ? network(lat, lng).singleOrError() :
                    Observable.concat(local(), network(lat, lng))
                            .filter(weatherResponse -> weatherResponse.getCurrently() != null).firstOrError();

            weatherDisposable = single
                    .subscribe(weatherReplaySubject::onNext, weatherReplaySubject::onError);
        }
        return weatherReplaySubject;
    }

    private Observable<WeatherResponse> network(double lat, double lng) {
        return api.getWeather(BuildConfig.API_KEY, lat, lng)
                .doOnNext(localService::writeResponseToFile)
                .compose(schedulerProvider.applyIoSchedulers());
    }

    private Observable<WeatherResponse> local() {
        return Observable.fromCallable(() -> localService.readResponseFromFile())
                .onErrorReturnItem(new WeatherResponse())
                .compose(schedulerProvider.applyIoSchedulers());
    }
}
