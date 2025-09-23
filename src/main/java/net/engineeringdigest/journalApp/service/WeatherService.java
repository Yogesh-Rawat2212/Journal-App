package net.engineeringdigest.journalApp.service;

import jakarta.annotation.PostConstruct;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    //spring static variable ko chedta hi nhi h
    //static variable class s related h na ki instance s related
    @Value("${weather.api.key}")
    private String apiKey;

    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void init(){
        if(apiKey == null || apiKey.isBlank()){
            throw new IllegalStateException("Weather API key is missing!");
        }
        baseUrl = "http://api.weatherstack.com/current?access_key=" + apiKey + "&query=";
    }

    public WeatherResponse getWeather(String city) {
        String finalAPI = baseUrl + city;
        return restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class).getBody();
    }
    // our endpoint,http verb, pojo ki class

}
