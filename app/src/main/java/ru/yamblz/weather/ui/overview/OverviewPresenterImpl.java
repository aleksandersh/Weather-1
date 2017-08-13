package ru.yamblz.weather.ui.overview;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.yamblz.weather.R;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.weather.Forecast;
import ru.yamblz.weather.data.usecase.OverviewUseCase;
import ru.yamblz.weather.di.ActivityContext;
import ru.yamblz.weather.di.scope.PerActivity;
import ru.yamblz.weather.ui.base.BasePresenter;
import ru.yamblz.weather.ui.overview.model.ForecastsConverter;

@PerActivity
public class OverviewPresenterImpl extends BasePresenter<OverviewContract.OverviewView> implements OverviewContract.OverviewPresenter {

    private OverviewUseCase useCase;
    private Context context;
    private ForecastsConverter forecastsConverter;

    @Inject
    public OverviewPresenterImpl(OverviewUseCase useCase, @ActivityContext Context context,
                                 ForecastsConverter forecastsConverter) {
        this.useCase = useCase;
        this.context = context;
        this.forecastsConverter = forecastsConverter;
    }

    @Override
    public void requestWeather(Location location, boolean force) {
        getView().showLoading();
        String lang = context.getString(R.string.api_language_value);
        getCompositeDisposable().add(
                useCase.loadWeather(location, lang, force)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                weather -> {
                                    getView().hideLoading();
                                    getView().displayWeatherData(weather);
                                    displayForecasts(weather.getForecasts());
                                },
                                err -> {
                                    getView().hideLoading();
                                    getView().showError();
                                }
                        )
        );
    }

    @Override
    public void onViewCreated() {
        getView().showLoading();
        getCompositeDisposable().add(
                useCase.getCurrentLocation()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(location -> getView().showLoading())
                        .subscribe(
                                location -> {
                                    getView().setCurrentLocation(location);
                                    requestCityByLocation(location);
                                    requestWeather(location, false);
                                },
                                throwable -> {
                                    getView().hideLoading();
                                    getView().showError();
                                }
                        )
        );
    }

    @Override
    public void requestCityByLocation(Location location) {
        getView().displayCityName("");
        String lang = context.getString(R.string.api_language_value);
        getCompositeDisposable().add(
                useCase.getCityByCoordinates(location.getLatitude(), location.getLongitude(), lang)
                        .subscribe(
                                city -> {
                                    getView().displayCityName(city.getName());
                                    getView().setFavorite(city.isFavorite());
                                },
                                throwable -> {
                                    getView().showError();
                                    String unknownCity = context.getString(R.string.cities_unknown_city);
                                    getView().displayCityName(unknownCity);
                                    getView().setFavorite(false);
                                }));
    }

    @Override
    public void setFavorite(Location location, boolean favorite) {
        getCompositeDisposable().add(
                useCase.setFavorite(location, favorite)
                        .subscribe(
                                () -> {
                                },
                                err -> {
                                    getView().setFavorite(!favorite);
                                    getView().showError();
                                }
                        )
        );
    }

    private void displayForecasts(List<Forecast> forecasts) {
        getCompositeDisposable().add(
                Single.fromCallable(() -> forecastsConverter.convert(forecasts))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                dailyForecasts -> getView().setForecasts(dailyForecasts),
                                err -> getView().showError())
        );
    }
}
