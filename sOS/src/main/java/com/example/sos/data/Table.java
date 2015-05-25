package com.example.sos.data;

import com.example.sos.data.DbSOS;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class Table {
	protected DbSOS db = null;
	protected final String TB_NAME;
	protected final String COMMA = ",";
	protected final String TYPE_TEXT = " TEXT ";
	protected final String TYPE_INT = " INTEGER ";	
	
	public Table(String tbName, DbSOS db) {
		TB_NAME = tbName;
		this.db = db;
	}
	
	
	public abstract void create(SQLiteDatabase db);
	
	//get total count
    public int getCount() {
        String countQuery = "SELECT  * FROM " + TB_NAME;
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
 
        // return count
        return count;
    }
	
    //Delete table
    public void delete(SQLiteDatabase db){    	
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
    }
	

}
