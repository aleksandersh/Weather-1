package ru.yamblz.weather.data.usecase.places;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import ru.yamblz.weather.data.database.WeatherDao;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.CityBuilder;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.data.network.PlacesApiClient;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by AleksanderSh on 30.07.2017.
 */
public class GooglePlacesUseCaseImplTest {
    @Mock
    PlacesApiClient apiClient;
    @Mock
    AppPreferenceManager preferenceManager;
    @Mock
    WeatherDao dao;

    private CitiesUseCase useCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        useCase = new GooglePlacesUseCaseImpl(apiClient, preferenceManager, dao);
    }

    /**
     * Тестирование объединения результатов бд и сети.
     */
    @Test
    public void loadPlacePredictions() {
        City cityDb1 = buildCity("Moscow", "Moscow_addr", 30, 40, "msk", true).build();

        PlacePrediction predictionNw1 = new PlacePrediction("ny", "New-York_addr", false);
        PlacePrediction predictionNw2 = new PlacePrediction("msk", "Moscow_addr", false);

        final List<PlacePrediction> predictionsNw = new ArrayList<>(2);
        predictionsNw.add(predictionNw1);
        predictionsNw.add(predictionNw2);

        final TestObserver<List<PlacePrediction>> testObserver = TestObserver.create();

        final Single<City> dbSingleCorrect = Single.just(cityDb1);
        final Single<City> dbSingleIncorrect = Single.create(e -> e.onError(new NoSuchElementException()));
        final Single<List<PlacePrediction>> nwSingle = Single.just(predictionsNw);

        when(dao.getCityByGooglePlaceId(eq("msk"))).thenReturn(dbSingleCorrect);
        when(dao.getCityByGooglePlaceId(eq("ny"))).thenReturn(dbSingleIncorrect);
        when(apiClient.loadPlacePredictions(eq("Mos"))).thenReturn(nwSingle);

        useCase.loadPlacePredictions("Mos").subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        assertEquals(testObserver.valueCount(), 1);

        List<PlacePrediction> predictions = testObserver.values().get(0);
        assertEquals(predictions.size(), 2);

        PlacePrediction predictionMsk = predictions.get(0);
        assertEquals(predictionMsk.getId(), "msk");
        assertEquals(predictionMsk.getText(), "Moscow_addr");
        assertEquals(predictionMsk.isFavorite(), true);

        PlacePrediction predictionNy = predictions.get(1);
        assertEquals(predictionNy.getId(), "ny");
        assertEquals(predictionNy.getText(), "New-York_addr");
        assertEquals(predictionNy.isFavorite(), false);
    }

    /**
     * Если в базе данных уже существует такой город, то локация устанавливается для него.
     */
    @Test
    public void setCurrentLocationByPredictionFromDb() {
        final String cityName = "Moscow";
        final String cityAddress = "Moscow_addr";
        final double cityLat = 30;
        final double cityLng = 40;
        final String cityId = "msk";
        final String lang = "ru";

        final City city = buildCity(cityName, cityAddress, cityLat, cityLng).build();
        final Location location = new Location(cityLat, cityLng);

        final TestObserver<List<PlacePrediction>> testObserver = TestObserver.create();
        final Single<City> single = Single.just(city);
        final Single<City> wrongSingle = Single.create(e -> e.onError(null));
        final ArgumentCaptor<Location> argumentCaptor = ArgumentCaptor.forClass(Location.class);

        when(dao.getCityByGooglePlaceId(cityId, lang)).thenReturn(single);
        when(apiClient.loadCityByPlaceId(anyString(), anyString())).thenReturn(wrongSingle);

        useCase.setCurrentLocationByPrediction(
                new PlacePrediction(cityId, "some_name", false), lang)
                .subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        verify(preferenceManager).setCurrentLocation(argumentCaptor.capture());
        assertEquals(location, argumentCaptor.getValue());
    }

    /**
     * Если город не найден в базе данных, выполняется запрос к сервису и данные сохраняются в бд.
     */
    @Test
    public void setCurrentLocationByPredictionFromNetwork() {
        final String cityName = "Moscow";
        final String cityAddress = "Moscow_addr";
        final double cityLat = 30;
        final double cityLng = 40;
        final String cityId = "msk";
        final String lang = "ru";

        final Location location = new Location(cityLat, cityLng);
        final City city = buildCity(cityName, cityAddress, cityLat, cityLng, cityId, false)
                .setLang(lang)
                .build();

        final TestObserver<List<PlacePrediction>> testObserver = TestObserver.create();
        final Single<City> single = Single.just(city);
        final Single<City> wrongSingle = Single.create(e -> e.onError(null));
        final ArgumentCaptor<Location> locationArgCaptor = ArgumentCaptor.forClass(Location.class);
        final ArgumentCaptor<City> cityArgCaptor = ArgumentCaptor.forClass(City.class);

        when(apiClient.loadCityByPlaceId(eq(cityId), eq(lang))).thenReturn(single);
        when(dao.getCityByGooglePlaceId(anyString(), anyString())).thenReturn(wrongSingle);

        useCase.setCurrentLocationByPrediction(
                new PlacePrediction(cityId, cityName, false), lang)
                .subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        verify(preferenceManager).setCurrentLocation(locationArgCaptor.capture());
        assertEquals(location, locationArgCaptor.getValue());

        verify(dao).saveCity(cityArgCaptor.capture());

        City cityCaptured = cityArgCaptor.getValue();
        assertEquals(cityName, cityCaptured.getName());
        assertEquals(lang, cityCaptured.getLang());
        assertEquals(cityLat, cityCaptured.getLatitude(), 0);
        assertEquals(cityLng, cityCaptured.getLongitude(), 0);
        assertEquals(cityId, cityCaptured.getGooglePlacesId());
    }

    @Test
    public void getCurrentCity() {
        final String lang = "ru";
        final double lat = 30;
        final double lng = 40;
        final String cityName = "Moscow";
        final String cityAddress = "Moscow_addr";

        final Location location = new Location(lat, lng);
        final City city = buildCity(cityName, cityAddress, lat, lng).build();

        final TestObserver<City> testObserver = TestObserver.create();

        when(preferenceManager.getLocation()).thenReturn(location);
        when(dao.getCityByCoordinates(eq(lat), eq(lng), eq(lang))).thenReturn(Single.just(city));

        useCase.getCurrentCity(lang).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        testObserver.assertValue(city);
    }

    private CityBuilder buildCity(String name, String address, double lat, double lng) {
        return buildCity(name, address, lat, lng, "id", false);
    }

    private CityBuilder buildCity(String name, String address, double lat, double lng, String placeId, boolean favorite) {
        return new CityBuilder()
                .setName(name)
                .setAddress(address)
                .setLatitude(lat)
                .setLongitude(lng)
                .setGooglePlacesId(placeId)
                .setFavorite(favorite);
    }
}