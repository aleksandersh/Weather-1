package ru.yamblz.weather.ui.cities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yamblz.weather.R;
import ru.yamblz.weather.ui.base.BaseActivity;
import ru.yamblz.weather.ui.cities.favorite.FavoriteCitiesViewImpl;

public class CitiesActivity extends BaseActivity implements CitiesContract.CitiesActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cities);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSelectionSuccessful() {
        finish();
    }

    private boolean setupViewPager() {
        viewPager = findViewById(R.id.view_pager);
        if (viewPager != null) {
            CitiesPagerAdapter adapter = new CitiesPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);

            TabLayout tabLayout = findViewById(R.id.tab_layout);
            if (tabLayout != null) {
                tabLayout.setupWithViewPager(viewPager);
            }

            return true;
        }

        return false;
    }

    private class CitiesPagerAdapter extends FragmentPagerAdapter {
        private static final int CITIES_VIEW = 0;
        private static final int FAVORITE_CITIES_VIEW = 1;

        public CitiesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == CITIES_VIEW) {
                return CitiesViewImpl.newInstance();
            } else if (position == FAVORITE_CITIES_VIEW) {
                return FavoriteCitiesViewImpl.newInstance();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == CITIES_VIEW) {
                return getString(R.string.cities_search_title);
            } else if (position == FAVORITE_CITIES_VIEW) {
                return getString(R.string.cities_favorite_title);
            }

            return super.getPageTitle(position);
        }
    }
}
