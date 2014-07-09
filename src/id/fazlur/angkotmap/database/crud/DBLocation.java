package id.fazlur.angkotmap.database.crud;

import id.fazlur.angkotmap.R;
import id.fazlur.angkotmap.database.helper.HelperLocation;
import id.fazlur.angkotmap.database.model.Location;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBLocation {
	private Context context;
	private SQLiteDatabase database;
	 
    // inisialisasi kelas DBHelper
    private HelperLocation helper;
 
    // ambil semua nama kolom
    private String[] allColumns = { 
    		HelperLocation.COLUMN_ID,
    		HelperLocation.COLUMN_NAME,
    		HelperLocation.COLUMN_LAT,
    		HelperLocation.COLUMN_LNG };
 
    // DBHelper diinstantiasi pada constructor
    public DBLocation(Context context)
    {
    	this.context = context;
        helper = new HelperLocation(context);
    }
 
    // Membuka atau membuat sambungan baru ke database
    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }
 
    // Menutup sambungan ke database
    public void close() {
        helper.close();
    }
 
    // Method untuk create/insert Angkot ke database
    public Location create(Location loc) {
 
        // membuat sebuah ContentValues, yang berfungsi
        // untuk memasangkan data dengan nama-nama
        // kolom pada database
        ContentValues values = new ContentValues();
        values.put(HelperLocation.COLUMN_NAME, loc.getName());
        values.put(HelperLocation.COLUMN_LAT, loc.getLat());
        values.put(HelperLocation.COLUMN_LNG, loc.getLng());
 
        // mengeksekusi perintah SQL insert data
        // yang akan mengembalikan sebuah insert ID 
        long insertId = database.insert(HelperLocation.TABLE_NAME, null,
            values);
 
        // setelah data dimasukkan, memanggil perintah SQL Select 
        // menggunakan Cursor untuk melihat apakah data tadi benar2 sudah masuk
        // dengan menyesuaikan ID = insertID 
        Cursor cursor = database.query(HelperLocation.TABLE_NAME,
            allColumns, HelperLocation.COLUMN_ID + " = " + insertId, null,
            null, null, null);
 
        // pindah ke data paling pertama 
        cursor.moveToFirst();
 
        // mengubah objek pada kursor pertama tadi
        // ke dalam objek barang
        Location newLocation = cursorToObject(cursor);
 
        // close cursor
        cursor.close();
 
        // mengembalikan object baru
        return newLocation;
    }
 
    private Location cursorToObject(Cursor cursor) {
        // buat objek barang baru
    	Location loc = new Location();
        
        /* Set atribut pada objek barang dengan data kursor yang diambil dari database*/
    	loc.setId(cursor.getLong(0));
    	loc.setName(cursor.getString(1));
    	loc.setLat(cursor.getDouble(2));
    	loc.setLng(cursor.getDouble(3));
 
        //kembalikan sebagai objek
        return loc;
    }
    
    //mengambil semua data
    public ArrayList<Location> getAll() {
        ArrayList<Location> locList = new ArrayList<Location>();

        String orderByFilter = HelperLocation.COLUMN_NAME + " ASC";
        
        // select all SQL query
        Cursor cursor = database.query(HelperLocation.TABLE_NAME, allColumns, null, null, null, null, orderByFilter);
     
        // pindah ke data paling pertama
        cursor.moveToFirst();
        
        // masukkan my location ke daftar lokasi
        Location myLocation = new Location(0, context.getResources().getString(R.string.my_location), 0, 0);
        
        locList.add(myLocation);
        
        // jika masih ada data, masukkan data lokasi ke daftar lokasi
        while (!cursor.isAfterLast()) {
        	Location loc = cursorToObject(cursor);
        	locList.add(loc);
        	cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return locList;
    }
    
    public boolean isExists() {
    	boolean exists = false;
    	// select all SQL query
        Cursor cursor = database.query(HelperLocation.TABLE_NAME, allColumns, null, null, null, null, null);
        exists  = (cursor.getCount() > 0);
        cursor.close();
    	return exists;
    }

    public Location getById(long id)
    {
    	Location loc = new Location();
        //select query
        Cursor cursor = database.query(HelperLocation.TABLE_NAME, allColumns, "id="+id, null, null, null, null);
        //ambil data yang pertama
        cursor.moveToFirst();
        loc = cursorToObject(cursor);
        cursor.close();
        return loc;
    }
 
    // update by inserting object
    public void update(Location loc)
    {
        // ambil id user
        String strFilter = "id=" + loc.getId();
        //memasukkan ke content values
        ContentValues args = new ContentValues();
        //masukkan data sesuai dengan kolom pada database
        args.put(HelperLocation.COLUMN_NAME, loc.getName());
        args.put(HelperLocation.COLUMN_LAT, loc.getLat());
        args.put(HelperLocation.COLUMN_LNG, loc.getLng());
        // update query
        database.update(HelperLocation.TABLE_NAME, args, strFilter, null);
    }
    
    // Delete sesuai id
    public void delete(long id)
    {
        String strFilter = "id=" + id;
        database.delete(HelperLocation.TABLE_NAME, strFilter, null);
    }
}
