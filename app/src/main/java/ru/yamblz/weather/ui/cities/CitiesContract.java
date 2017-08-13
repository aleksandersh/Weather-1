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
    interface CitiesActivity {
        void onSelectionSuccessful();
    }

    interface CitiesView extends MvpView {
        /**
         * Вывести список предпологаемых мест.
         */
        void showPredictions(List<PlacePrediction> predictions);

        /**
         * Установить данные об используемом местоположении.
         *
         * @deprecated
         */
        @Deprecated
        void setCurrentLocation(Location location);

        /**
         * @param text Текст, который необходимо установить в поле ввода поиска.
         */
        void setSearchText(String text);

        /**
         * Вызывается после завершения выбора локации.
         */
        void onSelectionSuccessful();

        /**
         * Вывести ошибку.
         */
        void showError(@StringRes int stringResId);

        /**
         * Показать контент фрагмента.
         */
        void showContent();

        /**
         * Скрыть контент фрагмента.
         */
        void hideContent();
    }

    interface CitiesPresenter extends MvpPresenter<CitiesView> {
        /**
         * При изменении поискового запроса.
         *
         * @param text Измененный текст поиска.
         */
        void onSearchTextChanged(String text);

        /**
         * После создания View.
         */
        void onViewCreated();

        /**
         * При выборе предпологаемой локации.
         *
         * @param prediction Предполагаемая локация.
         */
        void onPredictionSelected(PlacePrediction prediction);
    }
}
