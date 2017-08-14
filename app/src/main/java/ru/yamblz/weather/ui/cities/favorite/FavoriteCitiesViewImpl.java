package ru.yamblz.weather.ui.cities.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yamblz.weather.R;
import ru.yamblz.weather.data.model.places.City;
import ru.yamblz.weather.ui.base.BaseActivity;
import ru.yamblz.weather.ui.base.BaseFragment;
import ru.yamblz.weather.ui.cities.CitiesActivity;

public class FavoriteCitiesViewImpl extends BaseFragment
        implements FavoriteCitiesContract.CitiesView {
    @BindView(R.id.favorite_cities_recycler_view)
    RecyclerView recyclerView;

    @Inject
    FavoriteCitiesPresenterImpl presenter;

    private CitiesAdapter adapter;

    public static FavoriteCitiesViewImpl newInstance() {
        return new FavoriteCitiesViewImpl();
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_favorite_cities;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

        presenter.onAttach(this);

        setupRecyclerView();

        presenter.onViewCreated();
    }

    @Override
    public void setFavoriteCities(List<City> cities) {
        adapter.setCities(cities);
        adapter.notifyDataSetChanged();
    }

    public void onSelectionSuccessful() {
        ((CitiesActivity) getActivity()).onSelectionSuccessful();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CitiesAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    class CitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.city_text_view)
        TextView cityTextView;
        @BindView(R.id.city_favorite_mark_color)
        ImageView imageView;

        private City city;

        CitiesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            presenter.onCitySelected(city);
            onSelectionSuccessful();
        }

        void bindItem(City city) {
            this.city = city;
            cityTextView.setText(city.getAddress());
            if (city.isFavorite()) imageView.setImageResource(R.color.colorAccent);
            else imageView.setImageResource(0);
        }
    }

    private class CitiesAdapter extends RecyclerView.Adapter<CitiesViewHolder> {
        private List<City> cities = Collections.emptyList();

        @Override
        public CitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new CitiesViewHolder(
                    inflater.inflate(R.layout.cities_recycler_view_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CitiesViewHolder holder, int position) {
            holder.bindItem(cities.get(position));
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }

        public void setCities(List<City> cities) {
            this.cities = cities;
        }
    }
}
