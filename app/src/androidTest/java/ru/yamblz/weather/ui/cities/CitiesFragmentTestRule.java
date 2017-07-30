package ru.yamblz.weather.ui.cities;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import ru.yamblz.weather.R;
import ru.yamblz.weather.ui.main.MainActivity;

/**
 * Created by AleksanderSh on 30.07.2017.
 */

public class CitiesFragmentTestRule extends ActivityTestRule<MainActivity> {
    public CitiesFragmentTestRule() {
        super(MainActivity.class, true, true);
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        Fragment fragment = CitiesViewImpl.newInstance();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .commit();
    }
}
