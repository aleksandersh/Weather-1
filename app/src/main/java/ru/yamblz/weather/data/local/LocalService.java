package ru.yamblz.weather.data.local;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import ru.yamblz.weather.data.model.response.WeatherResponse;
import ru.yamblz.weather.di.ApplicationContext;

@Deprecated
@Singleton
public class LocalService {

    private final String FILE_NAME = "weather.response";
    private final Gson gson;
    private final File file;

    @Inject
    LocalService(@ApplicationContext Context context) {
        file = new File(context.getCacheDir(), FILE_NAME);
        gson = new Gson();
    }

    public void writeResponseToFile(WeatherResponse weatherResponse) throws IOException {
        removeCache();
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream(file));
        bs.write(gson.toJson(weatherResponse).getBytes());
        bs.flush();
        bs.close();
    }

    public Single<WeatherResponse> readResponseFromFile() {
        return Single.fromCallable(this::readFile);
    }

    private WeatherResponse readFile() throws IOException {
        Type postsResponseType = new TypeToken<WeatherResponse>() {
        }.getType();
        String line;
        StringBuilder jsonString = new StringBuilder();

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while ((line = bufferedReader.readLine()) != null) {
            jsonString.append(line);
        }
        return gson.fromJson(jsonString.toString(), postsResponseType);
    }

    private void removeCache() {
        if (isCacheAvailable()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    private boolean isCacheAvailable() {
        return file.exists();
    }
}
