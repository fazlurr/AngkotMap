package id.fazlur.angkotmap;

import id.fazlur.angkotmap.database.crud.DBRoute;
import id.fazlur.angkotmap.database.model.Route;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class StepActivity extends ListActivity implements OnClickListener {
	
	private Long routeId;
	private DBRoute dbRoute;
	private Route route;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle bun = this.getIntent().getExtras();
        routeId = bun.getLong("ROUTE_ID");
                
        dbRoute = new DBRoute(this);
        
        dbRoute.open();
        
        route = dbRoute.getById(routeId);
        
        String[] steps = route.getSteps().split(",");
        
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, steps);
        
        setListAdapter(adapter);
        
	}

	@Override
	public void onClick(View v) {
		
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
	
	public void switchToMap() {
		Intent i = new Intent(getBaseContext(), MapActivity.class);
        Bundle bun = new Bundle();
        bun.putLong("ROUTE_ID", routeId);
        i.putExtras(bun);
        startActivity(i);
    }
}
