package ru.yamblz.weather.ui.cities.favorite;

import java.util.List;

import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.ui.base.MvpPresenter;
import ru.yamblz.weather.ui.base.MvpView;

public class FavoriteCitiesContract {
    interface CitiesView extends MvpView {
        /**
         * @param cities Устанавливат список городов.
         */
        void setFavoriteCities(List<City> cities);
    }

    interface CitiesPresenter extends MvpPresenter<CitiesView> {
        /**
         * После создания View.
         */
        void onViewCreated();

        /**
         * @param city После выбора города.
         */
        void onCitySelected(City city);
    }
}
