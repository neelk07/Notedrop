package com.example.android.geofence;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

/**
 * Created by neel on 4/12/14.
 */
public class GeofenceMap extends FragmentActivity implements
        GoogleMap.OnMarkerDragListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{


    private static final String TAG = GeofenceMap.class.getSimpleName();
    private SupportMapFragment mMap;
    private GoogleMap gMap = null;
    private int distance;
    Button set_location;

    //A request to connect to Location Services
    private LocationRequest mLocationRequest;

    private LocationClient mLocationClient;

    private Location currentLocation;

    private Marker current;

    private double requestedLongitude;
    private double requestedLatitude;

    public static int MILLISECONDS_PER_UPDATE = 500 * 100;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geofence_layout);
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        // Create a new global location parameters object
        mLocationRequest = LocationRequest.create();

        /*
         * Set the update interval
         */
        mLocationRequest.setInterval(MILLISECONDS_PER_UPDATE);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(MILLISECONDS_PER_UPDATE);

        mLocationClient = new LocationClient(this,this,this);
        mLocationClient.connect();

        set_location = (Button)findViewById(R.id.set_location_btn);
        set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMain();
            }
        });

        /*
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Hello world"));



        googleMap = mapFragment.getMap();
        googleMap.setOnMarkerDragListener(this);
        distance = 100;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                26.788707, 75.828108), 15));
        createGeofence(26.788707, 75.828108, distance, "CIRCLE", "GEOFENCE");
        */
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.

    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }


    private void createGeofence(double latitude, double longitude, int radius, String geofenceType, String title)
    {

        Marker stopMarker = gMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(new LatLng(latitude, longitude))
                .title(title)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_launcher)));

        gMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude)).radius(radius)
                .fillColor(Color.parseColor("#B2A9F6")));
    }

    @Override
    public void onMarkerDragStart(Marker marker)
    {

    }

    @Override
    public void onMarkerDrag(Marker marker)
    {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng dragPosition = marker.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;
        setRequestedLocation(dragLat,dragLong);
        gMap.clear();
        createGeofence(dragLat, dragLong, distance, "CIRCLE", "GEOFENCE");
        Toast.makeText(GeofenceMap.this,"onMarkerDragEnd dragLat :" + dragLat + " dragLong :" + dragLong, Toast.LENGTH_SHORT).show();
        Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);

    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Toast.makeText(this, "Connected",
                Toast.LENGTH_LONG).show();

        mLocationClient.requestLocationUpdates(mLocationRequest, new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                current.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                //gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                //        location.getLatitude(), location.getLongitude()), 17));
                Log.d(TAG, "location updated: " + location.getLongitude() + ", " + location.getLatitude());
                String msg = "Updated Location: " + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                setRequestedLocation(location);
            }
        });

        currentLocation = mLocationClient.getLastLocation();
        if(currentLocation != null)
        {
            if (gMap == null) {
                gMap = mMap.getMap();
                if (gMap != null) {
                    MarkerOptions curr = (new MarkerOptions()
                            .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                            .title("Testing")
                            .draggable(true));
                    current = gMap.addMarker(curr);
                    gMap.setOnMarkerDragListener(this);
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                            currentLocation.getLatitude(), currentLocation.getLongitude()), 17));
                    setRequestedLocation(currentLocation);
                }
            }
        }


    }

    @Override
    public void onDisconnected()
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }






    // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */ 
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    /*
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        current.setPosition(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        Log.d(TAG, "location updated: " + location.getLongitude() + ", " + location.getLatitude());
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    */

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */

                        break;
                }
        }
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Get the error code
            int errorCode = 00;//ConnectionResult.getErrorCode();
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getSupportFragmentManager(),
                        "Location Updates");
            }
            return  false;
        }
    }

    public void setRequestedLocation(Location location)
    {
        requestedLatitude = location.getLatitude();
        requestedLongitude = location.getLongitude();
    }

    public void setRequestedLocation(double longitude, double latitude)
    {
        requestedLatitude = latitude;
        requestedLongitude = longitude;
    }

    public void moveToMain()
    {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("long",requestedLatitude);
        i.putExtra("lat",requestedLongitude);
        startActivity(i);
    }

}
