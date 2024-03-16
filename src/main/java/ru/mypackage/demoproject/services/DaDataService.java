package ru.mypackage.demoproject.services;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mypackage.demoproject.models.Phone;
import ru.mypackage.demoproject.repository.PhoneRepository;

@Service
@RequiredArgsConstructor
public class DaDataService {

    @Value("${myToken}")
    private String myToken;

    @Value("${secretToken}")
    private String secretToken;
    private final String url = "https://cleaner.dadata.ru/api/v1/clean/phone";

    private final PhoneRepository phoneRepository;

    public Phone checkPhone(String phoneNumber) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Token " + myToken);
        httpHeaders.add("X-Secret", secretToken);

        String number = "[ " + phoneNumber + " ]";
        HttpEntity<String> request = new HttpEntity<>(number, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.postForObject(url, request, String.class);
        json = StringUtils.substring(json, 1, json.length() - 1);


        Phone phone = new Gson().fromJson(json, Phone.class);

        if (phone.getType().equals("Мобильный")) {
            return phone;
        } else {
            return null;
        }
    }

}
