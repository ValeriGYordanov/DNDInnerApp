package app.inner.drinkanddrivebrothers.model;

import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by plame_000 on 15-Nov-17.
 */

public class Course implements Serializable{

    private String number;
    private float km;
    private String driver1;
    private String driver2;
    private String hour;
    private double price;


    public Course(){
    }

    public Course(String number, int km, String driver1Names, String driver2Names, String hour, double price){
        this.number = number;
        this.km = km;
        this.driver1 = driver1Names;
        this.driver2 = driver2Names;
        this.hour = hour;
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public float getKm() {
        return km;
    }

    public String getDriver1() {
        return driver1;
    }

    public String getDriver2() {
        return driver2;
    }

    public String getHour(){
        return hour;
    }

    public double getPrice() {
        return price;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setKm(float km) {
        this.km = km;
    }

    public void setDriver1(String driver1) {
        this.driver1 = driver1;
    }

    public void setDriver2(String driver2) {
        this.driver2 = driver2;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
