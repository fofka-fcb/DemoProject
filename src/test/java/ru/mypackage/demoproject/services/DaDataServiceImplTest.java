package ru.mypackage.demoproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mypackage.demoproject.feign.DaDataClient;
import ru.mypackage.demoproject.models.Phone;
import ru.mypackage.demoproject.services.impl.DaDataServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DaDataServiceImplTest {

    @Mock
    private DaDataClient daDataClient;

    @InjectMocks
    private DaDataServiceImpl daDataService;

    String json = "[{\"source\":\"89214285400\"," +
            "\"type\":\"Мобильный\"," +
            "\"phone\":\"+7 921 428-54-00\"," +
            "\"country_code\":\"7\"," +
            "\"city_code\":\"921\"," +
            "\"number\":\"4285400\"," +
            "\"extension\":null," +
            "\"provider\":\"ПАО \\\"МегаФон\\\"\"," +
            "\"country\":\"Россия\"," +
            "\"region\":\"Санкт-Петербург и Ленинградская область\"," +
            "\"city\":null," +
            "\"timezone\":\"UTC+3\"," +
            "\"qc_conflict\":0," +
            "\"qc\":0}]";

    @Test
    void shouldHaveCorrectCheckPhone() {
        String expectedPhoneNumber = "+7 921 428-54-00";
        String expectedType = "Мобильный";
        Integer expectedCityCode = 921;

        when(daDataClient.getString(any(String.class)))
                .thenReturn(json);

        Phone phone = daDataService.checkPhone("89214285400");

        assertAll(
                () -> assertThat(phone.getPhone()).isEqualTo(expectedPhoneNumber),
                () -> assertThat(phone.getType()).isEqualTo(expectedType),
                () -> assertThat(phone.getCity_code()).isEqualTo(expectedCityCode)
        );
    }

}
