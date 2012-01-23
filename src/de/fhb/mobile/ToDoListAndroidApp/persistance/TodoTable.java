package de.fhb.mobile.ToDoListAndroidApp.persistance;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TodoTable {
	public static final String TABLE_TODO = "todo";
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_FINISHED = "finished";
	public static final String KEY_FAVORITE = "favorite";
	public static final String KEY_EXPIREDATE = "expiredate";
	public static final String KEY_CONTACTS = "contacts";
	private static final String DATABASE_CREATE_TODO = "CREATE TABLE IF  NOT EXISTS "
			+ TABLE_TODO
			+ "("
			+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_NAME +" TEXT, "
			+ KEY_DESCRIPTION +	" TEXT, "
			+ KEY_FINISHED + " INTEGER, "
			+ KEY_FAVORITE + " INTEGER, "
			+ KEY_EXPIREDATE + " INTEGER, " 
			+ KEY_CONTACTS + " TEXT" 
			+ ");";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_TODO);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(TodoTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
		onCreate(database);
	}
}
