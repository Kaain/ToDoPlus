package de.fhb.mobile.ToDoListAndroidApp.persistance;

import android.database.sqlite.SQLiteDatabase;

public class TodoViews {
	
	public static final String VIEW_TODO_AND_CONTACT = "todocontact";
	public static final String TABLE_TODO = TodoTable.TABLE_TODO;
	public static final String TABLE_TODOTOCONTACT = TodoToContactTable.TABLE_TODOTOCONTACT;
	public static final String KEY_ID = TodoTable.KEY_ID;
	public static final String KEY_NAME = TodoTable.KEY_NAME;
	public static final String KEY_DESCRIPTION = TodoTable.KEY_DESCRIPTION;
	public static final String KEY_FINISHED = TodoTable.KEY_FINISHED;
	public static final String KEY_FAVORITE = TodoTable.KEY_FAVORITE;
	public static final String KEY_EXPIREDATE = TodoTable.KEY_EXPIREDATE;
	public static final String KEY_CONTACTID = TodoToContactTable.KEY_CONTACTID;
	public static final String KEY_TODOID = TodoToContactTable.KEY_TODOID;
	public static void createViewTodoAndContact(SQLiteDatabase database) {
		String query = "CREATE VIEW " + VIEW_TODO_AND_CONTACT + " AS SELECT "
				+ TABLE_TODO +"." +KEY_ID +" AS " +KEY_ID +" , "
				+ TABLE_TODO +"." +KEY_NAME +" AS " +KEY_NAME +" , "
				+ TABLE_TODO +"." +KEY_DESCRIPTION +" AS " +KEY_DESCRIPTION +" , "
				+ TABLE_TODO +"." +KEY_EXPIREDATE +" AS " +KEY_EXPIREDATE +" , "
				+ TABLE_TODO +"." +KEY_FAVORITE +" AS " +KEY_FAVORITE +" , "
				+ TABLE_TODO +"." +KEY_FINISHED +" AS " +KEY_FINISHED +" , "
				+ TABLE_TODOTOCONTACT +"." +KEY_CONTACTID +" AS " +KEY_CONTACTID
				+ " FROM " + TABLE_TODO +", "
				+ TABLE_TODOTOCONTACT
				+ " WHERE " + KEY_ID + " = " +KEY_TODOID;
		database.execSQL(query);
	}
}
