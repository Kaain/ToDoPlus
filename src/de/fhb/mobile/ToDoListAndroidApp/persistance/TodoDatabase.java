package de.fhb.mobile.ToDoListAndroidApp.persistance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.fhb.mobile.ToDoListAndroidApp.commons.DateHelper;
import de.fhb.mobile.ToDoListAndroidApp.exceptions.CreateException;
import de.fhb.mobile.ToDoListAndroidApp.exceptions.OneConnectToServerException;
import de.fhb.mobile.ToDoListAndroidApp.exceptions.UpdateException;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import de.fhb.mobile.ToDoListAndroidApp.models.User;

/**
 * The Class TodoDatabase.
 */
public class TodoDatabase {

	/** The context. */
	private Context context;

	/** The database. */
	private SQLiteDatabase database;

	/** The oh. */
	private TodoDatabaseHelper oh;

	/**
	 * Instantiates a new todo database.
	 * 
	 * @param context
	 *            the context
	 */
	public TodoDatabase(Context context) {
		this.context = context;
	}
	
	public TodoDatabase() {
	}

	/**
	 * Closes the database.
	 */
	public void close() {
		Log.i(this.getClass().toString(), "close DB");
		database.close();
		oh.close();
		database = null;
		oh = null;
		SQLiteDatabase.releaseMemory();
	}
	
	public List<Todo> synchronize(List<Todo> todoList) throws UpdateException, CreateException{

			for (Todo t : todoList) {
				Todo databaseTodo = this.getTodoById(t.getId());
				System.out.println("mal sehen");
				if (databaseTodo.getId() == 0) {
					this.createTodo(t);
				} else {
					if (databaseTodo.getLastUpdated() <= t
							.getLastUpdated()) {
						this.updateTodo(t);
					} else {
						t = databaseTodo;
					}
				}
			}
			return todoList;
	}

	/**
	 * Creates the todo.
	 * 
	 * @param todo
	 *            the todo
	 * @return the long
	 * @throws CreateException
	 *             the create exception
	 */
	public long createTodo(Todo todo) throws CreateException {
		Log.i(this.getClass().toString(), "new Todo:" + todo);
		String name = todo.getName();
		String description = todo.getDescription();
		boolean finished = todo.isFinished();
		boolean favorite = todo.isFavorite();
		Calendar cal = todo.getExpireDate();
		List<Long> contacts = todo.getContacts();

		ContentValues values = new ContentValues();
		if (name == null)
			throw new CreateException("Name needed");
		else
			values.put(TodoTable.KEY_NAME, name);

		values.put(TodoTable.KEY_DESCRIPTION, (description == null) ? ""
				: description);
		values.put(TodoTable.KEY_FINISHED, finished);
		values.put(TodoTable.KEY_FAVORITE, favorite);
		values.put(TodoTable.KEY_EXPIREDATE, (cal == null) ? 0 : cal.getTime()
				.getTime());
		values.put(TodoTable.KEY_LASTUPDATED, DateHelper.getActualTime());

		long rowid = database.insert(TodoTable.TABLE_TODO, null, values);
		createTodoToContact(rowid, contacts);

		return rowid;
	}

	/**
	 * Creates the todo to contact.
	 * 
	 * @param todoId
	 *            the todo id
	 * @param contactsId
	 *            the contacts id
	 */
	private void createTodoToContact(long todoId, List<Long> contactsId) {
		ContentValues values = new ContentValues();
		for (int i = 0; i < contactsId.size(); i++) {
			values.put(TodoToContactTable.KEY_TODOID, todoId);
			values.put(TodoToContactTable.KEY_CONTACTID, contactsId.get(i));
			database.insert(TodoToContactTable.TABLE_TODOTOCONTACT, null,
					values);
		}
	}

	/**
	 * Delete all todo to contact.
	 * 
	 * @param todoId
	 *            the todo id
	 * @return the int
	 */
	private int deleteAllTodoToContact(long todoId) {
		Log.i(this.getClass().toString(), "deleteAllTodoToContact: todoid: "
				+ todoId);

		int rowsDeleted = database.delete(
				TodoToContactTable.TABLE_TODOTOCONTACT,
				TodoToContactTable.KEY_TODOID + "=?",
				new String[] { String.valueOf(todoId) });

		return rowsDeleted;
	}

