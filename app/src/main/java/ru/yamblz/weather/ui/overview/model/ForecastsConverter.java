package ru.yamblz.weather.ui.overview.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ru.yamblz.weather.data.model.converter.DtoToModelConverter;
import ru.yamblz.weather.data.model.weather.Forecast;
import ru.yamblz.weather.utils.Converter;

/**
 * Created by AleksanderSh on 12.08.2017.
 * <p>
 * Конвертер листа прогнозов приложения в лист для вывода.
 */

public class ForecastsConverter implements DtoToModelConverter<List<Forecast>, List<DailyForecast>> {
    private static final String TAG = "ForecastsConverter";
    private static final int NIGHT = 3;
    private static final int NIGHT_START = 0;
    private static final int NIGHT_END = 6;
    private static final int DAY = 15;
    private static final int DAY_START = 12;
    private static final int DAY_END = 18;

    private final Converter temperatureConverter;

    @Inject
    public ForecastsConverter(Converter temperatureConverter) {
        this.temperatureConverter = temperatureConverter;
    }

    @Override
    public List<DailyForecast> convert(List<Forecast> forecasts) {
        // TODO: 13.08.2017 Переделать, если без RecyclerView

        int daysCount = forecasts.size() / 24;
        if (daysCount == 0) {
            throw new IllegalArgumentException("Not enough forecasts.");
        }

        Collections.sort(forecasts, (f1, f2) -> f1.getTime().compareTo(f2.getTime()));

        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());

        calendar.setTime(new Date());
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int startIdx = -1;
        int startedHour = 0;
        boolean skipCurrentDay = false;

        if (currentHour >= NIGHT_END) {
            skipCurrentDay = true;
        }

        for (int i = 0; i < forecasts.size(); i++) {
            Date date = new Date(TimeUnit.MILLISECONDS
                    .convert(forecasts.get(i).getTime(), TimeUnit.SECONDS));
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            if ((!skipCurrentDay && day == currentDay) || day > currentDay) {
                startIdx = i;
                startedHour = calendar.get(Calendar.HOUR_OF_DAY);
                break;
            }
        }

        if (startIdx < 0) {
            Log.d(TAG, "displayForecasts: forecasts are stale.");
            return Collections.emptyList();
        }

        List<DailyForecast> dailyForecasts = new ArrayList<>(daysCount);

        int minLimit = DAY_END - startedHour;
        for (int dayNumber = 0; startIdx + minLimit + dayNumber * 24 < forecasts.size(); dayNumber++) {
            // Смещение индекса для текущего дня.
            int dayIdx = startIdx + dayNumber * 24 - startedHour;

            int evalDayIdx = dayIdx + DAY;
            int evalNightIdx = dayIdx + NIGHT;
            if (evalNightIdx < 0) evalNightIdx = 0;

            Forecast dayForecast = forecasts.get(evalDayIdx);
            Forecast nightForecast = forecasts.get(evalNightIdx);

            Date date = new Date(TimeUnit.MILLISECONDS.convert(dayForecast.getTime(), TimeUnit.SECONDS));

            String iconDay = dayForecast.getIcon();
            String iconNight = nightForecast.getIcon();

            int nightStartIdx = dayIdx + NIGHT_START;
            int nightEndIdx = dayIdx + NIGHT_END;
            if (nightStartIdx < 0) nightStartIdx = 0;
            int tempSum = 0;
            for (int i = nightStartIdx; i < nightEndIdx; i++) {
                Forecast forecastHourly = forecasts.get(i);
                tempSum += forecastHourly.getTemperature();
            }
            String tempNight = temperatureConverter
                    .convertTemperature(tempSum / (nightEndIdx - nightStartIdx));

            int dayStartIdx = dayIdx + DAY_START;
            int dayEndIdx = dayIdx + DAY_END;
            tempSum = 0;
            for (int i = dayStartIdx; i < dayEndIdx; i++) {
                Forecast forecastHourly = forecasts.get(i);
                tempSum += forecastHourly.getTemperature();
            }
            String tempDay = temperatureConverter
                    .convertTemperature(tempSum / (dayEndIdx - dayStartIdx));

            dailyForecasts.add(new DailyForecast(date, tempDay, tempNight, iconDay));
        }

        return dailyForecasts;
    }
}
