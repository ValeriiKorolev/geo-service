package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;

import java.util.stream.Stream;

public class GeoServiceTest {

    @ParameterizedTest
    @MethodSource("source")
    void geoServTestIp(String adresIP, Country country) {
        GeoServiceImpl sut = new GeoServiceImpl();

        Country expected = country;
        Country preferences = sut.byIp(adresIP).getCountry();
        Assertions.assertEquals(preferences, expected);
    }

    private static Stream<Arguments> source() {
        return Stream.of(Arguments.of("172.123.12.19", Country.RUSSIA), Arguments.of("96.44.183.149", Country.USA),
                Arguments.of("96.41.182.143", Country.RUSSIA));
    }

    @ParameterizedTest
    @MethodSource("source1")
    void geoServTestCoord(Double latitude, Double longitude) {
        GeoService sut = new GeoServiceImpl();
        Assertions.assertThrows(RuntimeException.class,
                () -> sut.byCoordinates(latitude, longitude));
    }

    private static Stream<Arguments> source1() {
        return Stream.of(Arguments.of(18.1, 33.6), Arguments.of(56.2, 45.7));

    }
}
