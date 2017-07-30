package ru.yamblz.weather.ui.cities;

import android.support.annotation.StringRes;

import java.util.List;

import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.ui.base.MvpPresenter;
import ru.yamblz.weather.ui.base.MvpView;

/**
 * Created by AleksanderSh on 24.07.2017.
 * <p>
 * Контракт между фрагментом и его презентором.
 */

public interface CitiesContract {
    interface CitiesView extends MvpView {
        /**
         * Вывести список предпологаемых мест.
         */
        void showPredictions(List<PlacePrediction> predictions);

        /**
         * Установить данные об используемом местоположении.
         */
        void setCurrentLocation(Location location);

        /**
         * Вызывается после завершения выбора локации.
         */
        void onSelectionSuccessful();

        /**
         * Вывести ошибку.
         */
        void showError(@StringRes int stringResId);
    }

    interface CitiesPresenter extends MvpPresenter<CitiesView> {
        /**
         * Запросить список предполагаемых мест.
         */
        void requestPredictions(String text);

        /**
         * Запросить данные начального заполнения.
         */
        void requestInitialData();

        /**
         * Устанавливает новую локацию в приложении по выбранному предположению.
         *
         * @param prediction Предполагаемая локация.
         */
        void setCurrentLocationByPrediction(PlacePrediction prediction);
    }
}
