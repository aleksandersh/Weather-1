package ru.yamblz.weather.data.network.googlePlaces;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import ru.yamblz.weather.data.model.converter.PlaceDetailsToCityConverter;
import ru.yamblz.weather.data.model.googlePlacesResponse.details.GeometryDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.details.LocationDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.details.PlaceDetailsDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.details.PlaceDetailsResponseDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.PredictionDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.PredictionsResponseDto;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.data.network.PlacesApiClient;
import ru.yamblz.weather.data.usecase.places.GooglePlacesException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by AleksanderSh on 09.08.2017.
 */

public class GooglePlacesClientTest {
    @Mock
    GooglePlacesApi api;

    private PlacesApiClient client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        client = new GooglePlacesClient(api, new PlaceDetailsToCityConverter());
    }

    @Test
    public void loadPlacePredictionsCorrect() {
        final String text = "Mos";

        final PredictionDto dto1 = new PredictionDto("Moscow", "", "msk", "",
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        final PredictionDto dto2 = new PredictionDto("New-York", "", "ny", "",
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

        final List<PredictionDto> dtoList = new ArrayList<>(2);
        dtoList.add(dto1);
        dtoList.add(dto2);
        final PredictionsResponseDto dto = new PredictionsResponseDto("OK", dtoList);

        final TestObserver<List<PlacePrediction>> testObserver = TestObserver.create();

        final Single<PredictionsResponseDto> single = Single.just(dto);

        when(api.getPredictions(anyString(), eq(text), anyString())).thenReturn(single);

        client.loadPlacePredictions(text).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        assertEquals(testObserver.valueCount(), 1);

        List<PlacePrediction> predictions = testObserver.values().get(0);
        assertEquals(predictions.size(), 2);

        PlacePrediction predictionMsk = null;
        PlacePrediction predictionNy = null;
        for (PlacePrediction pr : predictions) {
            if (pr.getId().equals("msk")) predictionMsk = pr;
            else if (pr.getId().equals("ny")) predictionNy = pr;
        }
        assertNotNull(predictionMsk);
        assertEquals(predictionMsk.isFavorite(), false);
        assertEquals(predictionMsk.getText(), "Moscow");

        assertNotNull(predictionNy);
        assertEquals(predictionNy.isFavorite(), false);
        assertEquals(predictionNy.getText(), "New-York");
    }

    @Test
    public void loadPlacePredictionsFailure() {
        final PredictionsResponseDto dto = new PredictionsResponseDto("REQUEST_DENIED", Collections.emptyList());

        final TestObserver<List<PlacePrediction>> testObserver = TestObserver.create();

        final Single<PredictionsResponseDto> networkSingle = Single.just(dto);

        when(api.getPredictions(anyString(), anyString(), anyString())).thenReturn(networkSingle);

        client.loadPlacePredictions("Mos").subscribe(testObserver);

        testObserver.assertError(GooglePlacesException.class);
    }

    @Test
    public void loadCityByPlaceIdCorrect() {
        final String cityName = "Moscow";
        final String cityAddress = "Moscow_addr";
        final double cityLat = 30;
        final double cityLng = 40;
        final String cityId = "msk";
        final String lang = "ru";

        final PlaceDetailsResponseDto dto =
                buildCorrectPlaceDetailsResponseDto(cityName, cityAddress, cityId, cityLat, cityLng);

        final TestObserver<City> testObserver = TestObserver.create();
        final Single<PlaceDetailsResponseDto> single = Single.just(dto);

        when(api.getPlaceDetails(anyString(), eq(cityId), eq(lang))).thenReturn(single);

        client.loadCityByPlaceId(cityId, lang).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        assertEquals(testObserver.valueCount(), 1);

        City city = testObserver.values().get(0);
        assertEquals(city.getName(), cityName);
        assertEquals(city.getAddress(), cityAddress);
        assertEquals(city.getLatitude(), cityLat, 0);
        assertEquals(city.getLongitude(), cityLng, 0);
        assertEquals(city.getGooglePlacesId(), cityId);
        assertEquals(city.getLang(), lang);
    }

    @Test
    public void loadCityByPlaceIdIncorrect() {
        final PlaceDetailsResponseDto dto = new PlaceDetailsResponseDto(null, "REQUEST_DENIED");

        final TestObserver<City> testObserver = TestObserver.create();
        final Single<PlaceDetailsResponseDto> single = Single.just(dto);

        when(api.getPlaceDetails(anyString(), anyString(), anyString())).thenReturn(single);

        client.loadCityByPlaceId("1", "ru").subscribe(testObserver);

        testObserver.assertError(GooglePlacesException.class);
    }

    private PlaceDetailsResponseDto buildCorrectPlaceDetailsResponseDto(
            String name, String address, String placeId, double lat, double lng) {
        return new PlaceDetailsResponseDto(
                new PlaceDetailsDto(
                        new GeometryDto(
                                new LocationDto(lat, lng))
                        , name, address, placeId, "some_reference")
                , "OK");
    }
}
