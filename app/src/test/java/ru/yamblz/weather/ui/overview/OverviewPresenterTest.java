package ru.yamblz.weather.ui.overview;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.data.usecase.OverviewUseCase;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyDouble;
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

    private OverviewContract.OverviewPresenter presenter;
    private TestScheduler testScheduler;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new OverviewPresenterImpl(useCase);
        testScheduler = new TestScheduler();
        presenter.onAttach(view);
    }

    @Test
    public void requestCurrentWeatherSuccess() throws Exception {
        Observable<WeatherResponse> observable = Observable.just(response).subscribeOn(testScheduler);
        when(useCase.loadCurrentWeather(anyDouble(),anyDouble(), anyBoolean())).thenReturn(observable);

        presenter.requestCurrentWeather(55, 56, true);

        verify(view).showLoading();

        testScheduler.triggerActions();

        verify(view).displayWeatherData(response);
        verify(view).hideLoading();
        verify(view, never()).showError();
    }

    @Test
    public void requestCurrentWeatherFailure() throws Exception {
        Observable<WeatherResponse> observable = Observable.error(new Exception("Some exception"));
        when(useCase.loadCurrentWeather(anyDouble(), anyDouble(), anyBoolean())).thenReturn(observable);

        presenter.requestCurrentWeather(55, 56, true);
        verify(view).showLoading();

        testScheduler.triggerActions();

        verify(view).showError();
        verify(view).hideLoading();
        verify(view, never()).displayWeatherData(any());
    }
}
