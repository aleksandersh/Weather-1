package ru.yamblz.weather.utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ru.yamblz.weather.R;
import ru.yamblz.weather.data.local.AppPreferenceManager;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;


public class ConverterTest {

    @Mock
    AppPreferenceManager preferenceManager;

    private Converter converter;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        converter = new Converter(preferenceManager);
    }

    @Test
    public void convertTemperatureFahrenheit() throws Exception {
        when(preferenceManager.getCurrentUnits()).thenReturn("F");
        assertEquals(converter.convertTemperature(66.6), "67");
    }

    @Test
    public void convertTemperatureCelsius() throws Exception {
        when(preferenceManager.getCurrentUnits()).thenReturn("C");
        assertEquals(converter.convertTemperature(22.4), "-5");
    }

    @Test
    public void convertTemperatureFahrenheitZero() {
        when(preferenceManager.getCurrentUnits()).thenReturn("F");
        assertEquals(converter.convertTemperature(0), "0");
    }

    @Test
    public void convertTemperatureCelsiusZero() {
        when(preferenceManager.getCurrentUnits()).thenReturn("C");
        assertEquals(converter.convertTemperature(0), "-18");
    }

    @Test
    public void convertTemperatureFahrenheitMinus() {
        when(preferenceManager.getCurrentUnits()).thenReturn("F");
        assertEquals(converter.convertTemperature(-10.5), "-10");
    }

    @Test
    public void convertTemperatureCelsiusMinus() {
        when(preferenceManager.getCurrentUnits()).thenReturn("C");
        assertEquals(converter.convertTemperature(-10.5), "-24");
    }

    @Test
    public void convertToPercentageTest() throws Exception {
        assertEquals(converter.convertToPercentage(0.11), "11");
    }

    @Test
    public void convertToPercentageTestZero() {
        assertEquals(converter.convertToPercentage(0), "0");
    }

    @Test
    public void convertToPercentageTestMinus() {
        assertEquals(converter.convertToPercentage(-0.015), "-1");
    }

    @Test
    public void convertIconToResCorrectString() {
        assertEquals(converter.convertIconToRes("snow"), R.drawable.ic_snow);
    }

    @Test
    public void convertIconToResIncorrectString() {
        assertEquals(converter.convertIconToRes("some-incorrect-string"), R.drawable.ic_cloudy);
    }

    @Test(expected = NullPointerException.class)
    public void convertIconToResNullString() {
        converter.convertIconToRes(null);
    }
}
