package ru.yamblz.weather.data.usecase;


import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.NoSuchElementException;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import ru.yamblz.weather.data.TestSchedulerProvider;
import ru.yamblz.weather.data.database.WeatherDao;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.CityBuilder;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.weather.Weather;
import ru.yamblz.weather.data.network.PlacesApiClient;
import ru.yamblz.weather.data.network.WeatherApiClient;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OverviewUseCaseTest {
    @Mock
    AppPreferenceManager preferenceManager;
    @Mock
    WeatherDao dao;
    @Mock
    WeatherApiClient weatherClient;
    @Mock
    PlacesApiClient placesClient;

    private OverviewUseCase overviewUseCase;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestScheduler testScheduler = new TestScheduler();
        overviewUseCase = new OverviewUseCaseImpl(new TestSchedulerProvider(testScheduler),
                preferenceManager, dao, weatherClient, placesClient);
    }

    @Test
    public void loadWeatherNetworkSuccess() throws Exception {
        final String lang = "ru";
        final Location location = new Location(20, 30);

        final Weather weather = new Weather(1L, 1L, "ru", 20D, 30D, 10D, "norm", 10D, 10D, 10D, "rain", Collections.emptyList());
        final Single<Weather> single = Single.just(weather);
        final ArgumentCaptor<Weather> argumentCaptor = ArgumentCaptor.forClass(Weather.class);
        final TestObserver<Weather> testObserver = TestObserver.create();

        when(weatherClient.getWeather(eq(20d), eq(30d), eq(lang))).thenReturn(single);

        overviewUseCase.loadWeather(location, lang, true).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        verify(dao, never()).getWeatherByCoordinates(anyDouble(), anyDouble(), anyString());
        verify(dao).saveWeather(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue(), weather);

        assertEquals(testObserver.valueCount(), 1);
        assertEquals(testObserver.values().size(), 1);
        assertEquals(testObserver.values().get(0), weather);
    }

    @Test
    public void loadWeatherLocalSuccess() throws Exception {
        final String lang = "ru";
        final Location location = new Location(20, 30);

        final Weather weather = new Weather(1L, 1L, "ru", 20D, 30D, 10D, "norm", 10D, 10D, 10D, "rain", Collections.emptyList());
        final Single<Weather> single = Single.just(weather);
        final Single<Weather> incorrectSingle = Single.create(e -> e.onError(null));
        final TestObserver<Weather> testObserver = TestObserver.create();

        when(dao.getWeatherByCoordinates(eq(20d), eq(30d), eq(lang))).thenReturn(single);
        when(weatherClient.getWeather(eq(20d), eq(30d), eq(lang))).thenReturn(incorrectSingle);

        overviewUseCase.loadWeather(location, lang, false).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        assertEquals(testObserver.valueCount(), 1);
        assertEquals(testObserver.values().size(), 1);
        assertEquals(testObserver.values().get(0), weather);
    }

    @Test
    public void loadWeatherLocalFailure() throws Exception {
        final String lang = "ru";
        final Location location = new Location(20, 30);

        final Weather weather = new Weather(1L, 1L, "ru", 20D, 30D, 10D, "norm", 10D, 10D, 10D, "rain", Collections.emptyList());
        final Single<Weather> single = Single.just(weather);
        final Single<Weather> incorrectSingle = Single.create(e -> e.onError(null));
        final TestObserver<Weather> testObserver = TestObserver.create();

        when(dao.getWeatherByCoordinates(eq(20d), eq(30d), eq(lang))).thenReturn(incorrectSingle);
        when(weatherClient.getWeather(eq(20d), eq(30d), eq(lang))).thenReturn(single);

        overviewUseCase.loadWeather(location, lang, false).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        assertEquals(testObserver.valueCount(), 1);
        assertEquals(testObserver.values().size(), 1);
        assertEquals(testObserver.values().get(0), weather);
    }

    @Test
    public void getCityByCoordinatesNetwork() throws Exception {
        final String cityName = "Moscow";
        final String cityAddress = "Moscow_addr";
        final double cityLat = 30;
        final double cityLng = 40;
        final String cityId = "msk";
        final String langSaved = "en";
        final String langPrimary = "ru";

        final CityBuilder builder = new CityBuilder()
                .setName(cityName)
                .setAddress(cityAddress)
                .setLatitude(cityLat)
                .setLongitude(cityLng)
                .setGooglePlacesId(cityId);

        final City cityDb = builder.setLang(langSaved).build();
        final City cityNw = builder.setLang(langPrimary).build();

        final Single<City> singleDbError = Single.create(emitter -> emitter.onError(new NoSuchElementException()));
        final Single<City> singleDb = Single.just(cityDb);
        final Single<City> singleNw = Single.just(cityNw);

        final TestObserver<City> testObserver = TestObserver.create();
        final ArgumentCaptor<City> cityArgCaptor = ArgumentCaptor.forClass(City.class);

        when(dao.getCityByCoordinates(eq(cityLat), eq(cityLng), eq(langPrimary))).thenReturn(singleDbError);
        when(dao.getCityByCoordinates(eq(cityLat), eq(cityLng))).thenReturn(singleDb);
        when(placesClient.loadCityByPlaceId(eq(cityId), eq(langPrimary))).thenReturn(singleNw);

        overviewUseCase.getCityByCoordinates(cityLat, cityLng, langPrimary).subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        testObserver.assertValue(cityNw);

        verify(dao).saveCity(cityArgCaptor.capture());
        assertEquals(cityArgCaptor.getValue(), cityNw);
    }
}
