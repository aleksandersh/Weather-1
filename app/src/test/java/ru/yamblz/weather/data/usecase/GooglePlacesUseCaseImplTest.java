package ru.yamblz.weather.data.usecase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import ru.yamblz.weather.data.local.AppPreferenceManager;
import ru.yamblz.weather.data.model.googlePlacesResponse.details.GeometryDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.details.LocationDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.details.PlaceDetailsDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.details.PlaceDetailsResponseDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.MatchedSubstringDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.PredictionDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.PredictionsResponseDto;
import ru.yamblz.weather.data.model.googlePlacesResponse.prediction.TermDto;
import ru.yamblz.weather.data.model.places.Location;
import ru.yamblz.weather.data.model.places.PlacePrediction;
import ru.yamblz.weather.data.network.GooglePlacesApi;
import ru.yamblz.weather.data.usecase.places.CitiesUseCase;
import ru.yamblz.weather.data.usecase.places.GooglePlacesException;
import ru.yamblz.weather.data.usecase.places.GooglePlacesUseCaseImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by AleksanderSh on 30.07.2017.
 */
public class GooglePlacesUseCaseImplTest {
    @Mock
    GooglePlacesApi mApi;
    @Mock
    AppPreferenceManager mPreferenceManager;

    private CitiesUseCase mUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mUseCase = new GooglePlacesUseCaseImpl(mApi, mPreferenceManager);
    }

    /**
     * Тестирование успешного выполнения запроса и конвертации объекта передачи данных.
     */
    @Test
    public void loadPlacePredictionsCorrect() {
        final String name = "Moscow";
        final String description = "Moscow Russia";
        final String id = "11";
        final PredictionsResponseDto dto = buildCorrectPredictionsResponseDto(name, description, id);

        final TestObserver<List<PlacePrediction>> testObserver = TestObserver.create();
        final Single<PredictionsResponseDto> single = Single.just(dto);

        when(mApi.getPredictions(anyString(), anyString(), anyString())).thenReturn(single);

        mUseCase.loadPlacePredictions("some_string").subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        assertEquals(testObserver.valueCount(), 1);

        List<PlacePrediction> predictions = testObserver.values().get(0);
        assertEquals(predictions.size(), 2);

        PlacePrediction prediction = predictions.get(0);
        assertEquals(prediction.getId(), id);
        assertEquals(prediction.getName(), name);
        assertEquals(prediction.getText(), description);
    }

    /**
     * Тестирование случая некорректного запроса.
     */
    @Test
    public void loadPlacePredictionsIncorrect() {
        final PredictionsResponseDto dto =
                new PredictionsResponseDto("REQUEST_DENIED", Collections.emptyList());
        final TestObserver<List<PlacePrediction>> testObserver = TestObserver.create();
        final Single<PredictionsResponseDto> single = Single.just(dto);

        when(mApi.getPredictions(anyString(), anyString(), anyString())).thenReturn(single);

        mUseCase.loadPlacePredictions("some_string").subscribe(testObserver);

        testObserver.assertError(GooglePlacesException.class);
    }

    @Test
    public void setCurrentLocationByPredictionCorrect() {
        final Location location = new Location("Moscow", 25, 35);
        final PlaceDetailsResponseDto dto = buildCorrectPlaceDetailsResponseDto(
                location.getLatitude(), location.getLongitude());

        final TestObserver<List<PlacePrediction>> testObserver = TestObserver.create();
        final Single<PlaceDetailsResponseDto> single = Single.just(dto);
        final ArgumentCaptor<Location> argumentCaptor = ArgumentCaptor.forClass(Location.class);

        when(mApi.getPlaceDetails(anyString(), anyString())).thenReturn(single);

        mUseCase.setCurrentLocationByPrediction(
                new PlacePrediction("some_id", location.getTitle(), "some_text"))
                .subscribe(testObserver);

        testObserver.assertNoErrors();
        testObserver.assertComplete();

        verify(mPreferenceManager).setCurrentLocation(argumentCaptor.capture());
        assertEquals(location, argumentCaptor.getValue());
    }

    @Test
    public void setCurrentLocationByPredictionIncorrect() {
        final PlaceDetailsResponseDto dto = new PlaceDetailsResponseDto(null, "NOT_FOUND");

        final TestObserver<List<PlacePrediction>> testObserver = TestObserver.create();
        final Single<PlaceDetailsResponseDto> single = Single.just(dto);

        when(mApi.getPlaceDetails(anyString(), anyString())).thenReturn(single);

        mUseCase.setCurrentLocationByPrediction(new PlacePrediction("some_id", "some_name", "some_text"))
                .subscribe(testObserver);

        testObserver.assertError(GooglePlacesException.class);
    }

    private PredictionsResponseDto buildCorrectPredictionsResponseDto(String name, String description, String id) {
        List<MatchedSubstringDto> matchedSubstringDtoList = new ArrayList<>(1);
        matchedSubstringDtoList.add(new MatchedSubstringDto(name.length(), 6));

        List<TermDto> termDtoList = new ArrayList<>(2);
        termDtoList.add(new TermDto(0, "Russia"));
        termDtoList.add(new TermDto(6, name));

        List<PredictionDto> predictionDtoList = new ArrayList<>(2);

        // Первый элемент проверочный.
        predictionDtoList.add(new PredictionDto(description, "1", id, "1",
                matchedSubstringDtoList, termDtoList, Collections.emptyList()));

        // Второй элемент - заглушка для количества.
        predictionDtoList.add(new PredictionDto("", "", "", "", null, null, null));

        return new PredictionsResponseDto("OK", predictionDtoList);
    }

    private PlaceDetailsResponseDto buildCorrectPlaceDetailsResponseDto(double lat, double lng) {
        return new PlaceDetailsResponseDto(
                new PlaceDetailsDto(
                        new GeometryDto(
                                new LocationDto(lat, lng)
                        ), "some_name", "some_id", "some_reference"
                ), "OK"
        );
    }
}