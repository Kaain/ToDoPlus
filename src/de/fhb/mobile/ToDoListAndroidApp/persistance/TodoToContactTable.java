package de.fhb.mobile.ToDoListAndroidApp.persistance;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The Class TodoToContactTable.
 */
public class TodoToContactTable {

	/** The Constant TABLE_TODOTOCONTACT. */
	public static final String TABLE_TODOTOCONTACT = "todoToContact";

	/** The Constant KEY_TODOTOCONTACTID. */
	public static final String KEY_TODOTOCONTACTID = "_todotocontactid";

	/** The Constant KEY_TODOID. */
	public static final String KEY_TODOID = "todoid";

	/** The Constant KEY_CONTACTID. */
	public static final String KEY_CONTACTID = "contactid";

	/** The Constant DATABASE_CREATE_TODO. */
	private static final String DATABASE_CREATE_TODO = "CREATE TABLE IF  NOT EXISTS "
			+ TABLE_TODOTOCONTACT
			+ "("
			+ KEY_TODOTOCONTACTID
			+ " INTEGER PRIMARY KEY, "
			+ KEY_TODOID
			+ " INTEGER, "
			+ KEY_CONTACTID + " INTEGER" + ");";

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
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOTOCONTACT);
		onCreate(database);
	}
}
