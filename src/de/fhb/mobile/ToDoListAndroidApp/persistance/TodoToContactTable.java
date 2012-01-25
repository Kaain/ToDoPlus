package de.fhb.mobile.ToDoListAndroidApp.persistance;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TodoToContactTable {
	public static final String TABLE_TODOTOCONTACT = "todoToContact";
	public static final String KEY_TODOTOCONTACTID = "_todotocontactid";
	public static final String KEY_TODOID = "todoid";
	public static final String KEY_CONTACTID = "contactid";

	private static final String DATABASE_CREATE_TODO = "CREATE TABLE IF  NOT EXISTS "
			+ TABLE_TODOTOCONTACT
			+ "("
			+ KEY_TODOTOCONTACTID + " INTEGER PRIMARY KEY, "
			+ KEY_TODOID + " INTEGER, "
			+ KEY_CONTACTID +" INTEGER"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_TODO);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(TodoTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOTOCONTACT);
		onCreate(database);
	}
}
