package id.fazlur.angkotmap;

import java.util.ArrayList;

import id.fazlur.angkotmap.database.DatabaseHelper;
import id.fazlur.angkotmap.database.crud.DBLocation;
import id.fazlur.angkotmap.database.model.Location;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class LocationActivity extends ListActivity implements OnClickListener {
	private Integer optionCode;
	private ListView locationList;
	private DatabaseHelper myDbHelper;
	private DBLocation dbLocation;
	private ArrayList<Location> locations;
	private ArrayAdapter<Location> adapter;
	private EditText inputSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		
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
	 	
	 	adapter = new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1, locations);

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
        
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                LocationActivity.this.adapter.getFilter().filter(cs);   
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
