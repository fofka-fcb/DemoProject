package ru.mypackage.demoproject.services;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mypackage.demoproject.feign.DaDataClient;
import ru.mypackage.demoproject.models.Phone;

@Service
public class DaDataService {

    private final DaDataClient daDataClient;

    @Autowired
    public DaDataService(DaDataClient daDataClient) {
        this.daDataClient = daDataClient;
    }

    public Phone checkPhone(String phoneNumber) {
        phoneNumber = "[ " + phoneNumber + " ]";
        String json = daDataClient.getString(phoneNumber);
        json = StringUtils.substring(json, 1, json.length() - 1);

        Phone phone = new Gson().fromJson(json, Phone.class);

        return phone;
    }

}
