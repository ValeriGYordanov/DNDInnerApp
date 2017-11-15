package app.inner.drinkanddrivebrothers.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by plame_000 on 15-Nov-17.
 */

public class Course implements Serializable{

    private String number;
    private float km;
    private String driver1;
    private String driver2;

    public Course(){

    }

    public Course(String number, float km, String driver1Names, String driver2Names){
        this.number = number;
        this.km = km;
        this.driver1 = driver1Names;
        this.driver2 = driver2Names;
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
}
