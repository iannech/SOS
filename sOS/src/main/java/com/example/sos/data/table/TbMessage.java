package com.example.sos.data.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sos.data.DbSOS;
import com.example.sos.data.Table;
import com.example.sos.data.bean.Message;

public class TbMessage extends Table {

	public final static String COLUMN_NAME_ID = "message_id";
	public final static String COLUMN_NAME_MESSAGE = "message";

	private final String RECORD_ID = "1";

	public TbMessage(String tbName, DbSOS db) {
		super(tbName, db);
	}

	@Override
	public void create(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TB_NAME + " ( " + COLUMN_NAME_ID 
				+ " PRIMARY KEY " + COMMA + COLUMN_NAME_MESSAGE + TYPE_TEXT + " )";

		db.execSQL(sql);

	}

	public void add(Message data) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, data.getId());
		values.put(COLUMN_NAME_MESSAGE, data.getMessage());

		SQLiteDatabase db = this.db.getWritableDatabase();
		db.insert(TB_NAME, null, values);
		db.close();
	}

	public void set(Message data) {
		data.setId(RECORD_ID);
		if (get() == null)
			add(data);
		else
			update(data);
	}

	public int update(Message data) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, RECORD_ID);
		values.put(COLUMN_NAME_MESSAGE, data.getMessage());

		SQLiteDatabase db = this.db.getWritableDatabase();

		// updating row
		return db.update(TB_NAME, values, COLUMN_NAME_ID + " = ?",
				new String[] { data.getId() });

	}

	public Message get() {
		SQLiteDatabase db = this.db.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TB_NAME
				+ " WHERE message_id = ?", new String[] { RECORD_ID });

		Message data = null;

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			data = new Message();

			data.setId(cursor.getString(0));
			data.setMessage(cursor.getString(1));

		}

		return data;
	}

	public void delete(String id) {
		SQLiteDatabase db = this.db.getWritableDatabase();
		db.delete(TB_NAME, COLUMN_NAME_ID + " = ?", new String[] { id });
		db.close();
	}

	public boolean exists(String id) {
		SQLiteDatabase db = this.db.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TB_NAME
				+ " WHERE contact_id = ?", new String[] { id });

		if (cursor.moveToFirst()) {
			return true;
		}

		return false;
	}

}

