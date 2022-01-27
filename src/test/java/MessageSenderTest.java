
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSenderImpl;
import ru.netology.entity.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class MessageSenderTest {

    @Test
    void senderTest() {
        GeoServiceImpl geoServiceImpl = Mockito.mock(GeoServiceImpl.class);
        LocalizationServiceImpl localizationServiceImpl = Mockito.mock(LocalizationServiceImpl.class);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");

        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoServiceImpl, localizationServiceImpl);

        String preferences = messageSenderImpl.send(headers);
        String expected = "Добро пожаловать";
        Assertions.assertEquals(preferences, expected);
    }

    @Test
    void locServTest() {
        LocalizationServiceImpl localizationServiceImpl = Mockito.mock(LocalizationServiceImpl.class);
        //Country country = Mockito.mock(Country.class);

        String preferences = localizationServiceImpl.locale(Country.RUSSIA);
        String expected = "Добро пожаловать";
        Assertions.assertEquals(preferences, expected);
    }




}
