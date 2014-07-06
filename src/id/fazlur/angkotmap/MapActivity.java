package id.fazlur.angkotmap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.fazlur.angkotmap.database.crud.DBLocation;
import id.fazlur.angkotmap.database.crud.DBRoute;
import id.fazlur.angkotmap.database.model.Location;
import id.fazlur.angkotmap.database.model.Route;
// import id.fazlur.angkotmap.gmap.GMapV2Direction;
import id.fazlur.angkotmap.gmap.GetDirectionsAsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
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
	LatLng firstLatLng = AMSTERDAM, lastLatLng = PARIS;
	private GoogleMap map;
	private SupportMapFragment fragment;
	private LatLngBounds latlngBounds;
	private Polyline newPolyline;
	private PolylineOptions rectLine;
	private String waypoints = "";
	private int width, height;
	private long routeId;
	private DBRoute dbRoute;
	private Route route;
	private DBLocation dbLocation;
	private ArrayList<Location> locations;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		rectLine = new PolylineOptions().width(5).color(Color.RED);
		
		// Get route id
		Bundle bun = this.getIntent().getExtras();
        routeId = bun.getLong("ROUTE_ID");
        
        Log.i("Route ID", String.valueOf(routeId));
        
        // Setting the Map
		getScreenDimentions();
	    fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
		map = fragment.getMap();
		
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.setMyLocationEnabled(true);
		
		// Load all placemarks (places of interest)
		loadPlacemarks();
		
		// Load GMap Direction line from database
		loadDirections();
		
		// Search Routes in Assets folder by location ID
        // searchRoute();
		
		// findDirections(AMSTERDAM.latitude, AMSTERDAM.longitude, PARIS.latitude, PARIS.longitude, GMapV2Direction.MODE_DRIVING);
	}
	
	private void loadDirections()
	{
		Bundle bun = this.getIntent().getExtras();
        routeId = bun.getLong("ROUTE_ID");
                
        dbRoute = new DBRoute(this);
        
        dbRoute.open();
        
        route = dbRoute.getById(routeId);
        
        dbRoute.close();
        
        String routeDirections = route.getDirections();
		
		String[] routeLatLng = routeDirections.split(" ");
		
		for (int i = 0; i < routeLatLng.length; i++)
		{
			Double latitude, longitude;
			
			String[] splittedLatLng = routeLatLng[i].split(",");
			
			latitude = Double.valueOf(splittedLatLng[1]);
			
			longitude = Double.valueOf(splittedLatLng[0]);
			
			LatLng point = new LatLng(latitude, longitude);
			
			if (i == 0)
				firstLatLng = point;
			else if (i == routeLatLng.length - 1)
				lastLatLng = point;
			
			rectLine.add(point);
		}
		
		if (newPolyline != null)
			newPolyline.remove();
		
		newPolyline = map.addPolyline(rectLine);
		
		latlngBounds = createLatLngBoundsObject(firstLatLng, lastLatLng);
		map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
	}
	
	private void loadPlacemarks()
	{
		dbLocation = new DBLocation(this);
		
		dbLocation.open();
		
		locations = dbLocation.getAll();
		
		dbLocation.close();
		
		for (Location location : locations)
		{
			Log.v("info", "Location Lat: "+ location.getLat());
			Log.v("info", "Location Lang: "+ location.getLng());
			
			LatLng placeMarkPoint = new LatLng(location.getLat(), location.getLng());
			
			map.addMarker(new MarkerOptions()
                .title(location.getName())
                .position(placeMarkPoint));
			
		}
	}
	
	@SuppressWarnings("unused")
	private void searchRoute()
	{
		String loadedJSON = loadJSONFromAsset();
		
        try {
        	if (loadedJSON != "" & loadedJSON != null){
        		
        		JSONObject jsonObject = new JSONObject(loadedJSON);
				JSONArray placeMarks = jsonObject.getJSONArray("placemarks");
				
				for (int i = 0; i < placeMarks.length(); i++) {
					
					JSONObject jsonPlaceMark = placeMarks.getJSONObject(i);
					
					String placeMarkTitle = jsonPlaceMark.getString("name");
					
					Double placeMarkLat = Double.valueOf(jsonPlaceMark.getString("lat"));
					
					Double placeMarkLng = Double.valueOf(jsonPlaceMark.getString("lng"));
					
					LatLng placeMarkPoint = new LatLng(placeMarkLat, placeMarkLng);
					
					map.addMarker(new MarkerOptions()
		                .title(placeMarkTitle)
		                .position(placeMarkPoint));
				}
				
				String routeDirections = jsonObject.getString("directions");
				
				String[] routeLatLng = routeDirections.split(" ");
				
				for (int i = 0; i < routeLatLng.length; i++) {
					Double latitude, longitude;
					
					String[] splittedLatLng = routeLatLng[i].split(",");
					
					latitude = Double.valueOf(splittedLatLng[1]);
					
					longitude = Double.valueOf(splittedLatLng[0]);
					
					LatLng point = new LatLng(latitude, longitude);
					
					if (i == 0) {
						firstLatLng = point;
					}
					else if (i == routeLatLng.length - 1) {
						lastLatLng = point;
					}
					
					rectLine.add(point);
				}
				
				if (newPolyline != null)
				{
					newPolyline.remove();
				}
				
				newPolyline = map.addPolyline(rectLine);
				
				latlngBounds = createLatLngBoundsObject(firstLatLng, lastLatLng);
				map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
        	}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String loadJSONFromAsset() {
	    String json = null;
	    String routeName = "routes.json";
	    
	    Log.v("info", "Route Name : " + routeName);
	    
	    try {

	        InputStream is = getAssets().open(routeName);

	        int size = is.available();

	        byte[] buffer = new byte[size];

	        is.read(buffer);

	        is.close();

	        json = new String(buffer, "UTF-8");


	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	    
	    return json;
	}

	@Override
	protected void onResume() {
		super.onResume();
    	// latlngBounds = createLatLngBoundsObject(AMSTERDAM, PARIS);
        // map.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));

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
		
		GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this, waypoints);
		asyncTask.execute(map);
	}
}