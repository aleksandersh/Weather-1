package ru.yamblz.weather.utils;

import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.TaskParams;

import javax.inject.Inject;

import ru.yamblz.weather.R;
import ru.yamblz.weather.data.database.WeatherDao;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.network.WeatherApiClient;
import ru.yamblz.weather.di.component.DaggerBackgroundComponent;
import ru.yamblz.weather.di.module.AppModule;


public class UpdateTaskService extends GcmTaskService {

    public static final String TAG = "update_task";
    private static final String TAG_LOG = "UpdateTaskService";

    @Inject
    AppPreferenceManager preferenceManager;
    @Inject
    WeatherDao dao;
    @Inject
    WeatherApiClient apiClient;

    private int result;

    @Override
    public int onRunTask(TaskParams taskParams) {
        inject();
        result = GcmNetworkManager.RESULT_SUCCESS;
        Log.d(TAG, "taskIsRunning");

        Location location = preferenceManager.getLocation();
        String lang = getApplicationContext().getString(R.string.api_language_value);
        apiClient.getWeather(location.getLatitude(), location.getLongitude(), lang)
                .doOnSuccess(dao::saveWeather)
                .subscribe(
                        weather -> result = GcmNetworkManager.RESULT_SUCCESS,
                        err -> {
                            result = GcmNetworkManager.RESULT_FAILURE;
                            Log.d(TAG_LOG, err.getMessage());
                        });

        return result;
    }

    public static void startUpdateTask(GcmNetworkManager gcmNetworkManager, String period) {
        Log.d(TAG_LOG, "startUpdateTask");
        long interval = Long.valueOf(period);

        PeriodicTask task = new PeriodicTask.Builder()
                .setService(UpdateTaskService.class)
                .setTag(TAG)
                .setPersisted(true)
                .setPeriod(interval)
                .setRequiredNetwork(PeriodicTask.NETWORK_STATE_CONNECTED)
                .build();

        gcmNetworkManager.schedule(task);
    }

    public static void stopUpdateTask(GcmNetworkManager gcmNetworkManager) {
        Log.d(TAG_LOG, "stopUpdateTask");

        gcmNetworkManager.cancelTask(TAG, UpdateTaskService.class);
    }

    private void inject() {
        DaggerBackgroundComponent.builder()
                .appModule(new AppModule(getApplication()))
                .build()
                .inject(this);
    }
}
