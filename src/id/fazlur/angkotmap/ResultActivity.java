package id.fazlur.angkotmap;

import id.fazlur.angkotmap.database.crud.DBRoute;
import id.fazlur.angkotmap.database.model.Route;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ResultActivity extends ListActivity implements OnClickListener {
	private Long locationIdFrom, locationIdTo;
	private boolean isFromMyLocation = false, isToMyLocation = false;
	private ListView routeList;
	private DBRoute dbRoute;
	private ArrayList<Route> routes = new ArrayList<Route>();
	private String noAngkotAvailable = "No Angkot Available";
	private ArrayList<String> noAngkot = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		Bundle bun = this.getIntent().getExtras();
        locationIdFrom = bun.getLong(MainActivity.FROM);
        locationIdTo = bun.getLong(MainActivity.TO);
        isFromMyLocation = bun.getBoolean(MainActivity.IS_FROM_MY_LOCATION);
        isToMyLocation = bun.getBoolean(MainActivity.IS_TO_MY_LOCATION);
        
        routeList = getListView();
        
        dbRoute = new DBRoute(this);
        
        dbRoute.open();
        
        routes = dbRoute.getResult(locationIdFrom, locationIdTo);
        
        dbRoute.close();
        
        if (routes.isEmpty()) {
        	noAngkot.add(noAngkotAvailable);
        	
        	ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noAngkot);
            setListAdapter(adapter);
        }
        else {
        	ListAdapter adapter = new ArrayAdapter<Route>(this, android.R.layout.simple_list_item_1, routes);
            
            setListAdapter(adapter);
            
            routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> av, View v, int position, long id) {
    				Route route = (Route) getListAdapter().getItem(position);
    			    switchToStep(route.getId());
    			}
            });
        }
	}

	@Override
	public void onClick(View v) {
		
	}
	
	public void switchToStep(long routeId) {
		Intent i = new Intent(getBaseContext(), StepActivity.class);
        Bundle bun = new Bundle();
        bun.putLong("ROUTE_ID", routeId);
        bun.putBoolean(MainActivity.IS_FROM_MY_LOCATION, isFromMyLocation);
        bun.putBoolean(MainActivity.IS_TO_MY_LOCATION, isToMyLocation);
        i.putExtras(bun);
        startActivity(i);
    }
	
}