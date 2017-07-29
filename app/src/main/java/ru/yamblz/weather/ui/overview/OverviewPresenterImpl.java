package ru.yamblz.weather.ui.overview;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.usecase.OverviewUseCase;
import ru.yamblz.weather.di.scope.PerActivity;
import ru.yamblz.weather.ui.base.BasePresenter;

@PerActivity
public class OverviewPresenterImpl extends BasePresenter<OverviewContract.OverviewView> implements OverviewContract.OverviewPresenter {

    private OverviewUseCase useCase;
    private AppPreferenceManager preferenceManager;

    @Inject
    public OverviewPresenterImpl(OverviewUseCase useCase, AppPreferenceManager preferenceManager) {
        this.useCase = useCase;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void requestCurrentWeather(double lat, double lng, boolean force) {
        getView().showLoading();
        getCompositeDisposable().add(
                useCase.loadCurrentWeather(lat, lng, force)
                        .subscribe(
                                weatherResponse -> {
                                    getView().hideLoading();
                                    getView().displayWeatherData(weatherResponse);
                                },
                                err -> {
                                    getView().showError();
                                    getView().hideLoading();
                                }
                        )
        );
    }

    @Override
    public void requestInitialData() {
        getView().showLoading();
        Single.fromCallable(() -> preferenceManager.getLocation())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {
                    getView().hideLoading();
                    getView().setCurrentLocation(location);
                });
    }
}
