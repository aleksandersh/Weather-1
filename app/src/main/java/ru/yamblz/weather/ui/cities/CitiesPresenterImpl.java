package ru.yamblz.weather.ui.cities;

import java.io.IOException;
import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.yamblz.weather.R;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.data.usecase.places.CitiesUseCase;
import ru.yamblz.weather.data.usecase.places.GooglePlacesException;
import ru.yamblz.weather.di.scope.PerActivity;
import ru.yamblz.weather.ui.base.BasePresenter;

/**
 * Created by AleksanderSh on 26.07.2017.
 */

@PerActivity
public class CitiesPresenterImpl extends BasePresenter<CitiesContract.CitiesView>
        implements CitiesContract.CitiesPresenter {
    private CitiesUseCase mPlacesUseCase;
    private AppPreferenceManager mPreferenceManager;

    @Inject
    public CitiesPresenterImpl(CitiesUseCase placesUseCase, AppPreferenceManager preferenceManager) {
        mPlacesUseCase = placesUseCase;
        mPreferenceManager = preferenceManager;
    }

    @Override
    public void requestPredictions(String text) {
        getCompositeDisposable().clear();
        if (text.length() > 0) {
            getCompositeDisposable().add(
                    mPlacesUseCase.loadPlacePredictions(text)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    predictions -> getView().showPredictions(predictions),
                                    err -> {
                                        showEmptyList();
                                        showErrorByThrowable(err);
                                    }
                            )
            );
        } else {
            showEmptyList();
        }
    }

    @Override
    public void requestInitialData() {
        Single.fromCallable(() -> mPreferenceManager.getLocation())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> getView().setCurrentLocation(location));
    }

    @Override
    public void setCurrentLocationByPrediction(PlacePrediction prediction) {
        getCompositeDisposable().clear();
        getCompositeDisposable().add(mPlacesUseCase.setCurrentLocationByPrediction(prediction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> getView().onSelectionSuccessful(),
                        this::showErrorByThrowable
                ));
    }

    private void showEmptyList() {
        getView().showPredictions(Collections.emptyList());
    }

    private void showErrorByThrowable(Throwable throwable) {
        if (throwable instanceof GooglePlacesException) {
            GooglePlacesException gpe = (GooglePlacesException) throwable;
            getView().showError(gpe.getDescriptionResId());
        } else if (throwable instanceof IOException) {
            getView().showError(R.string.error_http_io);
        } else {
            getView().showError(R.string.error_message);
        }
    }
}
