package bd.ac.bracu.marineblackbox;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private ProgressDialog pDialog;
	Button loc;
	Button wea;
	Button pas;
	
	String count = "";
	String temp = "";
	String hum = "";
	String pre = "";
	String longi = "";
	String lat = "";
	String time = "";

	// URL to get contacts JSON
	private static String url = "https://data.sparkfun.com/output/jq33axOErgin8vLOj1ra.json";

	// JSON Node names
	//private static final String TAG_CONTACTS = "";
	private static final String TAG_COUNT = "passenger_count";
	private static final String TAG_TEMP = "temperature";
	private static final String TAG_HUM = "humidity";
	private static final String TAG_PRE = "pressure";
	private static final String TAG_LONG = "longitude";
	private static final String TAG_LAT = "latitude";
	private static final String TAG_TIME = "timestamp";

	// JSONArray
	JSONArray points = null;

	// Hashmap for ListView
	//ArrayList<HashMap<String, String>> pointList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//--------------------------------------------------
		loc = (Button) findViewById(R.id.button1);
		wea = (Button) findViewById(R.id.button2);
		pas = (Button) findViewById(R.id.button3);
		
		
        loc.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				Intent intLoc = new Intent(MainActivity.this, LocationActivity.class);
				intLoc.putExtra(TAG_LAT, lat);
				intLoc.putExtra(TAG_LONG, longi);
                startActivity(intLoc);
                
				/*Intent intWeather = new Intent(MainActivity.this, WeatherActivity.class);
				intWeather.putExtra(TAG_TEMP, temp);
				intWeather.putExtra(TAG_HUM, hum);
				intWeather.putExtra(TAG_PRE, pre);
                startActivity(intWeather);*/
                }
		});
        
        
		
        wea.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				Intent intWeather = new Intent(MainActivity.this, WeatherActivity.class);
				intWeather.putExtra(TAG_TEMP, temp);
				intWeather.putExtra(TAG_HUM, hum);
				intWeather.putExtra(TAG_PRE, pre);
                startActivity(intWeather);
			}
		});
        
        
		
        pas.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v){
				Intent intPassenger = new Intent(MainActivity.this, PassengerActivity.class);
				intPassenger.putExtra(TAG_COUNT, count);
                startActivity(intPassenger);
			}
		});

		//pointList = new ArrayList<HashMap<String, String>>();

		/*ListView lv = getListView();

		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String longi = ((TextView) view.findViewById(R.id.longitude))
						.getText().toString();
				String lat = ((TextView) view.findViewById(R.id.latitude))
						.getText().toString();
				String time = ((TextView) view.findViewById(R.id.timestamp))
						.getText().toString();

				// Starting single contact activity
				Intent intSingle = new Intent(getApplicationContext(),
						SingleContactActivity.class);
				intSingle.putExtra(TAG_LONG, longi);
				intSingle.putExtra(TAG_LAT, lat);
				intSingle.putExtra(TAG_TIME, time);
				startActivity(intSingle);

			}
		});*/

		// Calling async task to get json
		new GetPoints().execute();
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetPoints extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					//JSONObject jsonObj = new JSONObject(jsonStr);
					JSONArray json = new JSONArray(jsonStr);
					
					// Getting JSON Array node
					//contacts = jsonObj.getJSONArray(TAG_CONTACTS);

					// looping through All Contacts
					//for (int i = 0; i < json.length(); i++) {
					for (int i = 0; i < 1; i++) {
						//JSONObject c = contacts.getJSONObject(i);
						
						// tmp hashmap for single line
						//HashMap<String, String> point = new HashMap<String, String>();
						JSONObject c= json.getJSONObject(i);
						count = c.getString(TAG_COUNT);
						temp = c.getString(TAG_TEMP);
						hum = c.getString(TAG_HUM);
						pre = c.getString(TAG_PRE);
						longi = c.getString(TAG_LONG);
						lat = c.getString(TAG_LAT);
						time = c.getString(TAG_TIME);
						
						// adding each child node to HashMap key => value
						//contact.put(TAG_ID, id);
						//point.put(TAG_LONG, temp);
						//point.put(TAG_LAT, hum);
						//point.put(TAG_TIME, pre);

						//pointList.add(point);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			
			// Updating parsed JSON data into ListView
			/* 
			ListAdapter adapter = new SimpleAdapter(
					MainActivity.this, pointList,
					R.layout.list_item, new String[] { TAG_LONG, TAG_LAT, TAG_TIME }, new int[] { R.id.longitude,
							R.id.latitude, R.id.timestamp });

			setListAdapter(adapter);*/
		}

	}

}
