package ru.yamblz.weather.data.model.converter;

import ru.yamblz.weather.data.model.googlePlacesResponse.details.PlaceDetailsDto;
import ru.yamblz.weather.data.model.places.CityBuilder;

public class PlaceDetailsToCityConverter
        implements DtoToModelConverter<PlaceDetailsDto, CityBuilder> {
    @Override
    public CityBuilder convert(PlaceDetailsDto dto) {
        return new CityBuilder()
                .setName(dto.getName())
                .setAddress(dto.getAddress())
                .setLatitude(dto.getGeometry().getLocation().getLat())
                .setLongitude(dto.getGeometry().getLocation().getLng())
                .setGooglePlacesId(dto.getPlaceId());
    }
}
