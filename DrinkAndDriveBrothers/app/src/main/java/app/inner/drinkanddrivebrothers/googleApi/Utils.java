package app.inner.drinkanddrivebrothers.googleApi;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by plame_000 on 11-Nov-17.
 */

public final class Utils {
    private Utils(){}

    public static boolean checkPermission(Activity act){
        if (ActivityCompat.checkSelfPermission(act, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }else{
            return true;
        }
    }
}
