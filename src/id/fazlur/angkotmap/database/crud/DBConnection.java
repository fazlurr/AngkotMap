package id.fazlur.angkotmap.database.crud;

import id.fazlur.angkotmap.database.helper.HelperConnection;
import id.fazlur.angkotmap.database.model.Connection;
import id.fazlur.angkotmap.database.model.Node;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBConnection {
	private SQLiteDatabase database;
    private HelperConnection helperConnection;
 
    private String[] allColumns = { 
    		HelperConnection.COLUMN_ID,
    		HelperConnection.COLUMN_LOCATION_ID_FROM,
    		HelperConnection.COLUMN_LOCATION_ID_TO,
    		HelperConnection.COLUMN_LENGTH };
 
    public DBConnection(Context context)
    {
        helperConnection = new HelperConnection(context);
    }

    public void open() throws SQLException {
        database = helperConnection.getWritableDatabase();
    }

    public void close() {
        helperConnection.close();
    }

    public Connection create(Connection connection) {
 
        ContentValues values = new ContentValues();
        values.put(HelperConnection.COLUMN_LOCATION_ID_FROM, connection.getLocation_id_from());
        values.put(HelperConnection.COLUMN_LOCATION_ID_TO, connection.getLocation_id_to());
        values.put(HelperConnection.COLUMN_LENGTH, connection.getLength());
 
        long insertId = database.insert(HelperConnection.TABLE_NAME, null, values);
  
        Cursor cursor = database.query(HelperConnection.TABLE_NAME,
            allColumns, HelperConnection.COLUMN_ID + " = " + insertId, null,
            null, null, null);

        cursor.moveToFirst();

        Connection newConnection = cursorToObject(cursor);

        cursor.close();

        return newConnection;
    }
 
    private Connection cursorToObject(Cursor cursor) {
    	Connection con = new Connection();
        
    	con.setId(cursor.getLong(0));
    	con.setLocation_id_from(cursor.getLong(1));
    	con.setLocation_id_to(cursor.getLong(2));
    	con.setLength(cursor.getLong(3));

        return con;
    }
    
    private Node cursorToNode(Cursor cursor) {
    	Node node = new Node();
        
    	node.setId(cursor.getLong(0));
    	node.setLocation_id_from(cursor.getLong(1));
    	node.setLocation_id_to(cursor.getLong(2));
    	node.setLength(cursor.getLong(3));

    	return node;
    }
    
    public ArrayList<Connection> getAll() {
        ArrayList<Connection> conList = new ArrayList<Connection>();
        
        Cursor cursor = database.query(HelperConnection.TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
        	Connection con = cursorToObject(cursor);
        	conList.add(con);
        	cursor.moveToNext();
        }
        
        cursor.close();
        return conList;
    }
    
    public ArrayList<Node> getAllNodes() {
        ArrayList<Node> nodes = new ArrayList<Node>();

        Cursor cursor = database.query(HelperConnection.TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
        	Node node = cursorToNode(cursor);
        	nodes.add(node);
        	cursor.moveToNext();
        }

        cursor.close();
        return nodes;
    }
    
    public boolean isExists() {
    	boolean exists = false;

        Cursor cursor = database.query(HelperConnection.TABLE_NAME, allColumns, null, null, null, null, null);
        exists  = (cursor.getCount() > 0);
        cursor.close();

        return exists;
    }

    public Connection getById(long id)
    {
    	Connection con = new Connection();
        //select query
        Cursor cursor = database.query(HelperConnection.TABLE_NAME, allColumns, "id="+id, null, null, null, null);
        //ambil data yang pertama
        cursor.moveToFirst();
        con = cursorToObject(cursor);
        cursor.close();
        return con;
    }

    public void update(Connection con)
    {
        String strFilter = "id=" + con.getId();

        ContentValues args = new ContentValues();

        args.put(HelperConnection.COLUMN_LOCATION_ID_FROM, con.getLocation_id_from());
        args.put(HelperConnection.COLUMN_LOCATION_ID_TO, con.getLocation_id_to());
        args.put(HelperConnection.COLUMN_LENGTH, con.getLength());

        database.update(HelperConnection.TABLE_NAME, args, strFilter, null);
    }
    
    public void delete(long id)
    {
        String strFilter = "id=" + id;
        database.delete(HelperConnection.TABLE_NAME, strFilter, null);
    }
    
    // Custom query for searching algorithm
    public ArrayList<Connection> getBranch(long locationIdFrom, long locationIdTo)
    {
    	ArrayList<Connection> conList = new ArrayList<Connection>();
    	String[] selectionArgs = null;
    	String[] columns = allColumns;
    	
    	String table = HelperConnection.TABLE_NAME;
    	String selection = HelperConnection.COLUMN_LOCATION_ID_FROM + "=" + locationIdFrom;
    	String groupBy = null, having = null, orderBy = null;
    	
    	Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    	
    	cursor.moveToFirst();
        
        while (!cursor.isAfterLast()) {
        	Connection con = cursorToObject(cursor);
        	conList.add(con);
        	cursor.moveToNext();
        }
        
        cursor.close();
        
        return conList;
    }
    
    public ArrayList<Connection> getLocationBranch(long locationIdFrom)
    {
    	ArrayList<Connection> conList = new ArrayList<Connection>();
    	String[] selectionArgs = null;
    	String[] columns = allColumns;
    	
    	String table = HelperConnection.TABLE_NAME;
    	String selection = HelperConnection.COLUMN_LOCATION_ID_FROM + "=" + locationIdFrom;
    	String groupBy = null, having = null, orderBy = null;
    	
    	Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    	
    	cursor.moveToFirst();
        
        while (!cursor.isAfterLast()) {
        	Connection con = cursorToObject(cursor);
        	conList.add(con);
        	cursor.moveToNext();
        }
        
        cursor.close();
        
        return conList;
    }
}
