package com.example.sos.data.table;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.sos.data.DbSOS;
import com.example.sos.data.Table;
import com.example.sos.data.bean.Contact;

public class TbContact extends Table {
    public final static String COLUMN_NAME_ID = "contact_id";
    public final static String COLUMN_NAME_FULLNAME = "fullname";
    public final static String COLUMN_NAME_PHONE = "phone";


    public TbContact(String tbName, DbSOS db) {
        super(tbName, db);
    }

    @Override
    public void create(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TB_NAME + "( " + COLUMN_NAME_ID
                + " PRIMARY KEY " + COMMA + COLUMN_NAME_FULLNAME + TYPE_TEXT
                + COMMA + COLUMN_NAME_PHONE + TYPE_TEXT + " )";

        db.execSQL(sql);
    }

    public void add(Contact data) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID, data.getId());
        values.put(COLUMN_NAME_FULLNAME, data.getFullName());
        values.put(COLUMN_NAME_PHONE, data.getPhone());

        SQLiteDatabase db = this.db.getWritableDatabase();
        db.insert(TB_NAME, null, values);
        db.close();
    }

    public int update(Contact data) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID, data.getId());
        values.put(COLUMN_NAME_FULLNAME, data.getFullName());
        values.put(COLUMN_NAME_PHONE, data.getPhone());

        SQLiteDatabase db = this.db.getWritableDatabase();

        // updating row
        return db.update(TB_NAME, values, COLUMN_NAME_ID + " = ?",
                new String[]{data.getId()});
    }

    public Contact get(String id) {
        SQLiteDatabase db = this.db.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_NAME
                + " WHERE contact_id = ?", new String[]{id});
        Contact data = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            data = new Contact();
            data.setId(cursor.getString(0));
            data.setFullName(cursor.getString(1));
            data.setPhone(cursor.getString(2));
        }
        return data;
    }

    public ArrayList<Contact> getAll() {
        ArrayList<Contact> list = new ArrayList<Contact>();
        String selectQuery = "SELECT  * FROM " + TB_NAME;
        SQLiteDatabase db = this.db.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Contact data = new Contact();
                data.setId(cursor.getString(0));
                data.setFullName(cursor.getString(1));
                data.setPhone(cursor.getString(2));
                list.add(data);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void delete(String id) {
        SQLiteDatabase db = this.db.getWritableDatabase();
        db.delete(TB_NAME, COLUMN_NAME_ID + " = ?", new String[]{id});
        db.close();
    }

    public boolean exists(String value, String column) {
        SQLiteDatabase db = this.db.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_NAME
                + " WHERE " + column +" = ?", new String[]{value});
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

}
