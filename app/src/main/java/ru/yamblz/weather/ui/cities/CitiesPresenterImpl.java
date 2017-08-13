package ru.yamblz.weather.ui.cities;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.yamblz.weather.R;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.data.usecase.places.CitiesUseCase;
import ru.yamblz.weather.data.usecase.places.GooglePlacesException;
import ru.yamblz.weather.di.ActivityContext;
import ru.yamblz.weather.di.scope.PerActivity;
import ru.yamblz.weather.ui.base.BasePresenter;

@PerActivity
public class CitiesPresenterImpl extends BasePresenter<CitiesContract.CitiesView>
        implements CitiesContract.CitiesPresenter {
    final private CitiesUseCase placesUseCase;
    final private Context context;

    @Inject
    public CitiesPresenterImpl(CitiesUseCase placesUseCase, @ActivityContext Context context) {
        this.placesUseCase = placesUseCase;
        this.context = context;
    }

    @Override
    public void onSearchTextChanged(@NonNull String text) {
        getCompositeDisposable().clear();
        if (text.length() > 0) {
            getCompositeDisposable().add(
                    placesUseCase.loadPlacePredictions(text)
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
    public void onViewCreated() {
        getView().hideContent();
        String lang = context.getString(R.string.api_language_value);
        getCompositeDisposable().add(
                placesUseCase.getCurrentCity(lang)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                city -> {
                                    getView().setSearchText(city.getName());
                                    getView().showContent();
                                },
                                throwable -> {
                                    getView().showContent();
                                })
        );
    }

    @Override
    public void onPredictionSelected(PlacePrediction prediction) {
        getView().hideContent();
        String lang = context.getString(R.string.api_language_value);
        getCompositeDisposable().add(placesUseCase.setCurrentLocationByPrediction(prediction, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> getView().onSelectionSuccessful(),
                        throwable -> {
                            getView().showContent();
                            showErrorByThrowable(throwable);
                        }
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
