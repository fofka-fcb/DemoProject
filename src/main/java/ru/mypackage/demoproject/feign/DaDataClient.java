package ru.mypackage.demoproject.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.mypackage.demoproject.configuration.feign.FeignClientConfiguration;

@FeignClient(value = "self",
        url = "https://cleaner.dadata.ru/api/v1/clean/phone",
        configuration = FeignClientConfiguration.class)
public interface DaDataClient {

    @RequestMapping(method = RequestMethod.POST)
    String getString(String phoneNumber);

}
