package ru.yamblz.weather.ui.overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import ru.yamblz.weather.R;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.ui.base.BaseFragment;
import ru.yamblz.weather.ui.main.MainActivity;


public class OverviewViewImpl extends BaseFragment implements OverviewContract.OverviewView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.temp)
    TextView temperature;

    @Inject
    OverviewPresenterImpl presenter;

    private ActionBar actionBar;

    @Override
    protected int provideLayout() {
        return R.layout.fragment_overview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getActivityComponent().inject(this);
        presenter.onAttach(this);
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        //Temporary will be replaced later
        //noinspection ConstantConditions
        actionBar.setTitle("Moscow");
        swipeRefreshLayout.setOnRefreshListener(this);
        presenter.requestCurrentWeather(55.751244, 37.618423, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDetach();
        actionBar = null;
    }

    @Override
    public void onRefresh() {
        presenter.requestCurrentWeather(55.751244, 37.618423, true);
    }

    @Override
    public void displayWeatherData(WeatherResponse weatherResponse) {
        temperature.setText(String.valueOf(weatherResponse.getCurrently().getTemperature()));
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
}
