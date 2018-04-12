package pl.dmcs.gpslocationsystem.providers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import pl.dmcs.gpslocationsystem.model.User;
import pl.dmcs.gpslocationsystem.interfaces.HttpMethodsProvider;

/**
 * Created by Damian on 28.12.2016.
 */

public class HttpMethodsProviderImpl implements HttpMethodsProvider {

    private RestTemplate restTemplate;

    public HttpMethodsProviderImpl() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    public Boolean registerUser(String url, Object request) {
        return restTemplate.postForObject(url, request, Boolean.class);
    }

    @Override
    public ResponseEntity<Boolean> authenticate(String url, HttpHeaders headers) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), Boolean.class);
    }

    @Override
    public ResponseEntity<Boolean> insertNewSession(String url, HttpHeaders headers, User user) {
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Object>(user, headers), Boolean.class);
    }


}