	/**
	 * Delete todo.
	 * 
	 * @param id
	 *            the id
	 * @return rows deleted
	 */
	public int deleteTodo(long id) {
		Log.i(this.getClass().toString(), "deleteTodo: " + id);
		int rowsDeleted = database.delete(TodoTable.TABLE_TODO,
				TodoTable.KEY_ID + "=?", new String[] { String.valueOf(id) });
		deleteAllTodoToContact(id);
		return rowsDeleted;
	}

	/**
	 * Gets all contact ids.
	 * 
	 * @return all contacts
	 */
	public List<Long> getAllContacts() {
		List<Long> contacts = new ArrayList<Long>();
		String[] columns = { TodoToContactTable.KEY_CONTACTID };
		final int iContactID;

		Cursor cursor = database.query(TodoToContactTable.TABLE_TODOTOCONTACT,
				columns, null, null, TodoToContactTable.KEY_CONTACTID, null,
				null);

		iContactID = cursor.getColumnIndex(TodoToContactTable.KEY_CONTACTID);
		while (cursor.moveToNext()) {
			contacts.add(cursor.getLong(iContactID));
		}
		cursor.close();
		return contacts;
	}

	/**
	 * Gets all contact ids for the todo.
	 * 
	 * @param todoId
	 *            the todo id
	 * @return all contacts
	 */
	public List<Long> getAllContacts(long todoId) {
		List<Long> contacts = new ArrayList<Long>();
		String[] columns = { TodoToContactTable.KEY_CONTACTID };
		String selection = TodoToContactTable.KEY_TODOID + "=?";
		String[] selectionArgs = { todoId + "" };
		final int iContactID;

		Cursor cursor = database.query(TodoToContactTable.TABLE_TODOTOCONTACT,
				columns, selection, selectionArgs,
				TodoToContactTable.KEY_CONTACTID, null, null);

		iContactID = cursor.getColumnIndex(TodoToContactTable.KEY_CONTACTID);
		while (cursor.moveToNext()) {
			contacts.add(cursor.getLong(iContactID));
		}
		cursor.close();
		return contacts;
	}

	/**
	 * Gets all todos.
	 * 
	 * @param sortby
	 *            the sortby
	 * @return all todos
	 */
	public List<Todo> getAllTodos(String sortby) {
		Log.i(this.getClass().toString(), "getAllTodos");
		final int idIndex;
		final int todoNameIndex;
		final int favoriteIndex;
		final int finishedIndex;
		final int expiredateIndex;
		final int descriptionIndex;
		final int lastupdatedIndex;
		List<Todo> list = new ArrayList<Todo>();

		Cursor cursor = database.query(TodoTable.TABLE_TODO, null, null, null,
				null, null, sortby);

		idIndex = cursor.getColumnIndex(TodoTable.KEY_ID);
		todoNameIndex = cursor.getColumnIndex(TodoTable.KEY_NAME);
		descriptionIndex = cursor.getColumnIndex(TodoTable.KEY_DESCRIPTION);
		finishedIndex = cursor.getColumnIndex(TodoTable.KEY_FINISHED);
		favoriteIndex = cursor.getColumnIndex(TodoTable.KEY_FAVORITE);
		expiredateIndex = cursor.getColumnIndex(TodoTable.KEY_EXPIREDATE);
		lastupdatedIndex = cursor.getColumnIndex(TodoTable.KEY_LASTUPDATED);

		while (cursor.moveToNext()) {
			long id = cursor.getLong(idIndex);
			Todo returnTodo = new Todo();
			returnTodo.setId(id);
			returnTodo.setName(cursor.getString(todoNameIndex));
			returnTodo.setDescription(cursor.getString(descriptionIndex));
			returnTodo.setFavorite((cursor.getInt(favoriteIndex) > 0 ? true
					: false));
			returnTodo.setFinished((cursor.getInt(finishedIndex) > 0 ? true
					: false));
			long expiredate = cursor.getLong(expiredateIndex);
			if (expiredate == 0)
				returnTodo.setExpireDate(null);
			else
				returnTodo.setExpireDate(DateHelper
						.getCalendarByLong(expiredate));

			returnTodo.setContacts(getAllContacts(id));
			returnTodo.setLastUpdated(cursor.getLong(lastupdatedIndex));
			list.add(returnTodo);
		}
		cursor.close();

		return list;
	}

