package ru.yamblz.weather.ui.overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import ru.yamblz.weather.R;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.response.Currently;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.ui.base.BaseFragment;
import ru.yamblz.weather.ui.main.MainActivity;
import ru.yamblz.weather.utils.Converter;
import ru.yamblz.weather.utils.GlobalConstants;
import ru.yamblz.weather.utils.RxBus;


public class OverviewViewImpl extends BaseFragment implements OverviewContract.OverviewView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.overviewContent)
    LinearLayout overviewContent;

    @BindView(R.id.temp)
    TextView temperature;

    @BindView(R.id.currentWeatherCondition)
    TextView currentWeatherCondition;

    @BindView(R.id.feelsLike)
    TextView feelsLike;

    @BindView(R.id.humidity)
    TextView humidity;

    @BindView(R.id.clouds)
    TextView clouds;

    @BindView(R.id.iconImage)
    ImageView icon;

    @Inject
    OverviewPresenterImpl presenter;

    @Inject
    Converter converter;

    @Inject
    RxBus rxBus;

    private ActionBar actionBar;
    private Location currentLocation;

    @Override
    protected int provideLayout() {
        return R.layout.fragment_overview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getActivityComponent().inject(this);
        rxBus.subscribe(GlobalConstants.WEATHER_INSTANT_CACHE,
                this,
                (weatherResponse) -> displayWeatherData((WeatherResponse) weatherResponse));
        presenter.onAttach(this);
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();

        swipeRefreshLayout.setOnRefreshListener(this);

        presenter.requestInitialData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rxBus.unsubscribe(this);
        presenter.onDetach();
        actionBar = null;
    }

    @Override
    public void onRefresh() {
        if (currentLocation != null) {
            presenter.requestCurrentWeather(
                    currentLocation.getLatitude(), currentLocation.getLongitude(), true);
        } else {
            presenter.requestInitialData();
        }
    }

    @Override
    public void displayWeatherData(WeatherResponse weatherResponse) {
        overviewContent.setVisibility(View.VISIBLE);
        Currently currently = weatherResponse.getCurrently();
        temperature.setText(getString(R.string.degree, converter.convertTemperature(currently.getTemperature())));
        currentWeatherCondition.setText(currently.getSummary());
        icon.setImageResource(converter.convertIconToRes(currently.getIcon()));
        feelsLike.setText(getString(R.string.degree, converter.convertTemperature(currently.getApparentTemperature())));
        humidity.setText(getString(R.string.percent, converter.convertToPercentage(currently.getHumidity())));
        clouds.setText(getString(R.string.percent, converter.convertToPercentage(currently.getCloudCover())));
    }

    @Override
    public void displayCityName(String name) {
        //noinspection ConstantConditions
        actionBar.setTitle(name);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext().getApplicationContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setCurrentLocation(Location location) {
        // TODO: 27.07.2017 После добавления городов подгрузка данных из памяти может выдавать неверный результат.
        presenter.requestCurrentWeather(location.getLatitude(), location.getLongitude(), true);
        currentLocation = location;
        displayCityName(location.getTitle());
    }
}
