package id.fazlur.angkotmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.fazlur.angkotmap.gmap.GMapV2Direction;
import id.fazlur.angkotmap.gmap.GetDirectionsAsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;

public class MapActivity extends FragmentActivity {
	private static final LatLng AMSTERDAM = new LatLng(52.37518, 4.895439);
	private static final LatLng PARIS = new LatLng(48.856132, 2.352448);
//	private LatLng fromPosition;
//	private LatLng toPosition;
	private GoogleMap map;
	private SupportMapFragment fragment;
	private LatLngBounds latlngBounds;
	private Polyline newPolyline;
	private PolylineOptions rectLine;
	private int width, height;
	private long fromPositionID, toPositionID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		rectLine = new PolylineOptions().width(5).color(Color.RED);
		
		// Get from position id and to position id from main activity
		Bundle bun = this.getIntent().getExtras();
        fromPositionID = bun.getLong(MainActivity.FROM);
        toPositionID = bun.getLong(MainActivity.TO);
        
        Log.i("From ID", String.valueOf(fromPositionID));
        Log.i("To ID", String.valueOf(toPositionID));
        
        // Search location in Database by location ID
        
        // Setting the Map
		getScreenDimentions();
	    fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
		map = fragment.getMap();

		findDirections(AMSTERDAM.latitude, AMSTERDAM.longitude, PARIS.latitude, PARIS.longitude, GMapV2Direction.MODE_DRIVING);
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
    	latlngBounds = createLatLngBoundsObject(AMSTERDAM, PARIS);
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));

	}

	public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
		for(int i = 0 ; i < directionPoints.size() ; i++)
		{          
			rectLine.add(directionPoints.get(i));
		}
		
		if (newPolyline != null)
		{
			newPolyline.remove();
		}
		
		newPolyline = map.addPolyline(rectLine);
		
		latlngBounds = createLatLngBoundsObject(AMSTERDAM, PARIS);
		map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
		
	}
	
	@SuppressWarnings({ "deprecation" })
	private void getScreenDimentions()
	{
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
	}
	
	private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation)
	{
		if (firstLocation != null && secondLocation != null)
		{
			LatLngBounds.Builder builder = new LatLngBounds.Builder();    
			builder.include(firstLocation).include(secondLocation);
			
			return builder.build();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);
		
		GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
		asyncTask.execute(map);
	}
}