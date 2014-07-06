package id.fazlur.angkotmap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

}
