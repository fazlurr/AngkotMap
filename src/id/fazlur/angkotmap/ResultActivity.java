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
	private ListView routeList;
	private DBRoute dbRoute;
	private ArrayList<Route> routes = new ArrayList<Route>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle bun = this.getIntent().getExtras();
        locationIdFrom = bun.getLong(MainActivity.FROM);
        locationIdTo = bun.getLong(MainActivity.TO);
        
        routeList = getListView();
        
        dbRoute = new DBRoute(this);
        
        dbRoute.open();
        
        routes = dbRoute.getResult(locationIdFrom, locationIdTo);
        
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

	@Override
	public void onClick(View v) {
		
	}
	
	public void switchToStep(long routeId) {
		Intent i = new Intent(getBaseContext(), StepActivity.class);
        Bundle bun = new Bundle();
        bun.putLong("ROUTE_ID", routeId);
        i.putExtras(bun);
        startActivity(i);
    }
}
