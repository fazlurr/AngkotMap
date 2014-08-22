package id.fazlur.angkotmap;

import java.util.Locale;

import id.fazlur.angkotmap.database.crud.DBAngkot;
import id.fazlur.angkotmap.database.model.Angkot;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class AngkotActivity extends Activity {
	
	private ImageView angkotImage;
	private TextView textAngkotName;
	private TextView textAngkotDescription;
	private TextView textAngkotTime;
	
	private DBAngkot dbAngkot;
	private Angkot angkot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_angkot);
		
		angkotImage = (ImageView) findViewById(R.id.angkot_image);
		textAngkotName = (TextView) findViewById(R.id.angkot_name);
		textAngkotDescription = (TextView) findViewById(R.id.angkot_description);
		textAngkotTime = (TextView) findViewById(R.id.angkot_time);
		
		Bundle bun = this.getIntent().getExtras();
		String angkotName = bun.getString("ANGKOT_NAME");
		
		Log.v("Angkot Name", angkotName);
		
		dbAngkot = new DBAngkot(this);
		dbAngkot.open();
		
		angkot = dbAngkot.getAngkot(angkotName);
		
		setAngkotDetails(angkot);
	}
	
	public void setAngkotDetails(Angkot angkot) {
		
		String angkotName = angkot.getName().toLowerCase(Locale.getDefault());
		
		int resId = getResources().getIdentifier("id.fazlur.angkotmap:drawable/angkot_" + angkotName, null, null);
		
		angkotImage.setImageResource(resId);
		textAngkotName.setText(angkot.getName());
		textAngkotDescription.setText(angkot.getDescription());
		textAngkotTime.setText(angkot.getTime());
	}
}
