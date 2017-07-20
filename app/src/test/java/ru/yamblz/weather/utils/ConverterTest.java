package ru.yamblz.weather.utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    public void convertToPercentageTest() throws Exception {
        assertEquals(converter.convertToPercentage(0.11), "11");
    }
}
