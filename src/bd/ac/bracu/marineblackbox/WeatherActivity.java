package bd.ac.bracu.marineblackbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WeatherActivity extends Activity{
	// JSON node keys
		private static final String TAG_TEMP = "temperature";
		private static final String TAG_HUM = "humidity";
		private static final String TAG_PRE = "pressure";
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_weather);
	        
	        // getting intent data
	        Intent in = getIntent();
	        
	        // Get JSON values from previous intent
	        String temp = in.getStringExtra(TAG_TEMP);
	        String hum = in.getStringExtra(TAG_HUM);
	        String pre = in.getStringExtra(TAG_PRE);
	        
	        // Displaying all values on the screen
	        TextView lblTemp = (TextView) findViewById(R.id.temp_label);
	        TextView lblHum = (TextView) findViewById(R.id.hum_label);
	        TextView lblPre = (TextView) findViewById(R.id.pre_label);
	        
	        lblTemp.setText(temp.substring(0, 2));
	        lblHum.setText(hum);
	        lblPre.setText(pre);
	        
	        //-----------------------------------------------------------------------
	        /*Button mapButton = (Button) findViewById(R.id.btn_enable);
	        mapButton.setOnClickListener(new OnClickListener() {
	                public void onClick(View m){
	                    Log.d("", "Loading Map..");
	                    // Loading Google Map View
	                    startService(new Intent(SingleContactActivity.this, MapViewer.class));
	                }       
	        });*/
	        //-------------------------------------------------------------------------
	        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarToday);
	        String just = temp.substring(0, 2);
	        int val = Integer.parseInt(just);
	        int metric = (int)((val*100)/50);
			pb.setProgress( metric );
	    }
	}

