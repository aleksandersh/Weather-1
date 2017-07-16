package ru.yamblz.weather.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.yarolegovich.mp.MaterialChoicePreference;
import com.yarolegovich.mp.MaterialSwitchPreference;

import butterknife.BindView;
import ru.yamblz.weather.R;
import ru.yamblz.weather.ui.base.BaseFragment;
import ru.yamblz.weather.ui.main.MainActivity;
import ru.yamblz.weather.utils.UpdateTaskService;


public class SettingsViewImpl extends BaseFragment implements SettingsContract.SettingsView {

    @BindView(R.id.updateSwitchPreference)
    MaterialSwitchPreference updateSwitchPreference;

    @BindView(R.id.updateIntervalPreference)
    MaterialChoicePreference updateChoicePreference;

    private GcmNetworkManager gcmNetworkManager;

    @Override
    protected int provideLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //noinspection ConstantConditions
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_title);
        setChoicePreferenceStatus(updateSwitchPreference.getValue());
        gcmNetworkManager = GcmNetworkManager.getInstance(getContext());

        updateSwitchPreference.setOnClickListener(view1 -> {
            if (updateSwitchPreference.getValue()) {
                UpdateTaskService.startUpdateTask(gcmNetworkManager, updateChoicePreference.getValue());
            } else {
                UpdateTaskService.stopUpdateTask(gcmNetworkManager);
            }
            setChoicePreferenceStatus(updateSwitchPreference.getValue());
        });
    }

    private void setChoicePreferenceStatus(boolean updateSwitchValue) {
        if (updateSwitchValue) {
            updateChoicePreference.setEnabled(false);
        } else {
            updateChoicePreference.setEnabled(true);
        }
    }
}
