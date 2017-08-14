package ru.yamblz.weather.ui.overview;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ru.yamblz.weather.data.model.places.Location;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class OverviewViewTest {

    @Test
    public void requestDataOnRefresh() {
        OverviewViewImpl view = new OverviewViewImpl();
        OverviewPresenterImpl presenter = mock(OverviewPresenterImpl.class);

        view.presenter = presenter;
        view.setCurrentLocation(new Location(20, 30));
        view.onRefresh();

        verify(presenter).requestWeather(any(Location.class), eq(true));
    }

    @Test
    public void requestDataOnRefreshIncorrect() {
        OverviewViewImpl view = new OverviewViewImpl();
        OverviewPresenterImpl presenter = mock(OverviewPresenterImpl.class);

        view.presenter = presenter;
        view.setCurrentLocation(null);
        view.onRefresh();

        verify(presenter).onViewCreated();
    }
}
