package de.fhb.mobile.ToDoListAndroidApp.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The Class TodoDatabaseHelper.
 */
public class TodoDatabaseHelper extends SQLiteOpenHelper {

	/** The Constant DATABASE_NAME. */
	private static final String DATABASE_NAME = "todos.db";

	/** The Constant DATABASE_VERSION. */
	private static final int DATABASE_VERSION = 1;

	/**
	 * Instantiates a new todo database helper.
	 * 
	 * @param context
	 *            the context
	 */
	public TodoDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		TodoTable.onCreate(database);
		TodoToContactTable.onCreate(database);
		UserTable.onCreate(database);
		TodoViews.createViewTodoAndContact(database);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		TodoTable.onUpgrade(database, oldVersion, newVersion);
		TodoToContactTable.onUpgrade(database, oldVersion, newVersion);
		UserTable.onCreate(database);
	}
}