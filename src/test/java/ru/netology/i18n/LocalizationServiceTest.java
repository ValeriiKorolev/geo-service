package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;

import java.util.stream.Stream;

public class LocalizationServiceTest {
    @ParameterizedTest
    @MethodSource("source")
    void locServTest(Country country, String messageText) {
        LocalizationServiceImpl sut1 = new LocalizationServiceImpl();

        String expected = messageText;
        String preferences = sut1.locale(country);
        Assertions.assertEquals(preferences, expected);
    }

    private static Stream<Arguments> source() {
        return Stream.of(Arguments.of(Country.RUSSIA, "Добро пожаловать"), Arguments.of(Country.USA, "Welcome"), Arguments.of(Country.BRAZIL, "Welcome"));
    }
}
