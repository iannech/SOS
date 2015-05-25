package com.example.sos.data.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sos.data.DbSOS;
import com.example.sos.data.Table;
import com.example.sos.data.bean.Details;

public class TbDetails extends Table{
	
	public final static String COLUMN_NAME_ID = "detail_id";
	public final static String COLUMN_NAME_FIRSTNAME = "firstname";
	public final static String COLUMN_NAME_LASTTNAME = "lastname";
	public final static String COLUMN_NAME_DATEOFBIRTH = "dateofbirth";
	public final static String COLUMN_NAME_AGE = "age";
	public final static String COLUMN_NAME_GENDER = "gender";
	public final static String COLUMN_NAME_PHONE = "phone";
	
	private final String RECORD_ID = "1";
	
	
    // constructor
	public TbDetails(String tbName, DbSOS db){
		super(tbName,db);
	}

	@Override
	public void create(SQLiteDatabase db) {
		String sql = "CREATE TABLE "  + TB_NAME + " ( "+
	                  COLUMN_NAME_ID + " PRIMARY KEY "+ COMMA+
				      COLUMN_NAME_FIRSTNAME + TYPE_TEXT+ COMMA +
	                  COLUMN_NAME_LASTTNAME + TYPE_TEXT+ COMMA +
				      COLUMN_NAME_DATEOFBIRTH+ TYPE_TEXT + COMMA +
	                  COLUMN_NAME_AGE+ TYPE_TEXT+ COMMA+
				      COLUMN_NAME_GENDER + TYPE_TEXT + COMMA+
	                  COLUMN_NAME_PHONE + TYPE_TEXT + " )";
	                
		 db.execSQL(sql);
	}

	// will be called only once
	public void add(Details data){
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, data.getId());
		values.put(COLUMN_NAME_FIRSTNAME, data.getFirstName());
		values.put(COLUMN_NAME_LASTTNAME, data.getLastName());
		values.put(COLUMN_NAME_DATEOFBIRTH, data.getDateOfBirth());
		values.put(COLUMN_NAME_AGE, data.getAge());
		values.put(COLUMN_NAME_GENDER, data.getGender());
		values.put(COLUMN_NAME_PHONE, data.getPhone());
		
		SQLiteDatabase db = this.db.getWritableDatabase();
		db.insert(TB_NAME, null, values);
		db.close();
	}
	
	public void set(Details data){
		data.setId(RECORD_ID);
		if(get() == null)
			add(data);
		else
			update(data);
	}
	
	public int update(Details data){
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, data.getId());
		values.put(COLUMN_NAME_FIRSTNAME, data.getFirstName());
		values.put(COLUMN_NAME_LASTTNAME, data.getLastName());
		values.put(COLUMN_NAME_DATEOFBIRTH, data.getDateOfBirth());
		values.put(COLUMN_NAME_AGE, data.getAge());
		values.put(COLUMN_NAME_GENDER, data.getGender());
		values.put(COLUMN_NAME_PHONE, data.getPhone());
		
		SQLiteDatabase db = this.db.getWritableDatabase();
		
		//updating row
		return db.update(TB_NAME, values, COLUMN_NAME_ID + "=?",
				new String[] {data.getId()});
	}
	
	public Details get(){		
		SQLiteDatabase db = this.db.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM "+ TB_NAME + " WHERE detail_id = ?", new String[]{RECORD_ID});
		//INCOMPLETE
		Details data = null;
		
		if (cursor.getCount()>0){
	        cursor.moveToFirst();
	        
	        data = new Details();
	        
	        data.setId(cursor.getString(0));
	        data.setFirstName(cursor.getString(1));
	        data.setLastName(cursor.getString(2));
	        data.setDateOfBirth(cursor.getString(3));
	        data.setAge(cursor.getString(4));
	        data.setGender(cursor.getString(5));
	        data.setPhone(cursor.getString(6));
	    }
		return data;
	}
	
	
	public void delete() {
		String id = "1";
	    SQLiteDatabase db = this.db.getWritableDatabase();
	    db.delete(TB_NAME, COLUMN_NAME_ID + " = ?",
	            new String[] { id });
	    db.close();
	}	

}
