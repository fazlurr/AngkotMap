package id.fazlur.angkotmap.database.crud;

import id.fazlur.angkotmap.database.helper.HelperTrack;
import id.fazlur.angkotmap.database.model.Track;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBTrack {
	private SQLiteDatabase database;
	 
    // inisialisasi kelas DBHelper
    private HelperTrack helper;
 
    // ambil semua nama kolom
    private String[] allColumns = { 
    		HelperTrack.COLUMN_ID,
    		HelperTrack.COLUMN_CONNECTION_ID,
    		HelperTrack.COLUMN_ANGKOT_ID,
    		HelperTrack.COLUMN_PATH };
 
    // DBHelper diinstantiasi pada constructor
    public DBTrack(Context context)
    {
        helper = new HelperTrack(context);
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
    public Track create(Track track) {
 
        // membuat sebuah ContentValues, yang berfungsi
        // untuk memasangkan data dengan nama-nama
        // kolom pada database
        ContentValues values = new ContentValues();
        values.put(HelperTrack.COLUMN_CONNECTION_ID, track.getConnection_id());
        values.put(HelperTrack.COLUMN_ANGKOT_ID, track.getAngkot_id());
        values.put(HelperTrack.COLUMN_PATH, track.getPath());
 
        // mengeksekusi perintah SQL insert data
        // yang akan mengembalikan sebuah insert ID 
        long insertId = database.insert(HelperTrack.TABLE_NAME, null,
            values);
 
        // setelah data dimasukkan, memanggil perintah SQL Select 
        // menggunakan Cursor untuk melihat apakah data tadi benar2 sudah masuk
        // dengan menyesuaikan ID = insertID 
        Cursor cursor = database.query(HelperTrack.TABLE_NAME,
            allColumns, HelperTrack.COLUMN_ID + " = " + insertId, null,
            null, null, null);
 
        // pindah ke data paling pertama 
        cursor.moveToFirst();
 
        // mengubah objek pada kursor pertama tadi
        // ke dalam objek barang
        Track newTrack = cursorToObject(cursor);
 
        // close cursor
        cursor.close();
 
        // mengembalikan object baru
        return newTrack;
    }
 
    private Track cursorToObject(Cursor cursor) {
        // buat objek barang baru
    	Track track = new Track();
        
        /* Set atribut pada objek barang dengan data kursor yang diambil dari database*/
    	track.setId(cursor.getLong(0));
    	track.setConnection_id(cursor.getLong(1));
    	track.setAngkot_id(cursor.getLong(2));
    	track.setPath(cursor.getString(3));
 
        //kembalikan sebagai objek
        return track;
    }
    
    //mengambil semua data
    public ArrayList<Track> getAll() {
        ArrayList<Track> trackList = new ArrayList<Track>();
     
        // select all SQL query
        Cursor cursor = database.query(HelperTrack.TABLE_NAME, allColumns, null, null, null, null, null);
     
        // pindah ke data paling pertama
        cursor.moveToFirst();
        // jika masih ada data, masukkan data barang ke daftar barang
        while (!cursor.isAfterLast()) {
        	Track track = cursorToObject(cursor);
        	trackList.add(track);
        	cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return trackList;
    }
    
    public boolean isExists() {
    	boolean exists = false;
    	// select all SQL query
        Cursor cursor = database.query(HelperTrack.TABLE_NAME, allColumns, null, null, null, null, null);
        exists  = (cursor.getCount() > 0);
        cursor.close();
    	return exists;
    }

    public Track getById(long id)
    {
    	Track track = new Track();
        //select query
        Cursor cursor = database.query(HelperTrack.TABLE_NAME, allColumns, "id="+id, null, null, null, null);
        //ambil data yang pertama
        cursor.moveToFirst();
        track = cursorToObject(cursor);
        cursor.close();
        return track;
    }
 
    // update by inserting object
    public void update(Track track)
    {
        // ambil id user
        String strFilter = "id=" + track.getId();
        //memasukkan ke content values
        ContentValues args = new ContentValues();
        //masukkan data sesuai dengan kolom pada database
        args.put(HelperTrack.COLUMN_CONNECTION_ID, track.getConnection_id());
        args.put(HelperTrack.COLUMN_ANGKOT_ID, track.getAngkot_id());
        args.put(HelperTrack.COLUMN_PATH, track.getPath());
        // update query
        database.update(HelperTrack.TABLE_NAME, args, strFilter, null);
    }
    
    // Delete sesuai id
    public void delete(long id)
    {
        String strFilter = "id=" + id;
        database.delete(HelperTrack.TABLE_NAME, strFilter, null);
    }
}
