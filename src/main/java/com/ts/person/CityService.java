package com.ts.person;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.util.Collections;

@Service
public class CityService {
    private final RestTemplate restTemplate;
    private static final String ZIPCODE_LOOKUP_URL = "http://ZiptasticAPI.com/";

    public CityService(RestTemplateBuilder builder) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        this.restTemplate = builder.messageConverters(converter).build();
    }

    public City getCityByZipCode(String zipCode) {
        try {
            return this.restTemplate.getForObject(ZIPCODE_LOOKUP_URL + zipCode, City.class);
        } catch (HttpClientErrorException | HttpServerErrorException | UnknownHttpStatusCodeException e) {
            City blankCity = new City();
            blankCity.setCountry("");
            blankCity.setState("");
            blankCity.setCity("");

            return blankCity;
        }
    }

}
