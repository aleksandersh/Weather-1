package ru.yamblz.weather.data.usecase.places;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.places.PlacePrediction;

/**
 * Created by AleksanderSh on 27.07.2017.
 */

public interface CitiesUseCase {
    /**
     * Получение предполагаемых городов по тексту.
     *
     * @param text Текст, содержащийся в названии города.
     * @return Rx-источник списка предполагаемых мест.
     */
    Single<List<PlacePrediction>> loadPlacePredictions(String text);

    /**
     * Подгружает и сохраняет необходимые данные о новом местоположении.
     *
     * @param prediction Предполагаемое местоположение.
     * @return Rx-источник с информацией об успешности операции.
     */
    Completable setCurrentLocationByPrediction(PlacePrediction prediction);

    /**
     * @return Текущая установленная в приложении локация.
     */
    Single<Location> getCurrentLocation();
}
