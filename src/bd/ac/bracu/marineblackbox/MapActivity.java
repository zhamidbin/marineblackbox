package bd.ac.bracu.marineblackbox;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;

public class MapActivity extends FragmentActivity implements
	GoogleApiClient.ConnectionCallbacks, 
	GoogleApiClient.OnConnectionFailedListener,
	LocationListener {
	
    public static final String TAG = LocationActivity.class.getSimpleName();
	private static final String TAG_LONG = "longitude";
	private static final String TAG_LAT = "latitude";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap myMap;
    private GoogleApiClient myGoogleApiClient;
    private LocationRequest myLocRequest;
    List<Overlay> myMapOverlays;
    GeoPoint p1, p2;
    LocationManager myLocManager;
    Drawable drawable;
    Document document;
    GMapV2GetRouteDirection v2GetRouteDirection;
    LatLng currentLatLng;
    LatLng fromPosition;
    LatLng toPosition;
    //GoogleMap mGoogleMap;
    MarkerOptions myMarkerOptions, myMarker;
    Location location;
    String longi="";
    String lat="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //-------------------------------------------------
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        lat = in.getStringExtra(TAG_LAT);
        longi = in.getStringExtra(TAG_LONG);
        
        
        v2GetRouteDirection = new GMapV2GetRouteDirection();
        setUpMapIfNeeded();

        myGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        myLocRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        myGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (myGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(myGoogleApiClient, this);
            myGoogleApiClient.disconnect();
        }
    }
    
    protected void onStop() {
    	super.onStop();
    	finish();
    }

    private void setUpMap() {
        myMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        LatLng latLng2 = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 14));
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
        myMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        myMarkerOptions = new MarkerOptions();
        myMarker = new MarkerOptions();
        fromPosition = currentLatLng;
        toPosition = latLng2;
        GetRouteTask getRoute = new GetRouteTask();
        getRoute.execute();        
    }
    

    private void setUpMapIfNeeded() {
        if (myMap == null) {
            
            myMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (myMap != null) {
                setUpMap();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(myGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(myGoogleApiClient, myLocRequest, this);
        }
        else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int j) {

    }
    
    private class GetRouteTask extends AsyncTask<String, Void, String> {
        
        private ProgressDialog Dialog;
        String response = "";
        @Override
        protected void onPreExecute() {
              Dialog = new ProgressDialog(MapActivity.this);
              Dialog.setMessage("Showing Route...");
              Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
              //Get All Route values
                    document = v2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
                    response = "Success";
              return response;

        }
    
    protected void onPostExecute(String result) {
          myMap.clear();
          if(response.equalsIgnoreCase("Success")){
          ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
          PolylineOptions rectLine = new PolylineOptions().width(8).color(
                      Color.BLUE);
            
          for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
          }
          // Adding route on the map
          myMap.addPolyline(rectLine);
          myMarkerOptions.position(toPosition);
          myMarkerOptions.draggable(true);
          myMap.addMarker(myMarkerOptions);

          
          }
         
          Dialog.dismiss();
    }
    }
    
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);                
            } catch (IntentSender.SendIntentException e) {}
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}

