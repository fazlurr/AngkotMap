package id.fazlur.angkotmap.library;

import java.util.ArrayList;

import id.fazlur.angkotmap.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StepArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final ArrayList<String> values;

	public StepArrayAdapter(Context context, ArrayList<String> stepsDescriptionMain) {
		super(context, R.layout.list_step_layout, stepsDescriptionMain);
	    this.context = context;
	    this.values = stepsDescriptionMain;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
		View rowView = inflater.inflate(R.layout.list_step_layout, parent, false);
	    
		TextView textView = (TextView) rowView.findViewById(R.id.label);
	    
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
	    
		textView.setText(values.get(position));
	    
	    // change the icon for Get On and Get Off
	    String s = values.get(position);
	    if (s.startsWith("Get on") || s.startsWith("Naik")) {
	    	imageView.setImageResource(R.drawable.ic_get_on);
	    }
	    else if (s.startsWith("Get off") || s.startsWith("Turun")) {
	    	imageView.setImageResource(R.drawable.ic_get_off);
	    }
	    else if (s.startsWith("Walk") || s.startsWith("Jalan")) {
	    	imageView.setImageResource(R.drawable.ic_get_off);
	    }

	    return rowView;
	  }
}