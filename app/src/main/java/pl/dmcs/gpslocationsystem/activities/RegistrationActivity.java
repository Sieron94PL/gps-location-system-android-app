package pl.dmcs.gpslocationsystem.activities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import pl.dmcs.gpslocationsystem.R;
import pl.dmcs.gpslocationsystem.model.User;
import pl.dmcs.gpslocationsystem.providers.HttpMethodsProviderImpl;

/**
 * Created by Damian on 05.11.2016.
 */
public class RegistrationActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {

    private Button btnConfirm;
    private EditText registrationUsername;
    private EditText registrationPassword;
    private EditText registrationEmail;

    private final static String URL_REGISTRATION = "http://192.168.2.2:8080/GpsLocationSystem/registerUser";
    private final static String REGISTERED_SUCCESSFULLY_MSG = "Registered successfully.";
    private final static String REGISTERED_UNSUCCESSFULLY_MSG = "There is already a user.";
    private final static String VALIDATION_ERROR_MSG = "Registration failed.";
    private final static String INVALID_EMAIL_MSG = "Invalid e-mail.";
    private final static String INVALID_USERNAME_LENGTH_MSG = "Your username must be at least 6 characters in length.";
    private final static String INVALID_PASSWORD_LENGTH_MSG = "Your password must be at least 6 characters in length.";
    private final static String NO_INTERNET_ACCESS_MSG = "No Internet connection.";

    private HttpMethodsProviderImpl httpMethodsProvider;

    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        httpMethodsProvider = new HttpMethodsProviderImpl();
        initTextEdits();
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
        registrationEmail.setOnFocusChangeListener(this);
    }

    private void initTextEdits() {
        registrationUsername = (EditText) findViewById(R.id.registrationUsername);
        registrationPassword = (EditText) findViewById(R.id.registrationPassword);
        registrationEmail = (EditText) findViewById(R.id.registrationEmail);
    }

    private void confirmRegistration() {
        new HttpRequestTask().execute();
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm:
                if (validate)
                    confirmRegistration();
                else Toast.makeText(getBaseContext(), VALIDATION_ERROR_MSG,
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        int usernameLength = registrationUsername.getText().toString().length();
        int passwordLength = registrationUsername.getText().toString().length();
        String email = registrationEmail.getText().toString();


        if (!isValidEmail(email)) {
            registrationEmail.setError(INVALID_EMAIL_MSG);
            validate = false;
        }

        if (usernameLength < 6) {
            registrationUsername.setError(INVALID_USERNAME_LENGTH_MSG);
            validate = false;
        }

        if (passwordLength < 6) {
            registrationPassword.setError(INVALID_PASSWORD_LENGTH_MSG);
            validate = false;
        }

        if (isValidEmail(email) && usernameLength >= 6 && passwordLength >= 6)
            validate = true;
    }

    private boolean isInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return httpMethodsProvider.registerUser(URL_REGISTRATION, getUser());
            } catch (Exception e) {
                return false;
            }
        }

        protected User getUser() {
            String username = registrationUsername.getText().toString();
            String password = registrationPassword.getText().toString();
            String mail = registrationEmail.getText().toString();
            User user = new User(username, password, mail);
            return user;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            if (isInternetConnection()) {
                if (response)
                    Toast.makeText(getBaseContext(), REGISTERED_SUCCESSFULLY_MSG,
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(), REGISTERED_UNSUCCESSFULLY_MSG,
                            Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getBaseContext(), NO_INTERNET_ACCESS_MSG,
                        Toast.LENGTH_SHORT).show();

        }

    }

}
