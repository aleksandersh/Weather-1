package ru.yamblz.weather.ui.overview;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class OverviewViewTest {

    @Test
    @Ignore("Локация может быть не задана")
    public void requestDataOnRefresh() {
        OverviewViewImpl view = new OverviewViewImpl();
        OverviewPresenterImpl presenter = mock(OverviewPresenterImpl.class);

        view.presenter = presenter;
        view.onRefresh();
        verify(presenter).requestCurrentWeather(anyDouble(), anyDouble(), eq(true));
    }
}