	/**
	 * Gets the all todos for contact.
	 * 
	 * @param sortby
	 *            the sortby
	 * @param contactId
	 *            the contact id
	 * @return the all todos for contact
	 */
	public List<Todo> getAllTodosForContact(String sortby, long contactId) {
		Log.i(this.getClass().toString(), "getAllTodosForContact");
		final int idIndex;
		final int todoNameIndex;
		final int favoriteIndex;
		final int finishedIndex;
		final int expiredateIndex;
		final int descriptionIndex;
		List<Todo> list = new ArrayList<Todo>();
		String where = TodoToContactTable.KEY_CONTACTID + "=" + contactId;
		Cursor cursor = database.query(TodoViews.VIEW_TODO_AND_CONTACT, null,
				where, null, null, null, sortby);

		idIndex = cursor.getColumnIndex(TodoTable.KEY_ID);
		todoNameIndex = cursor.getColumnIndex(TodoTable.KEY_NAME);
		descriptionIndex = cursor.getColumnIndex(TodoTable.KEY_DESCRIPTION);
		finishedIndex = cursor.getColumnIndex(TodoTable.KEY_FINISHED);
		favoriteIndex = cursor.getColumnIndex(TodoTable.KEY_FAVORITE);
		expiredateIndex = cursor.getColumnIndex(TodoTable.KEY_EXPIREDATE);

		while (cursor.moveToNext()) {
			long id = cursor.getLong(idIndex);
			Todo returnTodo = new Todo();
			returnTodo.setId(id);
			returnTodo.setName(cursor.getString(todoNameIndex));
			returnTodo.setDescription(cursor.getString(descriptionIndex));
			returnTodo.setFavorite((cursor.getInt(favoriteIndex) > 0 ? true
					: false));
			returnTodo.setFinished((cursor.getInt(finishedIndex) > 0 ? true
					: false));
			long expiredate = cursor.getLong(expiredateIndex);
			if (expiredate == 0)
				returnTodo.setExpireDate(null);
			else
				returnTodo.setExpireDate(DateHelper
						.getCalendarByLong(expiredate));

			list.add(returnTodo);
		}
		cursor.close();

		return list;
	}

	/**
	 * Gets the todo by id.
	 * 
	 * @param id
	 *            the id
	 * @return the todo by id
	 */
	public Todo getTodoById(long id) {
		Log.i(this.getClass().toString(), "getTodoById(" + id + ")");
		;
		final int idIndex;
		final int todoNameIndex;
		final int favoriteIndex;
		final int finishedIndex;
		final int expiredateIndex;
		final int descriptionIndex;

		Cursor cursor = database.query(TodoTable.TABLE_TODO, null,
				TodoTable.KEY_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null);
		if (cursor.moveToFirst()) {
			idIndex = cursor.getColumnIndex(TodoTable.KEY_ID);
			todoNameIndex = cursor.getColumnIndex(TodoTable.KEY_NAME);
			descriptionIndex = cursor.getColumnIndex(TodoTable.KEY_DESCRIPTION);
			finishedIndex = cursor.getColumnIndex(TodoTable.KEY_FINISHED);
			favoriteIndex = cursor.getColumnIndex(TodoTable.KEY_FAVORITE);
			expiredateIndex = cursor.getColumnIndex(TodoTable.KEY_EXPIREDATE);

			Todo returnTodo = new Todo();
			returnTodo.setId(cursor.getLong(idIndex));
			returnTodo.setName(cursor.getString(todoNameIndex));
			returnTodo.setDescription(cursor.getString(descriptionIndex));
			returnTodo.setFavorite((cursor.getInt(favoriteIndex) > 0 ? true
					: false));
			returnTodo.setFinished((cursor.getInt(finishedIndex) > 0 ? true
					: false));
			long expiredate = cursor.getLong(expiredateIndex);
			if (expiredate == 0)
				returnTodo.setExpireDate(null);
			else
				returnTodo.setExpireDate(DateHelper
						.getCalendarByLong(expiredate));
			returnTodo.setContacts(getAllContacts(id));
			cursor.close();
			return returnTodo;
		}else{
			cursor.close();
			return null;
		}
			
		

		
	}

	/**
	 * Opens the db.
	 * 
	 * @return the todo database
	 * @throws SQLException
	 *             the sQL exception
	 */
	public TodoDatabase open() throws SQLException {
		Log.i(this.getClass().toString(), "open DB");
		this.oh = new TodoDatabaseHelper(this.context);
		this.database = oh.getWritableDatabase();
		return this;
	}

