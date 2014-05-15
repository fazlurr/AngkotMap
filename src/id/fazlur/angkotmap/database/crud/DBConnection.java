package id.fazlur.angkotmap.database.crud;

import id.fazlur.angkotmap.database.helper.HelperConnection;
import id.fazlur.angkotmap.database.model.Connection;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBConnection {
	private SQLiteDatabase database;
	 
    // inisialisasi kelas DBHelper
    private HelperConnection helperConnection;
 
    // ambil semua nama kolom
    private String[] allColumns = { 
    		HelperConnection.COLUMN_ID,
    		HelperConnection.COLUMN_LOCATION_ID_FROM,
    		HelperConnection.COLUMN_LOCATION_ID_TO,
    		HelperConnection.COLUMN_LENGTH };
 
    // DBHelper diinstantiasi pada constructor
    public DBConnection(Context context)
    {
        helperConnection = new HelperConnection(context);
    }
 
    // Membuka atau membuat sambungan baru ke database
    public void open() throws SQLException {
        database = helperConnection.getWritableDatabase();
    }
 
    // Menutup sambungan ke database
    public void close() {
        helperConnection.close();
    }
 
    // Method untuk create/insert Angkot ke database
    public Connection create(Connection connection) {
 
        // membuat sebuah ContentValues, yang berfungsi
        // untuk memasangkan data dengan nama-nama
        // kolom pada database
        ContentValues values = new ContentValues();
        values.put(HelperConnection.COLUMN_LOCATION_ID_FROM, connection.getLocation_id_from());
        values.put(HelperConnection.COLUMN_LOCATION_ID_TO, connection.getLocation_id_to());
        values.put(HelperConnection.COLUMN_LENGTH, connection.getLength());
 
        // mengeksekusi perintah SQL insert data
        // yang akan mengembalikan sebuah insert ID 
        long insertId = database.insert(HelperConnection.TABLE_NAME, null,
            values);
 
        // setelah data dimasukkan, memanggil perintah SQL Select 
        // menggunakan Cursor untuk melihat apakah data tadi benar2 sudah masuk
        // dengan menyesuaikan ID = insertID 
        Cursor cursor = database.query(HelperConnection.TABLE_NAME,
            allColumns, HelperConnection.COLUMN_ID + " = " + insertId, null,
            null, null, null);
 
        // pindah ke data paling pertama 
        cursor.moveToFirst();
 
        // mengubah objek pada kursor pertama tadi
        // ke dalam objek barang
        Connection newConnection = cursorToObject(cursor);
 
        // close cursor
        cursor.close();
 
        // mengembalikan object baru
        return newConnection;
    }
 
    private Connection cursorToObject(Cursor cursor) {
        // buat objek barang baru
    	Connection con = new Connection();
        
        /* Set atribut pada objek barang dengan data kursor yang diambil dari database*/
    	con.setId(cursor.getLong(0));
    	con.setLocation_id_from(cursor.getLong(1));
    	con.setLocation_id_to(cursor.getLong(2));
    	con.setLength(cursor.getLong(3));
 
        //kembalikan sebagai objek
        return con;
    }
    
    //mengambil semua data
    public ArrayList<Connection> getAll() {
        ArrayList<Connection> conList = new ArrayList<Connection>();
     
        // select all SQL query
        Cursor cursor = database.query(HelperConnection.TABLE_NAME, allColumns, null, null, null, null, null);
     
        // pindah ke data paling pertama
        cursor.moveToFirst();
        // jika masih ada data, masukkan data barang ke daftar barang
        while (!cursor.isAfterLast()) {
        	Connection con = cursorToObject(cursor);
        	conList.add(con);
        	cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return conList;
    }
    
    public boolean isExists() {
    	boolean exists = false;
    	// select all SQL query
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
 
    // update by inserting object
    public void update(Connection con)
    {
        // ambil id user
        String strFilter = "id=" + con.getId();
        //memasukkan ke content values
        ContentValues args = new ContentValues();
        //masukkan data sesuai dengan kolom pada database
        args.put(HelperConnection.COLUMN_LOCATION_ID_FROM, con.getLocation_id_from());
        args.put(HelperConnection.COLUMN_LOCATION_ID_TO, con.getLocation_id_to());
        args.put(HelperConnection.COLUMN_LENGTH, con.getLength());
        // update query
        database.update(HelperConnection.TABLE_NAME, args, strFilter, null);
    }
    
    // Delete sesuai id
    public void delete(long id)
    {
        String strFilter = "id=" + id;
        database.delete(HelperConnection.TABLE_NAME, strFilter, null);
    }
}
