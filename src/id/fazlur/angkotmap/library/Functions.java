package id.fazlur.angkotmap.library;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import id.fazlur.angkotmap.database.crud.DBConnection;
import id.fazlur.angkotmap.database.model.Connection;
import id.fazlur.angkotmap.database.model.Location;

public class Functions {
	private DBConnection dbCon;
	
	public void findAngkot(Location locationFrom, Location locationTo, Context context) {
		long locationIdFrom = locationFrom.getId();
		long locationIdTo = locationTo.getId();
		ArrayList<Connection> locationBranches = null;
		
		dbCon = new DBConnection(context);
		
		locationBranches = dbCon.getLocationBranch(locationIdFrom);
		
		for (Connection con : locationBranches) {
		    Log.v("Branch id to", String.valueOf(con.getLocation_id_to()));
		    if(con.getLocation_id_to() == locationIdTo) {
		    	
		    }
		}
	}
}
