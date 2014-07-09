package id.fazlur.angkotmap;

import java.util.ArrayList;

import id.fazlur.angkotmap.database.crud.DBLocation;
import id.fazlur.angkotmap.database.model.Location;
import id.fazlur.angkotmap.library.GPSTracker;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	public static final int FROM_LOCATION_REQUEST = 1;
	public static final int TO_LOCATION_REQUEST = 2;
	public static final String FROM = "FROM";
	public static final String TO = "TO";
	private String locationFromName = "";
	private String locationToName = "";
	private Button btnSearch, btnFrom, btnTo;
	private long locationIdFrom = 0, locationIdTo = 0;
	private boolean isLocationFromSelected = false, isLocationToSelected = false;
	private DBLocation dbLocation;
	private ArrayList<Location> allLocations;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnSearch = (Button) this.findViewById(R.id.mainBtnSearch);
		btnFrom = (Button) this.findViewById(R.id.mainBtnFrom);
		btnTo= (Button) this.findViewById(R.id.mainBtnTo);
		
		btnSearch.setOnClickListener(this);
		btnFrom.setOnClickListener(this);
		btnTo.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	        case R.id.mainBtnSearch:
	    		switchToResult();
	            break;
	        case R.id.mainBtnFrom:
	        	switchToLocation(FROM_LOCATION_REQUEST);
	            break;
	        case R.id.mainBtnTo:
	        	switchToLocation(TO_LOCATION_REQUEST);
	            break;
	        default:
	        	break;
    	}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == FROM_LOCATION_REQUEST) {
	        if (resultCode == RESULT_OK) {
	        	locationIdFrom = data.getLongExtra(MainActivity.FROM, 0);

	        	locationFromName = data.getStringExtra("LOCATION_NAME");
	        	btnFrom.setText(locationFromName);
	        	
	        	isLocationFromSelected = true;
	        }
	    }
	    else if (requestCode == TO_LOCATION_REQUEST) {
	    	if (resultCode == RESULT_OK) {
	    		locationIdTo = data.getLongExtra(MainActivity.TO, 0);
	    		locationToName = data.getStringExtra("LOCATION_NAME");
	    		btnTo.setText(locationToName);
	    		
	    		isLocationToSelected = true;
	        }
	    }
	}
	
	public void switchToResult()
    {
		if (isLocationFromSelected == true && isLocationToSelected == true) {
			
			if ( locationIdFrom == 0 )
	        {
	        	locationIdFrom = getNearLocationId();
	        }
	        
			if ( locationIdTo == 0)
	        {
	        	locationIdTo = getNearLocationId();
	        }
			
			Intent i = new Intent(getBaseContext(), ResultActivity.class);
	        Bundle bun = new Bundle();
	        bun.putLong(FROM, locationIdFrom);
	        bun.putLong(TO, locationIdTo);
	        i.putExtras(bun);
	        startActivity(i);
			
		}
		else {
			Toast.makeText(this, "Harap pilih lokasi From dan To", Toast.LENGTH_SHORT).show();
		}
    }
	
	public void switchToLocation(Integer n)
    {
		Intent i = new Intent(getBaseContext(), LocationActivity.class);
		Bundle bun = new Bundle();
		bun.putInt("OPTION_CODE", n);
        i.putExtras(bun);
        startActivityForResult(i, n);
    }
	
	public Long getNearLocationId() {
		Location nearLocation = new Location(), currentLocation = new Location();
		LatLng latLngFrom = null, latLngTo = null;
		
		currentLocation = getCurrentLocation();
		
		Log.v("info", "Current Lat : " + currentLocation.getLat() + ", Current Long : " + currentLocation.getLng());
		
		latLngFrom = new LatLng(currentLocation.getLat(), currentLocation.getLng());
		
		dbLocation = new DBLocation(this);
	 	
	 	dbLocation.open();
	 	
	 	allLocations = dbLocation.getAll();
	 	
	 	double smallestDistance = 0;
	 	
	 	int i = 0;
	 	
	 	for (Location location : allLocations) {
	 		
			latLngTo = new LatLng(location.getLat(), location.getLng());
			
			double distance = SphericalUtil.computeDistanceBetween(latLngFrom, latLngTo);

			if ( i == 0 ) {
				smallestDistance = distance;
				nearLocation = location;
			}
			
			if (distance < smallestDistance){
				smallestDistance = distance;
				nearLocation = location;
			}
						
			i++;
		}
		
	 	Log.v("info", "Nearest location : " + nearLocation.getName());
	 	
		return nearLocation.getId();
	}
	
	public Location getCurrentLocation() {
		Location currentLocation = new Location(0, "My Location", 0, 0);
		
		// check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.canGetLocation())
        {            
            currentLocation.setName("My Location");
    		currentLocation.setLat(gpsTracker.getLatitude());
    		currentLocation.setLng(gpsTracker.getLongitude());
        }
        else
        {
            // Can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
		
		return currentLocation;
	}

}
