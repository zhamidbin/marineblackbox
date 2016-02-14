package bd.ac.bracu.marineblackbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LocationActivity extends Activity{
	// JSON node keys
		private static final String TAG_LONG = "longitude";
		private static final String TAG_LAT = "latitude";
		private static final String TAG_TIME = "timestamp";
		
		String longi = "";
		String lat = "";
		Button mapButton;
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_location);
	        
	        // getting intent data
	        Intent in = getIntent();
	        
	        // Get JSON values from previous intent
	        longi = in.getStringExtra(TAG_LONG);
	        lat = in.getStringExtra(TAG_LAT);
	        	        
	        TextView lblLongi = (TextView) findViewById(R.id.longi_label);
	        TextView lblLat = (TextView) findViewById(R.id.lat_label);
	        
	        lblLongi.setText(longi.substring(0, 5));
	        lblLat.setText(lat.substring(0, 5));
	        
	        mapButton = (Button) findViewById(R.id.btn_enable);
	        mapButton.setOnClickListener(new OnClickListener() {
	                public void onClick(View v){
	                    Intent intMap = new Intent(LocationActivity.this, MapActivity.class);
	    				intMap.putExtra(TAG_LAT, lat);
	    				intMap.putExtra(TAG_LONG, longi);
	                    startActivity(intMap);
	                }       
	        });
	    }
	}

