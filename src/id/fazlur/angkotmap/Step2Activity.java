package id.fazlur.angkotmap;

import java.util.ArrayList;

import id.fazlur.angkotmap.database.crud.DBAngkot;
import id.fazlur.angkotmap.database.crud.DBConnection;
import id.fazlur.angkotmap.database.crud.DBLocation;
import id.fazlur.angkotmap.database.crud.DBRoute;
import id.fazlur.angkotmap.database.crud.DBTrack;
import id.fazlur.angkotmap.database.model.Angkot;
import id.fazlur.angkotmap.database.model.Connection;
import id.fazlur.angkotmap.database.model.Location;
import id.fazlur.angkotmap.database.model.Route;
import id.fazlur.angkotmap.database.model.Track;
import id.fazlur.angkotmap.library.StepArrayAdapter;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class Step2Activity extends ListActivity {
	
	public static final String WALK = "Jalan kaki ";
	public static final String FROM = "dari ";
	public static final String TO = "ke ";
	public static final String GET_ON = "Naik ";
	public static final String GET_OFF = "Turun ";
	public static final String AT = "di ";
	public static final String YOUR_LOCATION = "lokasi anda ";
	
	private Long routeId;
	private DBRoute dbRoute;
	private Route route;
	private DBLocation dbLocation;
	private DBConnection dbConnection;
	private Connection connection;
	private DBTrack dbTrack;
	private Track track;
	private ArrayList<String> stepsDescriptionMain;
	private boolean isFromMyLocation = false, isToMyLocation = false;
	private DBAngkot dbAngkot;
	private Angkot angkot;
	private StepArrayAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step);
		
		Bundle bun = this.getIntent().getExtras();
        routeId = bun.getLong("ROUTE_ID");
        isFromMyLocation = bun.getBoolean(MainActivity.IS_FROM_MY_LOCATION);
        isToMyLocation = bun.getBoolean(MainActivity.IS_TO_MY_LOCATION);
                
        dbRoute = new DBRoute(this);
        
        dbRoute.open();
        
        route = dbRoute.getById(routeId);
        
        dbRoute.close();
        
        stepsDescriptionMain = generateSteps(route.getSteps());
        
        adapter = new StepArrayAdapter(this, stepsDescriptionMain);
        
        setListAdapter(adapter);
        
	}

//	@Override
//	public void onClick(View v) {
//		
//	}
	
	@SuppressLint("DefaultLocale")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		String[] splittedItem = item.split(" ");
		String angkotName = splittedItem[1];
		switchToAngkot(angkotName);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_step_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_show_map:
	            switchToMap();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public ArrayList<String> generateSteps(String steps) {
		dbTrack = new DBTrack(this);
		dbTrack.open();
		
		dbConnection = new DBConnection(this);
		dbConnection.open();
		
		dbLocation = new DBLocation(this);
		dbLocation.open();
		
		dbAngkot = new DBAngkot(this);
		dbAngkot.open();
		
		ArrayList<String> stepsDescription = new ArrayList<String>();
		
		String[] splittedSteps = steps.split(",");
		
		Location locationFrom, locationTo;
		
		int i = 0;
		long prevAngkotId = 0;
		long nextAngkotId = 0;
		
		for (int j = 0; j < splittedSteps.length; j++) {
			long trackId = Long.valueOf(splittedSteps[j]);
			
			track = dbTrack.getById(trackId);
			
			connection = dbConnection.getById(track.getConnection_id());
			
			angkot = dbAngkot.getAngkot(track.getAngkot_id());
			
			Log.v("Steps Count", String.valueOf(splittedSteps.length));
			
			if ( j+1 <= splittedSteps.length ) {
				// if not the last step
				long nextTrackId = Long.valueOf(splittedSteps[j+1]);
				Track nextTrack = dbTrack.getById(nextTrackId);
				Angkot nextAngkot = dbAngkot.getAngkot(nextTrack.getAngkot_id()); 
				nextAngkotId =  nextAngkot.getId();
			}
			
			locationFrom = dbLocation.getById(connection.getLocation_id_from());
			locationTo = dbLocation.getById(connection.getLocation_id_to()); 
			
			if (i == 0 && isFromMyLocation) {
				String stepContent = WALK + FROM + YOUR_LOCATION + TO + locationFrom.getName();
				stepsDescription.add(stepContent);
			}
			
			if ( j == 0 ) {
				// the first step
				String stepContentOn = GET_ON + angkot.getName() + " " + AT + locationFrom.getName();
				stepsDescription.add(stepContentOn);
			}
			else if ( j == splittedSteps.length - 1 || ( angkot.getId() != nextAngkotId )) {
				// if it is the last step or the next angkot is difference, then get off
				String stepContentOff = GET_OFF + AT + locationTo.getName();
				stepsDescription.add(stepContentOff);
			}
			else if ( (prevAngkotId != angkot.getId()) ) {
				// if the current angkot is not same with previous angkot, then take the next angkot
				String stepContentOn = GET_ON + angkot.getName() + " " + AT + locationFrom.getName();
				stepsDescription.add(stepContentOn);
				
			}
			else if ( (prevAngkotId == angkot.getId()) ) {
				// if the current angkot is same with previous angkot	
				
			}
			
			if (i == splittedSteps.length && isToMyLocation) {
				String stepContent1 = WALK + TO + YOUR_LOCATION;
				stepsDescription.add(stepContent1);
			}
			
			prevAngkotId = angkot.getId();
			
			i++;
		}
		
		dbTrack.close();
		dbConnection.close();
		dbLocation.close();
		dbAngkot.close();
		
		return stepsDescription;
	}
	
	public void switchToMap() {
		Intent i = new Intent(getBaseContext(), MapActivity.class);
        Bundle bun = new Bundle();
        bun.putLong("ROUTE_ID", routeId);
        i.putExtras(bun);
        startActivity(i);
    }
	
	public void switchToAngkot(String angkotName) {
		Intent i = new Intent(getBaseContext(), AngkotActivity.class);
        Bundle bun = new Bundle();
        bun.putString("ANGKOT_NAME", angkotName);
        i.putExtras(bun);
        startActivity(i);
	}
}
