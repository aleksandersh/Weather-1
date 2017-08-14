package ru.yamblz.weather.data.model;

/**
 * Created by AleksanderSh on 04.08.2017.
 * <p>
 * Интерфейс для Построителя моделей.
 */

public interface ModelBuilder<T> {
    T build();
}
