package de.fhb.mobile.ToDoListAndroidApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import de.fhb.mobile.ToDoListAndroidApp.persistance.CreateExeption;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoDatabase;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoTable;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class TestDatabaseActivity extends ListActivity {
	private TodoDatabase db;
	private List<Todo> todos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Define the columns of the table
		// which should be used in the ListView
		String[] from = new String[] { TodoTable.KEY_NAME };
		// Define the view elements to which the
		// columns will be mapped
		int[] to = new int[] { android.R.id.text1 };
		db = new TodoDatabase(this);
		db.open();

		// Create a new comment every time the activity is started
		todos = new ArrayList<Todo>();
    	for(int i=0;i<11;i++){
    		long id = i;
			String name = "name "+i;
			String description = "description " + i;
			boolean finished = true;
			boolean favorite = true;
			Calendar expireDate = Calendar.getInstance();
			String contacts = "contacts " + i;
			Todo newTodo = new Todo(id, name, description, finished, favorite, expireDate, contacts);
    		todos.add(newTodo);
    		try {
				db.createTodo(todos.get(i));
			} catch (CreateExeption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//Log.i("", "after db.createTodo(todos.get(1));");
    	}
		// Save the new comment to the database
		
		// Read all comments
		Cursor c = db.fetchAllTodos();

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, c, from, to);

		setListAdapter(adapter);
	}

	@Override
	protected void onPause() {
		db.close();
		super.onPause();
	}

}