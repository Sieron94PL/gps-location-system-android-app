package pl.dmcs.gpslocationsystem.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;

import pl.dmcs.gpslocationsystem.R;
import pl.dmcs.gpslocationsystem.interfaces.AuthenticationProvider;
import pl.dmcs.gpslocationsystem.interfaces.HttpMethodsProvider;
import pl.dmcs.gpslocationsystem.providers.AuthenticationProviderImpl;
import pl.dmcs.gpslocationsystem.providers.HttpMethodsProviderImpl;


/**
 * Created by Damian on 05.11.2016.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private final static String URL_AUTHENTICATION = "http://192.168.2.2:8080/GpsLocationSystem/authentication";
    private final static String SUCCESSFUL_LOG_IN_MSG = "You have successfully logged in.";
    private final static String INVALID_LOG_IN_MSG = "The username or password is incorrect.";
    private final static String NO_INTERNET_ACCESS_MSG = "No Internet connection.";
    private final static String USERNAME = "USERNAME";
    private final static String PASSWORD = "PASSWORD";

    private Button btnSignIn;
    private Button btnRegistration;
    private EditText loginUsername;
    private EditText loginPassword;
    private Intent intent;

    private HttpMethodsProvider httpMethodsProvider;
    private AuthenticationProvider authenticationProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initButtons();
        initEditTexts();
        setClickListeners();
        authenticationProvider = new AuthenticationProviderImpl();
        httpMethodsProvider = new HttpMethodsProviderImpl();
    }

    protected void initEditTexts() {
        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
    }

    private void initButtons() {
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnRegistration = (Button) findViewById(R.id.btnRegistration);
    }

    private void setClickListeners() {
        btnSignIn.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
    }

    protected boolean isInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {

        private String username = loginUsername.getText().toString();
        private String password = loginPassword.getText().toString();
        private HttpHeaders headers = authenticationProvider.getHeaders(username, password);

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return httpMethodsProvider.authenticate(URL_AUTHENTICATION, headers).getBody();
            } catch (HttpClientErrorException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isAuthenticated) {
            if (isAuthenticated) {
                Toast.makeText(getApplicationContext(), SUCCESSFUL_LOG_IN_MSG, Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(USERNAME, username);
                intent.putExtra(PASSWORD, password);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), INVALID_LOG_IN_MSG, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                signIn();
                break;
            case R.id.btnRegistration:
                registerUser();
                break;
        }
    }

    private void signIn() {
        if (isInternetConnection())
            new HttpRequestTask().execute();
        else
            Toast.makeText(getApplicationContext(), NO_INTERNET_ACCESS_MSG, Toast.LENGTH_LONG).show();
    }

    private void registerUser() {
        intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
    }
}
