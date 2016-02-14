package bd.ac.bracu.marineblackbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PassengerActivity extends Activity{
	// JSON node keys
		private static final String TAG_COUNT = "passenger_count";

		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_passenger);
	        
	        // getting intent data
	        Intent in = getIntent();
	        
	        // Get JSON values from previous intent
	        String count = in.getStringExtra(TAG_COUNT);
	        
	        // Displaying all values on the screen
	        TextView lblCount = (TextView) findViewById(R.id.count_label);
	        
	        lblCount.setText(count);
	        
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
	        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarToday2);
	        int metric = (int)((Integer.parseInt(count) *100) /200);
			pb.setProgress( metric );
	    }
	}

