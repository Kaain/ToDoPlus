package de.fhb.mobile.ToDoListAndroidApp.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todos.db";
	private static final int DATABASE_VERSION = 1;
	
	
    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        TodoTable.onCreate(database);
        TodoToContactTable.onCreate(database);
        TodoViews.createViewTodoAndContact(database);
    }

	@Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    	TodoTable.onUpgrade(database, oldVersion, newVersion);
    	TodoToContactTable.onUpgrade(database, oldVersion, newVersion);
    }
}