package de.fhb.mobile.ToDoListAndroidApp.persistance;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.fhb.mobile.ToDoListAndroidApp.commons.DateHelper;
import de.fhb.mobile.ToDoListAndroidApp.commons.ListHelper;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoDatabase {
	
	public static final String KEY_TODO = "todo";
    private Context context;
    private SQLiteDatabase database;
    private TodoDatabaseHelper oh ;

    public TodoDatabase(Context context) {
        this.context = context;
    }
    
    public TodoDatabase open() throws SQLException {
    	this.oh = new TodoDatabaseHelper(this.context);
        this.database = oh.getWritableDatabase();
		return this;
	}

    public void close()
    {
        database.close();
        oh.close();
        database= null;
        oh= null;
        SQLiteDatabase.releaseMemory();
    }
    
    public long createTodo(Todo todo) throws CreateException{
    	Log.i(this.getClass().toString(), "new Todo:" +todo);
    	String name = todo.getName();
    	String description = todo.getDescription();
    	boolean finished = todo.isFinished();
    	boolean favorite = todo.isFavorite();
    	Calendar cal = todo.getExpireDate();
    	List<Long> contacts = todo.getContacts();
    	
    	ContentValues values = new ContentValues();
    	if(name==null)
    		throw new CreateException("No Name added");
    	else
    		values.put(TodoTable.KEY_NAME, name);
		
		values.put(TodoTable.KEY_DESCRIPTION, (description==null) ? "": description);
		values.put(TodoTable.KEY_FINISHED, finished);
		values.put(TodoTable.KEY_FAVORITE, favorite);
		values.put(TodoTable.KEY_EXPIREDATE, cal.getTime().toString());
		values.put(TodoTable.KEY_CONTACTS, (contacts==null) ? "": ListHelper.listToString(contacts));
		
		return database.insert(TodoTable.TABLE_TODO, null, values);
    }
    
    public Cursor fetchAllTodos() {
		Log.i(this.getClass().toString(), "fetchAllTodos");
		
		return database.query(TodoTable.TABLE_TODO, null , null, null, null, null,
				TodoTable.KEY_ID + " DESC");
	}
    
    public Todo getTodoById(long id){
    	Log.i(this.getClass().toString(), "getTodoById(" +id +")");
    	
    	final int idIndex;
    	final int todoNameIndex;
    	final int favoriteIndex;
    	final int finishedIndex;
    	final int expiredateIndex;
    	final int descriptionIndex;
    	final int contactsIndex;
    	
    	String where = TodoTable.KEY_ID +"=" +id;
		Cursor cursor = database.query(TodoTable.TABLE_TODO, null, where , null, null,
				null, null);
		cursor.moveToFirst();
	    idIndex = cursor.getColumnIndex(TodoTable.KEY_ID);
	    todoNameIndex = cursor.getColumnIndex(TodoTable.KEY_NAME);
	    descriptionIndex = cursor.getColumnIndex(TodoTable.KEY_DESCRIPTION);
	    finishedIndex = cursor.getColumnIndex(TodoTable.KEY_FINISHED);
	    favoriteIndex = cursor.getColumnIndex(TodoTable.KEY_FAVORITE);
	    expiredateIndex = cursor.getColumnIndex(TodoTable.KEY_EXPIREDATE);
	    contactsIndex = cursor.getColumnIndex(TodoTable.KEY_CONTACTS);
	    
    	Todo returnTodo = new Todo();
    	returnTodo.setId(cursor.getLong(idIndex));
    	returnTodo.setName(cursor.getString(todoNameIndex));
    	returnTodo.setDescription(cursor.getString(descriptionIndex));
    	returnTodo.setFavorite((cursor.getInt(favoriteIndex)>0 ? true : false));
    	returnTodo.setFinished((cursor.getInt(finishedIndex)>0 ? true : false));
    	returnTodo.setExpireDate(DateHelper.getCalendarByString((cursor.getString(expiredateIndex))));
    	String contacts = cursor.getString(contactsIndex);
    	returnTodo.setContacts(ListHelper.stringToList(contacts));
		return returnTodo;
    }
}
