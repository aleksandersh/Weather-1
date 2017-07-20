package ru.yamblz.weather.ui.overview;

import android.util.Log;

import javax.inject.Inject;

import ru.yamblz.weather.data.usecase.OverviewUseCase;
import ru.yamblz.weather.di.scope.PerActivity;
import ru.yamblz.weather.ui.base.BasePresenter;

@PerActivity
public class OverviewPresenterImpl extends BasePresenter<OverviewContract.OverviewView> implements OverviewContract.OverviewPresenter {

    private OverviewUseCase useCase;

    @Inject
    public OverviewPresenterImpl(OverviewUseCase useCase) {
        this.useCase = useCase;
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
                            Log.d("Presenter", err.getMessage());
                        }
                )
        );
    }
}
