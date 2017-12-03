package app.inner.drinkanddrivebrothers.controller;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import app.inner.drinkanddrivebrothers.R;
import app.inner.drinkanddrivebrothers.googleApi.DirectionFinder;
import app.inner.drinkanddrivebrothers.googleApi.DirectionFinderListener;
import app.inner.drinkanddrivebrothers.googleApi.Route;
import app.inner.drinkanddrivebrothers.utility.Util;

public class MapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback, DirectionFinderListener {

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int INITIAL_REQUEST=1337;


    private static final int PLAY_SERVICES_REQUEST = 1000;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private List<Polyline> polylines;
    private PolylineOptions polyOpt;
    private GoogleMap myGoogleMap;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private MapFragment mapFragment;
    private GoogleMap mMap;

    private boolean mRequestLocationUpdates, fckedCheck = false;

    private LocationRequest mLocationRequest;

    private static int UPDATE_INTERVAL = 300;
    private static int FATEST_INTERVAL = 300;
    private static int DISPLACEMENT = 10;

    private EditText etDestination;
    private Button btnStartLocationUpdates, btnClearLines, btnFindPath;
    private float distance = 0;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
//        }

//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
//                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        etDestination = findViewById(R.id.etDestination);
        btnFindPath = findViewById(R.id.btnFindPath);

        btnStartLocationUpdates = findViewById(R.id.btnLocationUpdate);

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        btnStartLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePeriodLocationUpdates();
            }
        });

//        btnClearLines.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopLocationUpdates();
//
//            }
//        });

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void sendRequest() {
        if (etDestination == null || etDestination.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter a valid destination", Toast.LENGTH_SHORT).show();
            return;
        }
        String destination = etDestination.getText().toString();
        if(mLastLocation == null){
            Toast.makeText(this, "Първо стартирайте курса!", Toast.LENGTH_SHORT).show();
            return;
        }
        String currentAddress = mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude();

        try {
            new DirectionFinder(this, currentAddress, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        if (mGoogleApiClient.isConnected() && mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void togglePeriodLocationUpdates() {
        if (!mRequestLocationUpdates) {
            btnStartLocationUpdates.setText("стоп");
            mRequestLocationUpdates = true;
            startLocationUpdates();
        } else {
            btnStartLocationUpdates.setText("старт");
            mRequestLocationUpdates = false;
            stopLocationUpdates();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        if (Util.checkPermission(this)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        polylines = new ArrayList<>();
        polyOpt = new PolylineOptions();
    }

    protected void stopLocationUpdates() {
        btnStartLocationUpdates.setText("start updates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        if (polylines != null && polylines.size() > 0) {
            for (int i = 0; i < polylines.size(); i++) {
                polylines.get(i).remove();
            }
            polylines.clear();
            polylines = null;
        }

        // tazi proverka e zashtoto inache vliza 2 puti (nqmam obqsnenie zashto...)
        if(!fckedCheck) {

            Intent intent = new Intent();
            intent.putExtra("distance", distance);
            setResult(0, intent);
            distance = 0;
            finish();
            fckedCheck = true;
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double lastPointLat = 0;
        double lastPointLong = 0;

        if (mLastLocation == null) {
            lastPointLat = location.getLatitude();
            lastPointLong = location.getLongitude();
        } else {
            lastPointLat = mLastLocation.getLatitude();
            lastPointLong = mLastLocation.getLongitude();
        }
        LatLng latLng = new LatLng(lastPointLat, lastPointLong);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        mMap.moveCamera(cameraUpdate);
        mMap.animateCamera(cameraUpdate);

        mLastLocation = location;
        // singleton class
        float[] res = new float[3];
        Location.distanceBetween(lastPointLat, lastPointLong, location.getLatitude(), location.getLongitude(), res);
        distance += res[0];

        polyOpt.add(new LatLng(location.getLatitude(), location.getLongitude()));
        polylines.add(myGoogleMap.addPolyline(polyOpt));
      //  Toast.makeText(this, "Accurace: " + location.getAccuracy(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myGoogleMap = googleMap;
        if (Util.checkPermission(this)) {
            myGoogleMap.setMyLocationEnabled(true);
        } else {
            myGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(myGoogleMap.addMarker(new MarkerOptions()
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(myGoogleMap.addMarker(new MarkerOptions()
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(myGoogleMap.addPolyline(polylineOptions));
        }
    }
}
