package ru.yamblz.weather.data.database.storio.entity;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import ru.yamblz.weather.data.database.WeatherDbSchema.CityDescriptorTable;

/**
 * Created by AleksanderSh on 10.08.2017.
 * <p>
 * Костыль. В рамках работы с базой данных удобнее оперировать объектом, содержащим общее
 * описание города.
 */

@StorIOSQLiteType(table = CityDescriptorTable.NAME)
public class CityDescriptor {
    @StorIOSQLiteColumn(name = CityDescriptorTable.Cols._ID, key = true)
    Long id;
    @StorIOSQLiteColumn(name = CityDescriptorTable.Cols.LATITUDE)
    Double latitude;
    @StorIOSQLiteColumn(name = CityDescriptorTable.Cols.LONGITUDE)
    Double longitude;
    @StorIOSQLiteColumn(name = CityDescriptorTable.Cols.FAVORITE)
    Boolean favorite;
    @StorIOSQLiteColumn(name = CityDescriptorTable.Cols.GP_ID)
    String googlePlacesId;

    CityDescriptor() {
    }

    public CityDescriptor(Long id, Double latitude, Double longitude, Boolean favorite, String googlePlacesId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.favorite = favorite;
        this.googlePlacesId = googlePlacesId;
    }

    public Long getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public String getGooglePlacesId() {
        return googlePlacesId;
    }
}
