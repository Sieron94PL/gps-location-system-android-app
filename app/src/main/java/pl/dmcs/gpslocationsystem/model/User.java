package pl.dmcs.gpslocationsystem.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Damian on 26.10.2016.
 */
public class User implements Serializable {
    private String username;
    private String password;
    private int enabled;
    private List<Coordinates> coordinates = new ArrayList<Coordinates>();
    private String mail;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User() {
    }

    /**
     * Constructor for register user.
     *
     * @param username
     * @param password
     */
    public User(String username, String password, String mail) {
        this.username = username;
        this.password = password;
        this.enabled = 1;
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public void setCoordinates(List<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }

    public List<Coordinates> getCoordinates() {
        return coordinates;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
