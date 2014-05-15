package id.fazlur.angkotmap.database.crud;

import id.fazlur.angkotmap.database.helper.HelperAngkot;
import id.fazlur.angkotmap.database.model.Angkot;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAngkot {
	private SQLiteDatabase database;
	 
    // inisialisasi kelas DBHelper
    private HelperAngkot helperAngkot;
 
    // ambil semua nama kolom
    private String[] allColumns = { 
    		HelperAngkot.COLUMN_ID,
    		HelperAngkot.COLUMN_NAME,
    		HelperAngkot.COLUMN_DESCRIPTION };
 
    // DBHelper diinstantiasi pada constructor
    public DBAngkot(Context context)
    {
        helperAngkot = new HelperAngkot(context);
    }
 
    // Membuka atau membuat sambungan baru ke database
    public void open() throws SQLException {
        database = helperAngkot.getWritableDatabase();
    }
 
    // Menutup sambungan ke database
    public void close() {
        helperAngkot.close();
    }
 
    // Method untuk create/insert Angkot ke database
    public Angkot createAngkot(Angkot angkot) {
 
        // membuat sebuah ContentValues, yang berfungsi
        // untuk memasangkan data dengan nama-nama
        // kolom pada database
        ContentValues values = new ContentValues();
        values.put(HelperAngkot.COLUMN_NAME, angkot.getName());
        values.put(HelperAngkot.COLUMN_DESCRIPTION, angkot.getDescription());
 
        // mengeksekusi perintah SQL insert data
        // yang akan mengembalikan sebuah insert ID 
        long insertId = database.insert(HelperAngkot.TABLE_NAME, null,
            values);
 
        // setelah data dimasukkan, memanggil perintah SQL Select 
        // menggunakan Cursor untuk melihat apakah data tadi benar2 sudah masuk
        // dengan menyesuaikan ID = insertID 
        Cursor cursor = database.query(HelperAngkot.TABLE_NAME,
            allColumns, HelperAngkot.COLUMN_ID + " = " + insertId, null,
            null, null, null);
 
        // pindah ke data paling pertama 
        cursor.moveToFirst();
 
        // mengubah objek pada kursor pertama tadi
        // ke dalam objek barang
        Angkot newAngkot = cursorToAngkot(cursor);
 
        // close cursor
        cursor.close();
 
        // mengembalikan barang baru
        return newAngkot;
    }
 
    private Angkot cursorToAngkot(Cursor cursor) {
        // buat objek barang baru
    	Angkot angkot = new Angkot();
        
        /* Set atribut pada objek barang dengan data kursor yang diambil dari database*/
    	angkot.setId(cursor.getLong(0));
    	angkot.setName(cursor.getString(1));
    	angkot.setDescription(cursor.getString(2));
 
        //kembalikan sebagai objek barang
        return angkot;
    }
    
    //mengambil semua data angkot
    public ArrayList<Angkot> getAllAngkot() {
        ArrayList<Angkot> angkotList = new ArrayList<Angkot>();
     
        // select all SQL query
        Cursor cursor = database.query(HelperAngkot.TABLE_NAME, allColumns, null, null, null, null, null);
     
        // pindah ke data paling pertama
        cursor.moveToFirst();
        // jika masih ada data, masukkan data barang ke daftar barang
        while (!cursor.isAfterLast()) {
        	Angkot user = cursorToAngkot(cursor);
        	angkotList.add(user);
        	cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return angkotList;
    }
    
    public boolean isAngkotExists() {
    	boolean exists = false;
    	// select all SQL query
        Cursor cursor = database.query(HelperAngkot.TABLE_NAME, allColumns, null, null, null, null, null);
        exists  = (cursor.getCount() > 0);
        cursor.close();
    	return exists;
    }

    public Angkot getAngkot(long id)
    {
    	Angkot angkot = new Angkot();
        //select query
        Cursor cursor = database.query(HelperAngkot.TABLE_NAME, allColumns, "id="+id, null, null, null, null);
        //ambil data yang pertama
        cursor.moveToFirst();
        angkot = cursorToAngkot(cursor);
        cursor.close();
        return angkot;
    }
 
    // update user yang diedit
    public void updateAngkot(Angkot angkot)
    {
        // ambil id user
        String strFilter = "id=" + angkot.getId();
        //memasukkan ke content values
        ContentValues args = new ContentValues();
        //masukkan data sesuai dengan kolom pada database
        args.put(HelperAngkot.COLUMN_NAME, angkot.getName());
        args.put(HelperAngkot.COLUMN_DESCRIPTION, angkot.getDescription());
        // update query
        database.update(HelperAngkot.TABLE_NAME, args, strFilter, null);
    }
    
    // delete User sesuai ID
    public void deleteAngkot(long id)
    {
        String strFilter = "id=" + id;
        database.delete(HelperAngkot.TABLE_NAME, strFilter, null);
    }
}
