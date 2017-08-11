package ru.yamblz.weather.ui.cities;

import android.content.Context;
import android.support.annotation.NonNull;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.CityBuilder;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.data.usecase.places.CitiesUseCase;
import ru.yamblz.weather.data.usecase.places.GooglePlacesException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CitiesPresenterImplTest {
    @Mock
    private CitiesUseCase useCase;
    @Mock
    private CitiesContract.CitiesView view;
    @Mock
    private Context context;

    private CitiesContract.CitiesPresenter presenter;
    private TestScheduler scheduler;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new CitiesPresenterImpl(useCase, context);
        presenter.onAttach(view);
        scheduler = new TestScheduler();
    }

    @AfterClass
    public static void tearDownRxSchedulers() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Test
    public void onSearchTextChangedCorrect() {
        List<PlacePrediction> predictions = new ArrayList<>(1);
        predictions.add(new PlacePrediction("1", "Moscow", false));

        Single<List<PlacePrediction>> single = Single.just(predictions);
        when(useCase.loadPlacePredictions(anyString())).thenReturn(single.subscribeOn(scheduler));

        presenter.onSearchTextChanged("Moscow");

        scheduler.triggerActions();

        verify(view).showPredictions(predictions);
        verify(view, never()).showError(anyInt());
    }

    @Test
    public void onSearchTextChangedIncorrect() {
        GooglePlacesException exception = new GooglePlacesException(
                GooglePlacesException.ErrorDescription.REQUEST_DENIED);

        Single<List<PlacePrediction>> single = Single.error(exception);
        when(useCase.loadPlacePredictions(anyString())).thenReturn(single.subscribeOn(scheduler));

        presenter.onSearchTextChanged("Moscow");

        scheduler.triggerActions();

        verify(view).showError(exception.getDescriptionResId());
    }

    @Test
    public void onViewCreated() {
        final String cityName = "Moscow";

        City city = new CityBuilder()
                .setName(cityName)
                .build();
        Single<City> single = Single.just(city);

        when(useCase.getCurrentCity(anyString())).thenReturn(single.subscribeOn(scheduler));

        presenter.onViewCreated();

        verify(view).hideContent();

        scheduler.triggerActions();

        verify(view).showContent();
        verify(view).setSearchText(cityName);
    }

    @Test
    public void setCurrentLocationByPredictionCorrect() {
        Completable completable = Completable.complete();

        when(useCase.setCurrentLocationByPrediction(any(), anyString()))
                .thenReturn(completable.subscribeOn(scheduler));

        presenter.onPredictionSelected(new PlacePrediction("id", "name", false));

        verify(view).hideContent();

        scheduler.triggerActions();

        verify(view).onSelectionSuccessful();
    }

    @Test
    public void setCurrentLocationByPredictionIncorrect() {
        GooglePlacesException exception =
                new GooglePlacesException(GooglePlacesException.ErrorDescription.NOT_FOUND);
        Completable completable = Completable.error(exception);

        when(useCase.setCurrentLocationByPrediction(any(), anyString()))
                .thenReturn(completable.subscribeOn(scheduler));

        presenter.onPredictionSelected(new PlacePrediction("id", "name", false));

        verify(view).hideContent();

        scheduler.triggerActions();

        verify(view).showContent();
        verify(view).showError(exception.getDescriptionResId());
    }
}