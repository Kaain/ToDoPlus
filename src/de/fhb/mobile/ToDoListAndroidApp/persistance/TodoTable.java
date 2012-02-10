package de.fhb.mobile.ToDoListAndroidApp.persistance;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The Class TodoTable.
 */
public class TodoTable {

	/** The Constant TABLE_TODO. */
	public static final String TABLE_TODO = "todo";

	/** The Constant KEY_ID. */
	public static final String KEY_ID = "_id";

	/** The Constant KEY_NAME. */
	public static final String KEY_NAME = "name";

	/** The Constant KEY_DESCRIPTION. */
	public static final String KEY_DESCRIPTION = "description";

	/** The Constant KEY_FINISHED. */
	public static final String KEY_FINISHED = "finished";

	/** The Constant KEY_FAVORITE. */
	public static final String KEY_FAVORITE = "favorite";

	/** The Constant KEY_EXPIREDATE. */
	public static final String KEY_EXPIREDATE = "expiredate";

	/** The Constant DATABASE_CREATE_TODO. */
	private static final String DATABASE_CREATE_TODO = "CREATE TABLE IF  NOT EXISTS "
			+ TABLE_TODO
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_NAME
			+ " TEXT, "
			+ KEY_DESCRIPTION
			+ " TEXT, "
			+ KEY_FINISHED
			+ " INTEGER, "
			+ KEY_FAVORITE + " INTEGER, " + KEY_EXPIREDATE + " INTEGER "
			// + KEY_CONTACTS + " TEXT"
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
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
		onCreate(database);
	}
}
