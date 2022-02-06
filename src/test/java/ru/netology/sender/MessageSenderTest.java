package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MessageSenderTest {
    @ParameterizedTest
    @MethodSource("source")
    void senderTest(String adresIP, String messageText) {
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, adresIP);

        GeoServiceImpl geoServiceImpl = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoServiceImpl.byIp(Mockito.any()))
                .thenReturn(new Location(null, Country.USA, null, 0));
        Mockito.when(geoServiceImpl.byIp(Mockito.startsWith("172.")))
                .thenReturn(new Location(null, Country.RUSSIA, null, 0));

        LocalizationServiceImpl localizationServiceImpl = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationServiceImpl.locale(Mockito.any()))
                .thenReturn("Welcome");
        Mockito.when(localizationServiceImpl.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");


        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoServiceImpl, localizationServiceImpl);

        String preferences = messageSenderImpl.send(headers);
        String expected = messageText;
        Assertions.assertEquals(preferences, expected);
    }

    private static Stream<Arguments> source() {
        return Stream.of(Arguments.of("172.123.12.19", "Добро пожаловать"), Arguments.of("96.44.183.149", "Добро пожаловать"),
                Arguments.of("66.41.182.143", "Welcome"));
    }
}
