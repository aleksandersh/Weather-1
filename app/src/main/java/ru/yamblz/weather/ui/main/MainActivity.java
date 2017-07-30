package ru.yamblz.weather.ui.main;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;

import com.google.android.gms.gcm.GcmNetworkManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import ru.yamblz.weather.R;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.ui.about.AboutViewImpl;
import ru.yamblz.weather.ui.base.BaseActivity;
import ru.yamblz.weather.ui.overview.OverviewViewImpl;
import ru.yamblz.weather.ui.settings.SettingsViewImpl;
import ru.yamblz.weather.utils.UpdateTaskService;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Inject
    AppPreferenceManager preferenceManager;

    @State
    int fragmentId;

    private DrawerArrowDrawable homeDrawable;
    private boolean isHomeAsUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        homeDrawable = new DrawerArrowDrawable(toolbar.getContext());
        toolbar.setNavigationIcon(homeDrawable);

        toolbar.setNavigationOnClickListener(view -> {
            if (drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            } else if (isHomeAsUp){
                onBackPressed();
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            checkFirstTimeUser();
            fragmentId = R.id.nav_overview;
            selectItem(fragmentId);
            navigationView.setCheckedItem(fragmentId);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            setHomeAsUp(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id != fragmentId) {
            fragmentId = id;
            selectItem(id);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            fragmentId = R.id.nav_overview;
            setHomeAsUp(false);
        } else {
            setHomeAsUp(true);
        }
    }

    private void selectItem(int id) {
        switch (id) {
            case R.id.nav_overview:
                replaceFragment(R.id.contentFrame, new OverviewViewImpl(), false);
                break;
            case R.id.nav_settings:
                replaceFragment(R.id.contentFrame, new SettingsViewImpl(), true);
                break;
            case R.id.nav_about:
                replaceFragment(R.id.contentFrame, new AboutViewImpl(), true);
                break;
            default:
                replaceFragment(R.id.contentFrame, new OverviewViewImpl(), false);
                break;
        }
    }

    private void checkFirstTimeUser() {
        if (preferenceManager.getFirstTimeUser()) {
            GcmNetworkManager gcmNetworkManager = GcmNetworkManager.getInstance(getApplicationContext());
            UpdateTaskService.startUpdateTask(gcmNetworkManager, preferenceManager.getCurrentUpdateInterval());
            preferenceManager.setFirstTimeUser(false);
        }
    }

    private void setHomeAsUp(boolean isHomeAsUp) {
        if (this.isHomeAsUp != isHomeAsUp) {
            this.isHomeAsUp = isHomeAsUp;
            int lockMode = isHomeAsUp ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED;
            drawer.setDrawerLockMode(lockMode);
            ValueAnimator anim = isHomeAsUp ? ValueAnimator.ofFloat(0, 1) : ValueAnimator.ofFloat(1, 0);
            anim.addUpdateListener(valueAnimator -> {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                homeDrawable.setProgress(slideOffset);
            });
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(400);
            anim.start();
        }
    }
}