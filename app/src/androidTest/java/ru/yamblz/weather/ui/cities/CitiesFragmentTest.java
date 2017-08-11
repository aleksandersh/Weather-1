package ru.yamblz.weather.ui.cities;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.yamblz.weather.R;
import ru.yamblz.weather.data.local.AppPreferenceManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by AleksanderSh on 30.07.2017.
 */

@RunWith(AndroidJUnit4.class)
public class CitiesFragmentTest {
    String currentCity = "";

    @Rule
    public CitiesFragmentTestRule mFragmentTestRule = new CitiesFragmentTestRule();

    @Before
    public void setUp() {
        AppPreferenceManager preferenceManager = new AppPreferenceManager();
//        currentCity = preferenceManager.getLocation().getTitle();
    }

    @Test
    public void initialDataTest() {
        onView(withId(R.id.cities_search_input)).check(matches(withText(currentCity)));
    }

    @Test
    public void clearButtonTest() {
        onView(withId(R.id.cities_clear_button)).perform(click());
        onView(withId(R.id.cities_search_input)).check(matches(withText("")));
    }
}
