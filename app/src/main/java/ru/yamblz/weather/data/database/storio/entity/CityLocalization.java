package ru.yamblz.weather.data.database.storio.entity;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import ru.yamblz.weather.data.database.WeatherDbSchema.CityLocalizationTable;

/**
 * Created by AleksanderSh on 10.08.2017.
 * <p>
 * Костыль. В рамках работы с базой данных удобнее оперировать отдельной сущностью, содержащей
 * данные, зависимые от локализации.
 */

@StorIOSQLiteType(table = CityLocalizationTable.NAME)
public class CityLocalization {
    @StorIOSQLiteColumn(name = CityLocalizationTable.Cols._ID, key = true)
    Long id;
    @StorIOSQLiteColumn(name = CityLocalizationTable.Cols.CITY_ID)
    Long descriptorId;
    @StorIOSQLiteColumn(name = CityLocalizationTable.Cols.LANGUAGE)
    String lang;
    @StorIOSQLiteColumn(name = CityLocalizationTable.Cols.NAME)
    String name;
    @StorIOSQLiteColumn(name = CityLocalizationTable.Cols.ADDRESS)
    String address;

    CityLocalization() {
    }

    public CityLocalization(Long id, Long descriptorId, String lang, String name, String address) {
        this.id = id;
        this.descriptorId = descriptorId;
        this.lang = lang;
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public Long getDescriptorId() {
        return descriptorId;
    }

    public String getLang() {
        return lang;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
