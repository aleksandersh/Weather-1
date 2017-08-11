package ru.yamblz.weather.data.model.converter;

/**
 * Created by AleksanderSh on 04.08.2017.
 * <p>
 * Интерфейс конвертера объекта передачи данных в модель приложения.
 */

public interface DtoToModelConverter<T, U> {
    U convert(T dto);
}
