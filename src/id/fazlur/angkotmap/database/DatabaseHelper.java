package id.fazlur.angkotmap.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = DbConst.NAME;
    private static final int SCHEMA_VERSION = DbConst.VERSION;
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHelper(Context context) {
    	super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        this.myContext = context;
    }
 
    public void createDatabase() {
    	createDB();
    }
    
    public void createDB() {
    	File database= myContext.getDatabasePath(DATABASE_NAME);
    	
    	// boolean dbExist = isDBExist();
    	
    	if (!database.exists()) {
    		
    		Log.v("database info", "Db not exist");
    		
    		// Fungsi ini berguna untuk membuat database kosong di default location
    		this.getReadableDatabase();
    		
    		// Kemudian copy database
    		copyDBFromResource();
    		
    	}
    }
 
//    private boolean isDBExist() {
//    	SQLiteDatabase db = null;
//    	
//    	try {
//    		
//    		String databasePath = DATABASE_PATH + DATABASE_NAME;
//    		db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
////    		db.setLocale(Locale.getDefault());
//    		db.setVersion(SCHEMA_VERSION);
//    		
//    	} catch (SQLiteException e) {
//    		
//    		Log.e("SQLHelper", "database not found");
//    		
//    	}
//    	
//    	if (db != null) {
//    		db.close();
//    	}
//    	
//    	return db != null ? true : false;
//    }
    
    public void copyDBFromResource() {
    	
    	InputStream inputStream = null;
    	OutputStream outStream = null;
    	File dbFile = myContext.getDatabasePath(DATABASE_NAME);
    	String dbFilePath = dbFile.toString();
    	
    	try {
    		
    		inputStream = myContext.getAssets().open(DATABASE_NAME);
    		
    		outStream =  new FileOutputStream(dbFilePath);
    		
    		byte[] buffer = new byte[1024];
    		int length;
    		while ((length = inputStream.read(buffer)) > 0) {
    			outStream.write(buffer, 0, length);
    		}
    		
    		outStream.flush();
    		outStream.close();
    		inputStream.close();
    		
    	} catch (IOException e) {
    		
    		throw new Error("Problem copying database from resource file.");
    		
    	}
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 
}
