package ru.yamblz.weather.ui.cities;

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
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.data.usecase.places.CitiesUseCase;
import ru.yamblz.weather.data.usecase.places.GooglePlacesException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by AleksanderSh on 30.07.2017.
 */
public class CitiesPresenterImplTest {
    @Mock
    private CitiesUseCase mUseCase;
    @Mock
    private CitiesContract.CitiesView mView;

    private CitiesContract.CitiesPresenter mPresenter;
    private TestScheduler mScheduler;

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

        mPresenter = new CitiesPresenterImpl(mUseCase);
        mPresenter.onAttach(mView);
        mScheduler = new TestScheduler();
    }

    @AfterClass
    public static void tearDownRxSchedulers() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Test
    public void requestPredictionsCorrect() {
        List<PlacePrediction> predictions = new ArrayList<>(1);
        predictions.add(new PlacePrediction("1", "Moscow", "Moscow, Russia"));

        Single<List<PlacePrediction>> single = Single.just(predictions);
        when(mUseCase.loadPlacePredictions(anyString())).thenReturn(single.subscribeOn(mScheduler));

        mPresenter.requestPredictions("Moscow");

        mScheduler.triggerActions();

        verify(mView).showPredictions(predictions);
        verify(mView, never()).showError(anyInt());
    }

    @Test
    public void requestPredictionsIncorrect() {
        Single<List<PlacePrediction>> single = Single.error(new GooglePlacesException(
                GooglePlacesException.ErrorDescription.REQUEST_DENIED));
        when(mUseCase.loadPlacePredictions(anyString())).thenReturn(single.subscribeOn(mScheduler));

        mPresenter.requestPredictions("Moscow");

        mScheduler.triggerActions();

        verify(mView).showError(anyInt());
    }

    @Test
    public void requestInitialData() {
        Location location = new Location("Moscow", 35, 45);
        Single<Location> single = Single.just(location);

        when(mUseCase.getCurrentLocation()).thenReturn(single.subscribeOn(mScheduler));

        mPresenter.requestInitialData();

        verify(mView).hideContent();

        mScheduler.triggerActions();

        verify(mView).showContent();
        verify(mView).setCurrentLocation(location);
    }

    @Test
    public void setCurrentLocationByPredictionCorrect() {
        Completable completable = Completable.complete();

        when(mUseCase.setCurrentLocationByPrediction(any()))
                .thenReturn(completable.subscribeOn(mScheduler));

        mPresenter.setCurrentLocationByPrediction(new PlacePrediction("id", "name", "text"));

        verify(mView).hideContent();

        mScheduler.triggerActions();

        verify(mView).onSelectionSuccessful();
    }

    @Test
    public void setCurrentLocationByPredictionIncorrect() {
        GooglePlacesException exception =
                new GooglePlacesException(GooglePlacesException.ErrorDescription.NOT_FOUND);
        Completable completable = Completable.error(exception);

        when(mUseCase.setCurrentLocationByPrediction(any()))
                .thenReturn(completable.subscribeOn(mScheduler));

        mPresenter.setCurrentLocationByPrediction(new PlacePrediction("id", "name", "text"));

        verify(mView).hideContent();

        mScheduler.triggerActions();

        verify(mView).showContent();
        verify(mView).showError(exception.getDescriptionResId());
    }
}