package pl.dmcs.gpslocationsystem.interfaces;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import pl.dmcs.gpslocationsystem.model.User;

/**
 * Created by Damian on 28.12.2016.
 */

public interface HttpMethodsProvider {

    Boolean registerUser(String url, Object request);

    ResponseEntity<Boolean> authenticate(String url, HttpHeaders headers);

    ResponseEntity<Boolean> insertNewSession(String url, HttpHeaders headers, User user);
}


