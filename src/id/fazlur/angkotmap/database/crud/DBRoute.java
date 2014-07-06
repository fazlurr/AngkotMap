package id.fazlur.angkotmap.database.crud;

import id.fazlur.angkotmap.database.helper.HelperRoute;
import id.fazlur.angkotmap.database.model.Route;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBRoute {
	private SQLiteDatabase database;
    private HelperRoute helper;
 
    private String[] allColumns = { 
    		HelperRoute.COLUMN_ID,
    		HelperRoute.COLUMN_LOCATION_ID_FROM,
    		HelperRoute.COLUMN_LOCATION_ID_TO,
    		HelperRoute.COLUMN_STEPS,
    		HelperRoute.COLUMN_DIRECTIONS };
 
    public DBRoute(Context context)
    {
        helper = new HelperRoute(context);
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public Route create(Route route) {
 
        ContentValues values = new ContentValues();
        values.put(HelperRoute.COLUMN_LOCATION_ID_FROM, route.getLocation_id_from());
        values.put(HelperRoute.COLUMN_LOCATION_ID_TO, route.getLocation_id_to());
        values.put(HelperRoute.COLUMN_STEPS, route.getSteps());
        values.put(HelperRoute.COLUMN_DIRECTIONS, route.getDirections());
 
        long insertId = database.insert(HelperRoute.TABLE_NAME, null, values);
  
        Cursor cursor = database.query(HelperRoute.TABLE_NAME,
            allColumns, HelperRoute.COLUMN_ID + " = " + insertId, null,
            null, null, null);

        cursor.moveToFirst();

        Route newRoute = cursorToObject(cursor);

        cursor.close();

        return newRoute;
    }
 
    private Route cursorToObject(Cursor cursor) {
    	Route route = new Route();
        
    	route.setId(cursor.getLong(0));
    	route.setLocation_id_from(cursor.getLong(1));
    	route.setLocation_id_to(cursor.getLong(2));
    	route.setSteps(cursor.getString(3));
    	route.setDirections(cursor.getString(4));

        return route;
    }
    
    public ArrayList<Route> getAll() {
        ArrayList<Route> routeList = new ArrayList<Route>();
        
        Cursor cursor = database.query(HelperRoute.TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
        	Route route = cursorToObject(cursor);
        	routeList.add(route);
        	cursor.moveToNext();
        }
        
        cursor.close();
        return routeList;
    }
    
    public boolean isExists() {
    	boolean exists = false;

        Cursor cursor = database.query(HelperRoute.TABLE_NAME, allColumns, null, null, null, null, null);
        exists  = (cursor.getCount() > 0);
        cursor.close();

        return exists;
    }

    public Route getById(long id)
    {
    	Route route = new Route();
        //select query
        Cursor cursor = database.query(HelperRoute.TABLE_NAME, allColumns, "id="+id, null, null, null, null);
        //ambil data yang pertama
        cursor.moveToFirst();
        route = cursorToObject(cursor);
        cursor.close();
        return route;
    }

    public void update(Route route)
    {
        String strFilter = "id=" + route.getId();

        ContentValues args = new ContentValues();

        args.put(HelperRoute.COLUMN_LOCATION_ID_FROM, route.getLocation_id_from());
        args.put(HelperRoute.COLUMN_LOCATION_ID_TO, route.getLocation_id_to());
        args.put(HelperRoute.COLUMN_STEPS, route.getSteps());
        args.put(HelperRoute.COLUMN_DIRECTIONS, route.getDirections());

        database.update(HelperRoute.TABLE_NAME, args, strFilter, null);
    }
    
    public void delete(long id)
    {
        String strFilter = "id=" + id;
        database.delete(HelperRoute.TABLE_NAME, strFilter, null);
    }
    
    public ArrayList<Route> getResult(long locationIdFrom, long locationIdTo)
    {
    	ArrayList<Route> routeList = new ArrayList<Route>();
    	String[] selectionArgs = null;
    	String[] columns = allColumns;
    	
    	String table = HelperRoute.TABLE_NAME;
    	String selection = HelperRoute.COLUMN_LOCATION_ID_FROM + "=" + locationIdFrom + " AND " + HelperRoute.COLUMN_LOCATION_ID_TO + "=" + locationIdTo;
    	String groupBy = null, having = null, orderBy = null;
    	
    	Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    	
    	cursor.moveToFirst();
        
        while (!cursor.isAfterLast()) {
        	Route route = cursorToObject(cursor);
        	routeList.add(route);
        	cursor.moveToNext();
        }
        
        cursor.close();
        
        return routeList;
    }
}
