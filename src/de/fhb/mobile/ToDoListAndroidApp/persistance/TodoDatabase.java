package de.fhb.mobile.ToDoListAndroidApp.persistance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.fhb.mobile.ToDoListAndroidApp.commons.DateHelper;
import de.fhb.mobile.ToDoListAndroidApp.commons.ListHelper;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;

public class TodoDatabase {
	
    private Context context;
    private SQLiteDatabase database;
    private TodoDatabaseHelper oh ;

    public TodoDatabase(Context context) {
        this.context = context;
    }
    
    public TodoDatabase open() throws SQLException {
    	Log.i(this.getClass().toString(), "open DB");
    	this.oh = new TodoDatabaseHelper(this.context);
        this.database = oh.getWritableDatabase();
		return this;
	}

    public void close(){
    	Log.i(this.getClass().toString(), "close DB");
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
    	open();
    	
    	ContentValues values = new ContentValues();
    	if(name==null)
    		throw new CreateException("Name needed");
    	else
    		values.put(TodoTable.KEY_NAME, name);
		
		values.put(TodoTable.KEY_DESCRIPTION, (description==null) ? "": description);
		values.put(TodoTable.KEY_FINISHED, finished);
		values.put(TodoTable.KEY_FAVORITE, favorite);
		values.put(TodoTable.KEY_EXPIREDATE, (cal==null) ? 0 : cal.getTime().getTime());
		values.put(TodoTable.KEY_CONTACTS, (contacts==null) ? "": ListHelper.listToString(contacts));
		
		long rowid = database.insert(TodoTable.TABLE_TODO, null, values);
		close();
		return rowid;
    }
    
    public List<Todo> fetchAllTodos(String sortby) {
		Log.i(this.getClass().toString(), "fetchAllTodos");
    	final int idIndex;
    	final int todoNameIndex;
    	final int favoriteIndex;
    	final int finishedIndex;
    	final int expiredateIndex;
    	final int descriptionIndex;
    	final int contactsIndex;
    	List<Todo> list = new ArrayList<Todo>();
    	open();
		Cursor cursor = database.query(TodoTable.TABLE_TODO, null , null, null, null, null,
				sortby);
		idIndex = cursor.getColumnIndex(TodoTable.KEY_ID);
	    todoNameIndex = cursor.getColumnIndex(TodoTable.KEY_NAME);
	    descriptionIndex = cursor.getColumnIndex(TodoTable.KEY_DESCRIPTION);
	    finishedIndex = cursor.getColumnIndex(TodoTable.KEY_FINISHED);
	    favoriteIndex = cursor.getColumnIndex(TodoTable.KEY_FAVORITE);
	    expiredateIndex = cursor.getColumnIndex(TodoTable.KEY_EXPIREDATE);
	    contactsIndex = cursor.getColumnIndex(TodoTable.KEY_CONTACTS);
	    while(cursor.moveToNext()){
	    	Todo returnTodo = new Todo();
	    	returnTodo.setId(cursor.getLong(idIndex));
	    	returnTodo.setName(cursor.getString(todoNameIndex));
	    	returnTodo.setDescription(cursor.getString(descriptionIndex));
	    	returnTodo.setFavorite((cursor.getInt(favoriteIndex)>0 ? true : false));
	    	returnTodo.setFinished((cursor.getInt(finishedIndex)>0 ? true : false));
	    	long expiredate = cursor.getLong(expiredateIndex);
	    	if(expiredate == 0)
	    		returnTodo.setExpireDate(null);
	    	else
	    		returnTodo.setExpireDate(DateHelper.getCalendarByLong(expiredate));
	    	String contacts = cursor.getString(contactsIndex);
	    	returnTodo.setContacts(ListHelper.stringToList(contacts));
	    	list.add(returnTodo);
	    }
	    cursor.close();
	    close();
	    return list;
	}
    
    public int updateTodo(Todo todo) throws UpdateException{
    	Log.i(this.getClass().toString(), "new Todo: " +todo);
    	open();
    	String name = todo.getName();
    	String description = todo.getDescription();
    	boolean finished = todo.isFinished();
    	boolean favorite = todo.isFavorite();
    	Calendar cal = todo.getExpireDate();
    	List<Long> contacts = todo.getContacts();
    	
    	ContentValues values = new ContentValues();
    	if(name==null || name.isEmpty())
    		throw new UpdateException("Name needed");
    	else
    		values.put(TodoTable.KEY_NAME, name);
		
		values.put(TodoTable.KEY_DESCRIPTION, (description==null) ? "": description);
		values.put(TodoTable.KEY_FINISHED, finished);
		values.put(TodoTable.KEY_FAVORITE, favorite);
		values.put(TodoTable.KEY_EXPIREDATE, (cal==null) ? 0 : cal.getTime().getTime());
		values.put(TodoTable.KEY_CONTACTS, (contacts==null) ? "": ListHelper.listToString(contacts));
    	
    	int rowsUpdated = database.update(TodoTable.TABLE_TODO, values, TodoTable.KEY_ID +"=?", new String[]{String.valueOf(todo.getId())});
    	close();
    	return rowsUpdated;
    }
    
    public int updateBooleanColumns(long id, String column, boolean isChecked){
    	Log.i(this.getClass().toString(), "updateBooleanColumns: " +id + " - "+column +" - " +isChecked);
    	open();
    	ContentValues values = new ContentValues();
    	
    	if(column == TodoTable.KEY_FINISHED)
    		values.put(TodoTable.KEY_FINISHED, isChecked);
    	if(column == TodoTable.KEY_FAVORITE)
    		values.put(TodoTable.KEY_FAVORITE, isChecked);
    	
    	int rowsUpdated = database.update(TodoTable.TABLE_TODO, values, TodoTable.KEY_ID +"=?", new String[]{String.valueOf(id)});
    	close();
    	return rowsUpdated;
    }
    
    public Todo getTodoById(long id){
    	Log.i(this.getClass().toString(), "getTodoById(" +id +")");
    	open();
    	final int idIndex;
    	final int todoNameIndex;
    	final int favoriteIndex;
    	final int finishedIndex;
    	final int expiredateIndex;
    	final int descriptionIndex;
    	final int contactsIndex;
    	
		Cursor cursor = database.query(TodoTable.TABLE_TODO, null, TodoTable.KEY_ID +"=?", new String[]{String.valueOf(id)}, null,
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
    	long expiredate = cursor.getLong(expiredateIndex);
    	if(expiredate==0)
    		returnTodo.setExpireDate(null);
    	else
    		returnTodo.setExpireDate(DateHelper.getCalendarByLong(expiredate));
    	String contacts = cursor.getString(contactsIndex);
    	returnTodo.setContacts(ListHelper.stringToList(contacts));
    	cursor.close();
    	close();
		return returnTodo;
    }

	public int deleteTodo(long id) {
		Log.i(this.getClass().toString(), "deleteTodo: " +id);
		open();
		int rowsDeleted = database.delete(TodoTable.TABLE_TODO, TodoTable.KEY_ID +"=?", new String[]{String.valueOf(id)});
		close();
		return rowsDeleted;
	}
}
