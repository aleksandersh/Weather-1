package ru.yamblz.weather.ui.cities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.yamblz.weather.R;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.ui.base.BaseActivity;
import ru.yamblz.weather.ui.base.BaseFragment;

/**
 * Created by AleksanderSh on 24.07.2017.
 * <p>
 * Фрагмент с выбором текущего города.
 */

public class CitiesViewImpl extends BaseFragment implements CitiesContract.CitiesView {
    private static final String TAG = "CitiesViewImpl";
    private static final int DELAY_TIME_MILLIS = 300;

    @BindView(R.id.cities_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.cities_search_input)
    EditText mSearchInput;
    @BindViews({R.id.cities_search_layout, R.id.cities_recycler_view})
    List<View> mContentViews;

    private CitiesAdapter mCitiesAdapter;
    private Disposable mSearchDisposable;

    @Inject
    CitiesPresenterImpl mPresenter;

    public static CitiesViewImpl newInstance() {
        return new CitiesViewImpl();
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_cities;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        setTitle(getString(R.string.cities_title));

        setupRecyclerView();
        setupSearchInput();

        mPresenter.onViewCreated();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.onDetach();
        mSearchDisposable.dispose();
    }

    @Override
    public void showPredictions(List<PlacePrediction> predictions) {
        mCitiesAdapter.setPlacePredictions(predictions);
        mCitiesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(@StringRes int stringResId) {
        Toast.makeText(getActivity(), stringResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showContent() {
        ButterKnife.apply(mContentViews, new ButterKnife.Action<View>() {
            @Override
            public void apply(@NonNull View view, int index) {
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideContent() {
        ButterKnife.apply(mContentViews, new ButterKnife.Action<View>() {
            @Override
            public void apply(@NonNull View view, int index) {
                view.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void setCurrentLocation(Location location) {
    }

    @Override
    public void setSearchText(String text) {
        mSearchInput.setText(text);
    }

    @Override
    public void onSelectionSuccessful() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @OnClick(R.id.cities_clear_button)
    public void clearSearchText() {
        mSearchInput.setText("");
    }

    private void setTitle(String title) {
        ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    /**
     * Настройка списка.
     */
    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCitiesAdapter = new CitiesAdapter();
        mRecyclerView.setAdapter(mCitiesAdapter);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    /**
     * Настройка поиска.
     */
    private void setupSearchInput() {
        mSearchDisposable = RxTextView.textChanges(mSearchInput)
                .debounce(DELAY_TIME_MILLIS, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> mPresenter.onSearchTextChanged(charSequence.toString()));
    }

    class CitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private PlacePrediction mPrediction;

        @BindView(R.id.city_text_view)
        TextView mCityTextView;
        @BindView(R.id.city_favorite_mark_color)
        ImageView mImageView;

        CitiesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mPresenter.onPredictionSelected(mPrediction);
        }

        void bindItem(PlacePrediction prediction) {
            mPrediction = prediction;
            mCityTextView.setText(prediction.getText());
            if (prediction.isFavorite()) mImageView.setImageResource(R.color.colorAccent);
        }
    }

    private class CitiesAdapter extends RecyclerView.Adapter<CitiesViewHolder> {
        private List<PlacePrediction> mPlacePredictions = Collections.emptyList();

        @Override
        public CitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new CitiesViewHolder(
                    inflater.inflate(R.layout.cities_recycler_view_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CitiesViewHolder holder, int position) {
            holder.bindItem(mPlacePredictions.get(position));
        }

        @Override
        public int getItemCount() {
            return mPlacePredictions.size();
        }

        void setPlacePredictions(List<PlacePrediction> placePredictions) {
            mPlacePredictions = placePredictions;
        }
    }
}
