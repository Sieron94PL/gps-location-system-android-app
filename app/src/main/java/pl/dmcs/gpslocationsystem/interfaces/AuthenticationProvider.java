package pl.dmcs.gpslocationsystem.interfaces;

import org.springframework.http.HttpHeaders;

/**
 * Created by Damian on 28.12.2016.
 */

public interface AuthenticationProvider {

    HttpHeaders getHeaders(String username, String password);
}
