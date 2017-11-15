package app.inner.drinkanddrivebrothers.model;

import java.io.Serializable;

/**
 * Created by plame_000 on 15-Nov-17.
 */

public class User implements Serializable {

    private String name;
    private String lastName;
    private boolean isChecked = false;

    public User(String name, String lastName){
        this.name = name;
        this.lastName = lastName;
    }

    public User (){

    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
