package id.fazlur.angkotmap.database.helper;

import id.fazlur.angkotmap.database.DbConst;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HelperAngkot extends SQLiteOpenHelper {
	
	public static final String TABLE_NAME = "angkot";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    private static final String DB_NAME = DbConst.NAME;
    private static final int DB_VERSION = DbConst.VERSION;
    
    @SuppressWarnings("unused")
	private static final String DB_CREATE = "create table "
    		+ TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " varchar(5) not null, "
            + COLUMN_DESCRIPTION + " varchar(50) not null); "; 
    
    public HelperAngkot(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // Auto generated
    }
 
    // Mengeksekusi perintah SQL di atas untuk membuat tabel database baru
    @Override
    public void onCreate(SQLiteDatabase db) {
    	// Not creating table because this app using pre-populated db
        // db.execSQL(DB_CREATE);
    }
 
    // Dijalankan apabila ingin mengupgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(HelperAngkot.class.getName(),"Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
