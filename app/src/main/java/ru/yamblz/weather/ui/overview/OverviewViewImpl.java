package ru.yamblz.weather.ui.overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yamblz.weather.R;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.response.Currently;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.data.model.weather.Weather;
import ru.yamblz.weather.ui.base.BaseFragment;
import ru.yamblz.weather.ui.main.MainActivity;
import ru.yamblz.weather.ui.overview.model.DailyForecast;
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

    @BindView(R.id.forecast_recycler_view)
    RecyclerView forecastRecyclerView;

    @Inject
    OverviewPresenterImpl presenter;

    @Inject
    Converter converter;

    @Inject
    RxBus rxBus;

    private ActionBar actionBar;
    private Location currentLocation;
    private ForecastAdapter forecastAdapter;
    private DateFormat dateFormat;

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
        setupForecastRecyclerView();
        dateFormat = new SimpleDateFormat("d.MM");
        dateFormat.setTimeZone(TimeZone.getDefault());

        presenter.onViewCreated();
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
            presenter.requestWeather(currentLocation, true);
        } else {
            presenter.onViewCreated();
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
    public void displayWeatherData(Weather weather) {
        overviewContent.setVisibility(View.VISIBLE);
        temperature.setText(getString(R.string.degree, converter.convertTemperature(weather.getTemperature())));
        currentWeatherCondition.setText(weather.getCondition());
        icon.setImageResource(converter.convertIconToRes(weather.getIcon()));
        feelsLike.setText(getString(R.string.degree, converter.convertTemperature(weather.getApparent())));
        humidity.setText(getString(R.string.percent, converter.convertToPercentage(weather.getHumidity())));
        clouds.setText(getString(R.string.percent, converter.convertToPercentage(weather.getClouds())));
    }

    @Override
    public void displayCityName(String name) {
        actionBar.setTitle(name);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext().getApplicationContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setCurrentLocation(Location location) {
        currentLocation = location;
    }

    @Override
    public void setForecasts(List<DailyForecast> forecasts) {
        forecastAdapter.setForecasts(forecasts);
        forecastAdapter.notifyDataSetChanged();
    }

    private void setupForecastRecyclerView() {
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        forecastAdapter = new ForecastAdapter();
        forecastRecyclerView.setAdapter(forecastAdapter);
        forecastRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private DailyForecast forecast;

        @BindView(R.id.date_text_view)
        TextView dateTextView;
        @BindView(R.id.icon_image_view)
        ImageView iconImageView;
        @BindView(R.id.temp_day_text_view)
        TextView tempDay;
        @BindView(R.id.temp_night_text_view)
        TextView tempNight;

        ForecastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }

        void bindItem(DailyForecast forecast) {
            this.forecast = forecast;
            dateTextView.setText(dateFormat.format(forecast.getDate()));
            iconImageView.setImageResource(converter.convertIconToRes(forecast.getIconRes()));
            tempDay.setText(getString(R.string.degree, forecast.getTemperatureDay()));
            tempNight.setText(getString(R.string.degree, forecast.getTemperatureNight()));
        }
    }

    private class ForecastAdapter extends RecyclerView.Adapter<ForecastViewHolder> {
        private List<DailyForecast> forecasts = Collections.emptyList();

        @Override
        public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new ForecastViewHolder(
                    inflater.inflate(R.layout.overview_forecasts_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ForecastViewHolder holder, int position) {
            holder.bindItem(forecasts.get(position));
        }

        @Override
        public int getItemCount() {
            return forecasts.size();
        }

        void setForecasts(List<DailyForecast> forecasts) {
            this.forecasts = forecasts;
        }
    }
}
