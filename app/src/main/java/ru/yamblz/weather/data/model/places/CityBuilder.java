package ru.yamblz.weather.data.model.places;

import ru.yamblz.weather.data.model.ModelBuilder;

public class CityBuilder implements ModelBuilder<City> {
    private String name;
    private String address;
    private String lang;
    private double latitude;
    private double longitude;
    private boolean favorite;
    private String googlePlacesId;

    public CityBuilder() {
    }

    public CityBuilder(City city) {
        name = city.getName();
        address = city.getAddress();
        lang = city.getLang();
        latitude = city.getLatitude();
        longitude = city.getLongitude();
        favorite = city.isFavorite();
        googlePlacesId = city.getGooglePlacesId();
    }

    @Override
    public City build() {
        City city = new City();
        city.name = name;
        city.address = address;
        city.lang = lang;
        city.latitude = latitude;
        city.longitude = longitude;
        city.favorite = favorite;
        city.googlePlacesId = googlePlacesId;

        return city;
    }

    public CityBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CityBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public CityBuilder setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public CityBuilder setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public CityBuilder setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public CityBuilder setFavorite(boolean favorite) {
        this.favorite = favorite;
        return this;
    }

    public CityBuilder setGooglePlacesId(String googlePlacesId) {
        this.googlePlacesId = googlePlacesId;
        return this;
    }
}
