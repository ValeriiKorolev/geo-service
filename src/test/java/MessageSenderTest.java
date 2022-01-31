
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MessageSenderTest {

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
    @MethodSource("source2")
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

    private static Stream<Arguments> source2() {
        return Stream.of(Arguments.of("172.123.12.19", "Добро пожаловать"), Arguments.of("96.44.183.149", "Добро пожаловать"),
                Arguments.of("66.41.182.143", "Welcome"));
    }


    @ParameterizedTest
    @MethodSource("source1")
    void locServTest(Country country, String messageText) {
        LocalizationServiceImpl sut1 = new LocalizationServiceImpl();

        String expected = messageText;
        String preferences = sut1.locale(country);
        Assertions.assertEquals(preferences, expected);
    }

    private static Stream<Arguments> source1() {
        return Stream.of(Arguments.of(Country.RUSSIA, "Добро пожаловать"), Arguments.of(Country.USA, "Welcome"), Arguments.of(Country.BRAZIL, "Welcome"));
    }

    @ParameterizedTest
    @MethodSource("source3")
    void geoServTestCoord(Double latitude, Double longitude) {
        GeoService sut = new GeoServiceImpl();
        Assertions.assertThrows(RuntimeException.class,
                () -> sut.byCoordinates(latitude, longitude));
    }

    private static Stream<Arguments> source3() {
        return Stream.of(Arguments.of(18.1, 33.6), Arguments.of(56.2, 45.7));

    }

}
