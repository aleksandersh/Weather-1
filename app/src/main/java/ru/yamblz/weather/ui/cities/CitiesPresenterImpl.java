package ru.yamblz.weather.ui.cities;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.data.usecase.GooglePlacesUseCase;
import ru.yamblz.weather.di.scope.PerActivity;
import ru.yamblz.weather.ui.base.BasePresenter;

/**
 * Created by AleksanderSh on 26.07.2017.
 */

@PerActivity
public class CitiesPresenterImpl extends BasePresenter<CitiesContract.CitiesView>
        implements CitiesContract.CitiesPresenter {
    private GooglePlacesUseCase mPlacesUseCase;
    private AppPreferenceManager mPreferenceManager;

    @Inject
    public CitiesPresenterImpl(GooglePlacesUseCase placesUseCase, AppPreferenceManager preferenceManager) {
        mPlacesUseCase = placesUseCase;
        mPreferenceManager = preferenceManager;
    }

    @Override
    public void requestPredictions(String text) {
        getCompositeDisposable().clear();
        if (text.length() > 0) {
            getCompositeDisposable().add(
                    mPlacesUseCase.loadPlacePredictions(text)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    predictions -> getView().showPredictions(predictions),
                                    err -> {
                                        showEmptyList();
                                        getView().showError();
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
                        throwable -> getView().showError()
                ));
    }

    private void showEmptyList() {
        getView().showPredictions(Collections.emptyList());
    }
}
