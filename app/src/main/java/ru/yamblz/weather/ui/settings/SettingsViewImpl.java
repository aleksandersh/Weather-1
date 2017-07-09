package ru.yamblz.weather.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ru.yamblz.weather.R;
import ru.yamblz.weather.ui.base.BaseFragment;
import ru.yamblz.weather.ui.main.MainActivity;


public class SettingsViewImpl extends BaseFragment implements SettingsContract.SettingsView {

    @Override
    protected int provideLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //noinspection ConstantConditions
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_title);
    }
}
