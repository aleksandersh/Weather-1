package ru.yamblz.weather.ui.cities.favorite;

import android.content.Context;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.yamblz.weather.R;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.usecase.places.CitiesUseCase;
import ru.yamblz.weather.di.ActivityContext;
import ru.yamblz.weather.di.scope.PerActivity;
import ru.yamblz.weather.ui.base.BasePresenter;

@PerActivity
public class FavoriteCitiesPresenterImpl extends BasePresenter<FavoriteCitiesContract.CitiesView>
        implements FavoriteCitiesContract.CitiesPresenter {
    private final CitiesUseCase placesUseCase;
    private final Context context;

    @Inject
    public FavoriteCitiesPresenterImpl(CitiesUseCase placesUseCase, @ActivityContext Context context) {
        this.placesUseCase = placesUseCase;
        this.context = context;
    }

    @Override
    public void onViewCreated() {
        String lang = context.getString(R.string.api_language_value);
        getCompositeDisposable().add(
                placesUseCase.getFavoriteCities(lang)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cities -> getView().setFavoriteCities(cities))
        );
    }

    @Override
    public void onCitySelected(City city) {
        placesUseCase.setCurrentLocationByCity(city);
    }
}
