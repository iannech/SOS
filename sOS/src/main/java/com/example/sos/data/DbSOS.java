package com.example.sos.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sos.data.table.TbContact;
import com.example.sos.data.table.TbDetails;
import com.example.sos.data.table.TbMessage;

public class DbSOS extends SQLiteOpenHelper{
	private static final int DB_VERSION = 4;
	private final static String DB_NAME = "dbSOS";
	
	//Tables
	public TbContact contact = null;
	public TbDetails detail = null;
	public TbMessage message = null;

	/*
	 * Activity context*/
	public DbSOS(Context context) {
		super(context, DB_NAME, null, DB_VERSION);		
		contact = new TbContact("contacts", this);
		detail = new TbDetails("details", this);
		message = new TbMessage("message", this);
	}
	
    // Called once, when Database is being created
	@Override
	public void onCreate(SQLiteDatabase db) {		
		contact.create(db);
		detail.create(db);
		message.create(db);
	}
	
	public void reset(SQLiteDatabase db){
		delete(db);
		onCreate(db);
	}
	
	public void delete(SQLiteDatabase db){
		contact.delete(db);
		detail.delete(db);
		message.delete(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		reset(db);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}


}
