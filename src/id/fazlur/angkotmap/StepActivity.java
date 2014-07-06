package id.fazlur.angkotmap;

import id.fazlur.angkotmap.database.crud.DBRoute;
import id.fazlur.angkotmap.database.model.Route;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;

public class StepActivity extends ListActivity implements OnClickListener {
	
	private Long routeId;
	private ImageButton btnShowMap;
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
        
//        String loadedJSON = loadJSONFromAsset();
//        
//        if (loadedJSON != "" & loadedJSON != null){
//        	
//        }
        
        btnShowMap = (ImageButton) this.findViewById(R.id.resultBtnMap);
        btnShowMap.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.resultBtnMap) {
			switchToMap();
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
