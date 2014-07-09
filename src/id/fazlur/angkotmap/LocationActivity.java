package id.fazlur.angkotmap;

import java.util.ArrayList;

import id.fazlur.angkotmap.database.DatabaseHelper;
import id.fazlur.angkotmap.database.crud.DBLocation;
import id.fazlur.angkotmap.database.model.Location;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LocationActivity extends ListActivity implements OnClickListener {
	private Integer optionCode;
	private ListView locationList;
	private DatabaseHelper myDbHelper = null;
	private DBLocation dbLocation;
	private ArrayList<Location> locations;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle bun = this.getIntent().getExtras();
		
		optionCode = bun.getInt("OPTION_CODE");
		
		Log.v("Option Code", String.valueOf(optionCode));
		
		if (optionCode == MainActivity.TO_LOCATION_REQUEST){
			this.setTitle("To");
		}
		
		locationList = getListView();
		
		myDbHelper = new DatabaseHelper(this);
		
        myDbHelper.createDatabase();
		
	 	dbLocation = new DBLocation(this);
	 	
	 	dbLocation.open();
	 	
	 	locations = dbLocation.getAll();
	 	
	 	ArrayAdapter<Location> adapter = new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1, locations);

        adapter.notifyDataSetChanged();
        
        // set adapter pada list
        setListAdapter(adapter);
		
        locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {
				Location location= (Location) getListAdapter().getItem(position);
			    Intent i = new Intent();
			    if (optionCode == MainActivity.FROM_LOCATION_REQUEST) {
			    	i.putExtra(MainActivity.FROM, location.getId());
			    }
			    else if (optionCode == MainActivity.TO_LOCATION_REQUEST) {
			    	i.putExtra(MainActivity.TO, location.getId());
			    }
			    i.putExtra("LOCATION_NAME", location.getName());
			    setResult(RESULT_OK, i);
			    finish();
			}
        });
        
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
