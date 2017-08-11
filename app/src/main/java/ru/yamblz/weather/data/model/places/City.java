package ru.yamblz.weather.data.model.places;

/**
 * Created by AleksanderSh on 03.08.2017.
 * <p>
 * Представление города в рамках бизнес-логики приложения.
 */

public class City {
    String name;
    String address;
    String lang;
    double latitude;
    double longitude;
    boolean favorite;
    String googlePlacesId;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLang() {
        return lang;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getGooglePlacesId() {
        return googlePlacesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (Double.compare(city.latitude, latitude) != 0) return false;
        if (Double.compare(city.longitude, longitude) != 0) return false;
        if (favorite != city.favorite) return false;
        if (name != null ? !name.equals(city.name) : city.name != null) return false;
        if (address != null ? !address.equals(city.address) : city.address != null) return false;
        if (lang != null ? !lang.equals(city.lang) : city.lang != null) return false;
        return googlePlacesId != null ? googlePlacesId.equals(city.googlePlacesId) : city.googlePlacesId == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (favorite ? 1 : 0);
        result = 31 * result + (googlePlacesId != null ? googlePlacesId.hashCode() : 0);
        return result;
    }
}
