package pl.dmcs.gpslocationsystem.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import pl.dmcs.gpslocationsystem.R;
import pl.dmcs.gpslocationsystem.model.Coordinates;
import pl.dmcs.gpslocationsystem.model.User;
import pl.dmcs.gpslocationsystem.interfaces.AuthenticationProvider;
import pl.dmcs.gpslocationsystem.interfaces.HttpMethodsProvider;
import pl.dmcs.gpslocationsystem.providers.AuthenticationProviderImpl;
import pl.dmcs.gpslocationsystem.providers.HttpMethodsProviderImpl;


import org.springframework.http.HttpHeaders;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private Button btnStartSession;
    private Button btnStopSession;
    private TextView numberOfMeasurement;
    private ProgressBar progressBar;

    private final static long ONE_MINUTE = 1000 * 60;
    private final static long TIME_MILISECONDS = ONE_MINUTE / 6;
    private final static float DISTANCE_METERS = 10;

    private static final String URL_INSERT_NEW_SESSION = "http://192.168.2.2:8080/GpsLocationSystem/insertNewSession";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String DISABLE_GPS_MSG = "GPS is switch off.";

    private int counter;
    private User user;
    private HttpMethodsProvider httpMethodsProvider;
    private AuthenticationProvider authenticationProvider;
    private List<Coordinates> coordinates;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        httpMethodsProvider = new HttpMethodsProviderImpl();
        authenticationProvider = new AuthenticationProviderImpl();
        btnStartSession.setEnabled(true);
        btnStopSession.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void initButtons() {
        btnStartSession = (Button) findViewById(R.id.btnStartSession);
        btnStopSession = (Button) findViewById(R.id.btnStopSession);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        numberOfMeasurement = (TextView) findViewById(R.id.numberOfMeasurements);
    }

    protected void startNewSession(View view) throws SecurityException {
        user = new User();
        user.setUsername(getIntent().getStringExtra(USERNAME));
        date = new Date(Calendar.getInstance().getTime().getTime());
        user.setDate(date.toString());
        counter = 0;
        numberOfMeasurement.setText(Integer.toString(counter));
        progressBar.setVisibility(View.VISIBLE);
        btnStartSession.setEnabled(false);
        btnStopSession.setEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                TIME_MILISECONDS, DISTANCE_METERS, this);
        coordinates = new ArrayList<Coordinates>();
    }

    protected void sendData(View view) {
        progressBar.setVisibility(View.INVISIBLE);
        btnStopSession.setEnabled(false);
        btnStartSession.setEnabled(true);
        user.setCoordinates(coordinates);
        new HttpRequestTask().execute();
    }

    @Override
    public void onLocationChanged(Location location) {
        Coordinates coordinate = new Coordinates(location.getLatitude(), location.getLongitude());
        coordinates.add(coordinate);
        counter++;
        numberOfMeasurement.setText(Integer.toString(counter));
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {

        String username = getIntent().getStringExtra(USERNAME);
        String password = getIntent().getStringExtra(PASSWORD);
        HttpHeaders httpHeaders = authenticationProvider.getHeaders(username, password);

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return httpMethodsProvider.insertNewSession(URL_INSERT_NEW_SESSION, httpHeaders, user).getBody();
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), DISABLE_GPS_MSG,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Don't use.
     *
     * @param provider
     * @param status
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    /**
     * Don't use.
     *
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {
    }


}
