package ru.yamblz.weather.ui.overview;

import android.content.Context;
import android.support.annotation.NonNull;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.CityBuilder;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.data.model.weather.Weather;
import ru.yamblz.weather.data.model.weather.WeatherBuilder;
import ru.yamblz.weather.data.usecase.OverviewUseCase;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OverviewPresenterTest {

    @Mock
    OverviewUseCase useCase;

    @Mock
    OverviewContract.OverviewView view;

    @Mock
    WeatherResponse response;

    @Mock
    Context context;

    private OverviewContract.OverviewPresenter presenter;
    private TestScheduler testScheduler;

    @BeforeClass
    public static void setUpRxSchedulers() {
        Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new OverviewPresenterImpl(useCase, context);
        testScheduler = new TestScheduler();
        presenter.onAttach(view);
    }

    @AfterClass
    public static void tearDownRxSchedulers() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Test
    public void requestCurrentWeatherSuccess() throws Exception {
        final double lat = 20;
        final double lng = 30;
        final Weather weather = new WeatherBuilder()
                .setLatitude(lat)
                .setLongitude(lng)
                .build();
        final Location location = new Location(lat, lng);

        final Single<Weather> single = Single.just(weather).observeOn(testScheduler);

        when(useCase.loadWeather(eq(location), anyString(), anyBoolean())).thenReturn(single);

        presenter.requestWeather(location, true);

        verify(view).showLoading();

        testScheduler.triggerActions();

        verify(view).hideLoading();
        verify(view).displayWeatherData(weather);
        verify(view, never()).showError();
    }

    @Test
    public void requestCurrentWeatherFailure() throws Exception {
        final Single<Weather> single = Single.error(new Exception("Some exception"));
        when(useCase.loadWeather(any(Location.class), anyString(), anyBoolean())).thenReturn(single);

        presenter.requestWeather(new Location(55, 56), true);

        verify(view).showLoading();

        verify(view).showError();
        verify(view).hideLoading();
        verify(view, never()).displayWeatherData(any(Weather.class));
    }

    @Test
    public void onViewCreatedSuccess() throws Exception {
        final Location location = new Location(20, 30);

        final Observable<Location> observable = Observable.just(location).observeOn(testScheduler);
        final Single<City> singleCity = Single.just(new City());
        final Single<Weather> singleWeather = Single.just(new Weather());

        when(useCase.getCurrentLocation()).thenReturn(observable);
        when(useCase.getCityByCoordinates(anyDouble(), anyDouble(), anyString())).thenReturn(singleCity);
        when(useCase.loadWeather(any(Location.class), anyString(), anyBoolean())).thenReturn(singleWeather);

        presenter.onViewCreated();

        verify(view).showLoading();

        testScheduler.triggerActions();

        verify(view).setCurrentLocation(location);
        verify(view, never()).showError();
    }

    @Test
    public void requestCityByLocationCorrect() throws Exception {
        final String name = "Moscow";
        final Single<City> singleCity = Single.just(new CityBuilder().setName(name).build());

        when(useCase.getCityByCoordinates(anyDouble(), anyDouble(), anyString())).thenReturn(singleCity);

        presenter.requestCityByLocation(new Location(20, 30));

        verify(view).displayCityName(eq(name));
        verify(view, never()).showError();
    }

    @Test
    public void requestCityByLocationIncorrect() throws Exception {
        final Single<City> singleCity = Single.error(new NoSuchElementException());

        when(useCase.getCityByCoordinates(anyDouble(), anyDouble(), anyString())).thenReturn(singleCity);

        presenter.requestCityByLocation(new Location(20, 30));

        verify(view).showError();
    }
}
