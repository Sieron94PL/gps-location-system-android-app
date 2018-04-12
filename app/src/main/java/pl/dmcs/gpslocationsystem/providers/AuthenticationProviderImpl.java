package pl.dmcs.gpslocationsystem.providers;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import pl.dmcs.gpslocationsystem.interfaces.AuthenticationProvider;

/**
 * Created by Damian on 28.12.2016.
 */

public class AuthenticationProviderImpl implements AuthenticationProvider {
    @Override
    public HttpHeaders getHeaders(String username, String password) {
        String plainCredentials = username + ":" + password;
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        return headers;
    }
}