	/**
	 * Update boolean columns.
	 * 
	 * @param id
	 *            the id
	 * @param column
	 *            the column
	 * @param isChecked
	 *            the is checked
	 * @return the int
	 */
	public int updateBooleanColumns(long id, String column, boolean isChecked) {
		Log.i(this.getClass().toString(), "updateBooleanColumns: " + id + " - "
				+ column + " - " + isChecked);

		ContentValues values = new ContentValues();

		if (column == TodoTable.KEY_FINISHED)
			values.put(TodoTable.KEY_FINISHED, isChecked);
		if (column == TodoTable.KEY_FAVORITE)
			values.put(TodoTable.KEY_FAVORITE, isChecked);

		values.put(TodoTable.KEY_LASTUPDATED, DateHelper.getActualTime());
		int rowsUpdated = database.update(TodoTable.TABLE_TODO, values,
				TodoTable.KEY_ID + "=?", new String[] { String.valueOf(id) });

		return rowsUpdated;
	}

	/**
	 * Update todo.
	 * 
	 * @param todo
	 *            the todo
	 * @return the int
	 * @throws UpdateException
	 *             the update exception
	 */
	public int updateTodo(Todo todo) throws UpdateException {
		Log.i(this.getClass().toString(), "new Todo: " + todo);

		long id = todo.getId();
		String name = todo.getName();
		String description = todo.getDescription();
		boolean finished = todo.isFinished();
		boolean favorite = todo.isFavorite();
		Calendar cal = todo.getExpireDate();
		List<Long> contacts = todo.getContacts();

		ContentValues values = new ContentValues();
		if (name == null || name.isEmpty())
			throw new UpdateException("Name needed");
		else
			values.put(TodoTable.KEY_NAME, name);

		values.put(TodoTable.KEY_DESCRIPTION, (description == null) ? ""
				: description);
		values.put(TodoTable.KEY_FINISHED, finished);
		values.put(TodoTable.KEY_FAVORITE, favorite);
		values.put(TodoTable.KEY_EXPIREDATE, (cal == null) ? 0 : cal.getTime()
				.getTime());
		values.put(TodoTable.KEY_LASTUPDATED, DateHelper.getActualTime());
		updateTodoToContact(id, contacts);
		int rowsUpdated = database.update(TodoTable.TABLE_TODO, values,
				TodoTable.KEY_ID + "=?", new String[] { String.valueOf(id) });

		return rowsUpdated;
	}

	/**
	 * Update todo to contact.
	 * 
	 * @param todoId
	 *            the todo id
	 * @param contactsId
	 *            the contacts id
	 */
	private void updateTodoToContact(long todoId, List<Long> contactsId) {
		Log.i(this.getClass().toString(), "UpdateTodoToContact: todoid:"
				+ todoId);

		deleteAllTodoToContact(todoId);
		createTodoToContact(todoId, contactsId);
	}
	
	/**
	 * Authenticate user.
	 * if there is no user in database, he will be created
	 *
	 * @param user the user
	 * @return true, if successful
	 */
	public boolean authenticateUser(User user) throws OneConnectToServerException{
		Cursor cursor = database.query(UserTable.TABLE_USER, null, null, null, null, null, null);
		if(cursor.moveToNext() && cursor.isFirst()){
			int iUsername = cursor.getColumnIndex(UserTable.KEY_USERNAME);
			int iPassword = cursor.getColumnIndex(UserTable.KEY_PASSWORD);
			int iOneConnectToServer = cursor.getColumnIndex(UserTable.KEY_ONECONNECTWITHSERVER);
			String username = cursor.getString(iUsername);
			String password = cursor.getString(iPassword);
			boolean oneConnectToServer = (cursor.getInt(iOneConnectToServer) == 1) ? true : false;
			
			if(!oneConnectToServer && !user.isOneConnectWithServer())
				throw new OneConnectToServerException("Need one connect to server!");
			else
				updateUser(true);
			
			if(user.getPassword().compareTo(password) == 0 && user.getUsername().compareTo(username) == 0)
				return true;
			else
				return false;
		}else{
			return false;
		}
		
	}
	
	public void createUser(User user){
		Cursor cursor = database.query(UserTable.TABLE_USER, null, null, null, null, null, null);
		if(!cursor.moveToNext()){
			ContentValues values = new ContentValues();
			values.put(UserTable.KEY_USERNAME, user.getUsername());
			values.put(UserTable.KEY_PASSWORD, user.getPassword());
			values.put(UserTable.KEY_ONECONNECTWITHSERVER, false);
			database.insert(UserTable.TABLE_USER, null, values);
		}
	}
	
	private void updateUser(boolean isOneConnectWithServer){
		ContentValues values = new ContentValues();
		values.put(UserTable.KEY_ONECONNECTWITHSERVER, isOneConnectWithServer);
		database.update(UserTable.TABLE_USER, values, null, null);
	}
}
