package ru.yamblz.weather.ui.cities.favorite;

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

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.data.model.places.CityBuilder;
import ru.yamblz.weather.data.usecase.places.CitiesUseCase;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FavoriteCitiesPresenterTest {
    @Mock
    private CitiesUseCase useCase;
    @Mock
    private FavoriteCitiesViewImpl view;
    @Mock
    private Context context;

    private FavoriteCitiesPresenterImpl presenter;
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

        presenter = new FavoriteCitiesPresenterImpl(useCase, context);
        presenter.onAttach(view);
        scheduler = new TestScheduler();
    }

    @AfterClass
    public static void tearDownRxSchedulers() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Test
    public void onViewCreatedTest() {
        List<City> cities = new ArrayList<>();
        cities.add(new CityBuilder().setName("Msk").build());

        when(useCase.getFavoriteCities(anyString())).thenReturn(Single.just(cities));

        presenter.onViewCreated();

        verify(view).setFavoriteCities(eq(cities));
    }
}
