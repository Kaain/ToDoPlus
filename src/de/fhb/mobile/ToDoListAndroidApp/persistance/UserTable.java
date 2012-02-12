package de.fhb.mobile.ToDoListAndroidApp.persistance;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The Class TodoTable.
 */
public class UserTable {

	/** The Constant TABLE_TODO. */
	public static final String TABLE_USER = "user";

	/** The Constant KEY_ID. */
	public static final String KEY_ID = "_id";

	/** The Constant KEY_NAME. */
	public static final String KEY_USERNAME = "username";

	/** The Constant KEY_DESCRIPTION. */
	public static final String KEY_PASSWORD = "password";
	
	public static final String KEY_ONECONNECTWITHSERVER = "oneconnectwithserver";

	/** The Constant DATABASE_CREATE_TODO. */
	private static final String DATABASE_CREATE_TODO = "CREATE TABLE IF  NOT EXISTS "
			+ TABLE_USER
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_USERNAME
			+ " TEXT, "
			+ KEY_PASSWORD
			+ " TEXT, "
			+ KEY_ONECONNECTWITHSERVER
			+ " INTEGER"
			+ ");";

	/**
	 * On create.
	 * 
	 * @param database
	 *            the database
	 */
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_TODO);
	}

	/**
	 * On upgrade.
	 * 
	 * @param database
	 *            the database
	 * @param oldVersion
	 *            the old version
	 * @param newVersion
	 *            the new version
	 */
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(TodoTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		onCreate(database);
	}
}
