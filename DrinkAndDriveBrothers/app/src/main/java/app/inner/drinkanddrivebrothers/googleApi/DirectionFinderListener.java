package app.inner.drinkanddrivebrothers.googleApi;

import java.util.List;

/**
 * Created by plame_000 on 11-Nov-17.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
