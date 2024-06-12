package ru.mypackage.demoproject.services.impl;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mypackage.demoproject.feign.DaDataClient;
import ru.mypackage.demoproject.models.Phone;
import ru.mypackage.demoproject.services.DaDataService;

@Service
public class DaDataServiceImpl implements DaDataService {

    private final DaDataClient daDataClient;

    @Autowired
    public DaDataServiceImpl(DaDataClient daDataClient) {
        this.daDataClient = daDataClient;
    }

    public Phone checkPhone(String phoneNumber) {
        phoneNumber = "[ " + phoneNumber + " ]";
        String json = daDataClient.getString(phoneNumber);
        json = StringUtils.substring(json, 1, json.length() - 1);

        Phone phone = new Gson().fromJson(json, Phone.class);
        if (phone.getType().equals("Мобильный")) return phone;
        else return null;
    }

}
