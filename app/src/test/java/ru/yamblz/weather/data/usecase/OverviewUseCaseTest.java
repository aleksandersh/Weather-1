package ru.yamblz.weather.data.usecase;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;
import ru.yamblz.weather.data.TestSchedulerProvider;
import ru.yamblz.weather.data.local.LocalService;
import ru.yamblz.weather.data.model.response.Currently;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.data.network.Api;
import ru.yamblz.weather.utils.RxBus;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class OverviewUseCaseTest {

    @Mock
    Api api;

    @Mock
    LocalService localService;

    @Mock
    RxBus rxBus;

    private OverviewUseCase overviewUseCase;
    private TestScheduler testScheduler;

    private WeatherResponse actualResponse;
    private Throwable actualError;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        actualResponse = null;
        actualError = null;
        testScheduler = new TestScheduler();
        overviewUseCase = new OverviewUseCaseImpl(api, localService, rxBus, new TestSchedulerProvider(testScheduler));
    }

    @Test
    public void loadCurrentWeatherNetworkSuccess() throws Exception {
        WeatherResponse expectedResponse = new WeatherResponse();
        Single<WeatherResponse> single = Single.just(expectedResponse);
        when(api.getWeather(anyString(), anyDouble(), anyDouble())).thenReturn(single);

        overviewUseCase.loadCurrentWeather(55, 55, true).subscribe(
                weatherResponse -> actualResponse = weatherResponse,
                err -> {}
        );

        testScheduler.triggerActions();

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void loadCurrentWeatherNetworkFailure() throws Exception {
        Throwable expectedError = new Exception("Some error");
        Single<WeatherResponse> single = Single.error(expectedError);
        when(api.getWeather(anyString(), anyDouble(), anyDouble())).thenReturn(single);

        overviewUseCase.loadCurrentWeather(55, 55, true).subscribe(
                weatherResponse -> {},
                err -> actualError = err
        );

        testScheduler.triggerActions();

        assertNotNull(actualError);
        assertEquals(expectedError, actualError);
    }

    @Test
    public void loadCurrentWeatherLocalSuccess() throws Exception {
        WeatherResponse expectedResponse = new WeatherResponse();
        expectedResponse.setCurrently(new Currently());
        Single<WeatherResponse> single = Single.just(expectedResponse);
        when(localService.readResponseFromFile()).thenReturn(single);
        when(api.getWeather(anyString(), anyDouble(), anyDouble())).thenReturn(Single.error(new Exception()));

        overviewUseCase.loadCurrentWeather(55, 55, false).subscribe(
                weatherResponse -> actualResponse = weatherResponse,
                err -> {}
        );

        testScheduler.triggerActions();

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void loadCurrentWeatherLocalFailure() throws Exception {
        WeatherResponse localException = new WeatherResponse();
        Exception networkException = new Exception("Network failure");
        Single<WeatherResponse> singleLocal = Single.just(localException); // return empty item onException
        Single<WeatherResponse> singleNetwork = Single.error(networkException);
        when(localService.readResponseFromFile()).thenReturn(singleLocal);
        when(api.getWeather(anyString(), anyDouble(), anyDouble())).thenReturn(singleNetwork);

        overviewUseCase.loadCurrentWeather(55, 55, false).subscribe(
                weatherResponse -> {},
                err -> actualError = err
        );

        assertNotNull(actualError);
        assertEquals(networkException.getMessage(), actualError.getMessage());
    }
}
