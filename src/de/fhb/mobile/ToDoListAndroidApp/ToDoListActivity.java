package de.fhb.mobile.ToDoListAndroidApp;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.fhb.mobile.ToDoListAndroidApp.R;
import de.fhb.mobile.ToDoListAndroidApp.commons.DateHelper;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import de.fhb.mobile.ToDoListAndroidApp.persistance.CreateException;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoDatabase;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoTable;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoListActivity extends ListActivity {
	
	public static final int REQUEST_CODE_TODODETAILS = 1;
	public static final String ARG_TODO_ID = "todoObjectID";
	private TodoDatabase db;
	private List<Todo> todos;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);

        db = new TodoDatabase(this);
		db.open();
        // Zugriff auf die Listenansicht
        ListView listview = (ListView)findViewById(android.R.id.list);
        initTodos();

        Cursor cursorTodos = db.fetchAllTodos();
         
        // Define the columns of the table
 		// which should be used in the ListView
 		String[] from = new String[] {  TodoTable.KEY_ID, TodoTable.KEY_FAVORITE, TodoTable.KEY_FINISHED, TodoTable.KEY_NAME, TodoTable.KEY_EXPIREDATE};
 		// Define the view elements to which the
 		// columns will be mapped
 		int[] to = new int[] { R.id.hiddenId, R.id.favoriteCheckbox, R.id.finishedCheckbox, R.id.todoNameText, R.id.expiredateText };
         
        SimpleCursorAdapter sadapter = new TodoAdapter(this,
				R.layout.listelement, cursorTodos, from, to);
        // setze den Adapter auf die Listenansicht
        this.setListAdapter(sadapter);
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
    	Log.i(this.getClass().toString(), "onListItemClick: Todo_id " + id);
    	
    	Intent intent = new Intent(ToDoListActivity.this,
				ToDoDetailsActivity.class);
		// pass the item to the intent
		intent.putExtra(ARG_TODO_ID, id);

		// start the details activity with the intent
		startActivityForResult(intent, REQUEST_CODE_TODODETAILS);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem allTodosItem = menu.findItem(R.id.all_todos);
		allTodosItem.setEnabled(false);
		return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.all_todos:
        	//TODO
            return true;
        case R.id.contacts:
        	//TODO
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private class TodoAdapter extends SimpleCursorAdapter {
    	
    	private final Context mContext;
    	private final int mLayout;
    	private final Cursor mCursor;
    	private final int hiddenIdIndex;
    	private final int todoNameIndex;
    	private final int favoriteIndex;
    	private final int finishedIndex;
    	private final int expiredateIndex;
    	private final LayoutInflater mLayoutInflater;

    	private final class ViewHolder {
    		public TextView hiddenID;
    		public CheckBox finishedCheckBox;
    		public CheckBox favoriteCheckBox;
    	    public TextView todoName;
    	    public TextView expiredate;
    	}


		public TodoAdapter(Context context, int layout, Cursor c, String[] from, 
    			 int[] to) {
    		super(context, layout, c, from, to);
    		this.mContext = context;
    	    this.mLayout = layout;
    	    this.mCursor = c;
    	    this.hiddenIdIndex = mCursor.getColumnIndex(TodoTable.KEY_ID);
    	    this.todoNameIndex = mCursor.getColumnIndex(TodoTable.KEY_NAME);
    	    this.finishedIndex = mCursor.getColumnIndex(TodoTable.KEY_FINISHED);
    	    this.favoriteIndex = mCursor.getColumnIndex(TodoTable.KEY_FAVORITE);
    	    this.expiredateIndex = mCursor.getColumnIndex(TodoTable.KEY_EXPIREDATE);
    	    this.mLayoutInflater = LayoutInflater.from(mContext);

    	}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i(this.getClass().toString(), "getView");
		    if (mCursor.moveToPosition(position)) {
		        ViewHolder viewHolder;

		        if (convertView == null) {
		            convertView = mLayoutInflater.inflate(mLayout, null);

		            viewHolder = new ViewHolder();
		            viewHolder.hiddenID = (TextView) convertView.findViewById(R.id.hiddenId);
		            viewHolder.finishedCheckBox = (CheckBox) convertView.findViewById(R.id.finishedCheckbox);
		            viewHolder.favoriteCheckBox = (CheckBox) convertView.findViewById(R.id.favoriteCheckbox);
		            viewHolder.todoName = (TextView) convertView.findViewById(R.id.todoNameText);
		            viewHolder.expiredate = (TextView) convertView.findViewById(R.id.expiredateText);
		            convertView.setTag(viewHolder);
		        }
		        else {
		            viewHolder = (ViewHolder) convertView.getTag();
		        }
		        String id = mCursor.getString(hiddenIdIndex);
		        boolean finished = (mCursor.getInt(finishedIndex) > 0) ? true : false;
		        boolean favorite = (mCursor.getInt(favoriteIndex) > 0) ? true : false;
		        String name = mCursor.getString(todoNameIndex);
		        String expiredate = DateHelper.getDateAsString(DateHelper.getCalendarByString((mCursor.getString(expiredateIndex))));

		        //boolean isChecked = ((GlobalVars) mContext.getApplicationContext()).isFriendSelected(fb_id);

		        viewHolder.hiddenID.setText(id);
		        viewHolder.finishedCheckBox.setChecked(finished);
		        viewHolder.favoriteCheckBox.setChecked(favorite);
		        viewHolder.todoName.setText(name);
		        viewHolder.expiredate.setText(expiredate);
		    }
		    return convertView;
		}

    	/*
    	@Override
		public View getView(final int position, View listItemView, ViewGroup parent) {
    		LayoutInflater vi = (LayoutInflater) ToDoListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View v = vi.inflate(R.layout.listelement, null);
    		
    		TextView txt = (TextView)v.findViewById(R.id.todoNameText);
    		txt.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				Log.i(this.getClass().toString(), "onClick");
    				Toast.makeText(ToDoListActivity.this, "Todo " + position, Toast.LENGTH_LONG).show();
    				Intent intent = new Intent(ToDoListActivity.this, ToDoDetailsActivity.class);
    				// pass the item to the intent
    				intent.putExtra(ARG_TODO_OBJECT, item);
    				// also specify the accessor class
    				intent.putExtra(DataAccessActivity.ARG_ACCESSOR_CLASS,
    						IntentDataItemAccessorImpl.class.getName());
    				startActivityForResult(intent, REQUEST_CODE_TODODETAILS);
    				
    			}
    		});
    		return v;
    	}*/
    }
    
    private void initTodos(){
    	Todo newtodo = new Todo();
    	newtodo.setName("testCheckbox");
    	newtodo.setFinished(false);
    	newtodo.setFavorite(false);
    	newtodo.setExpireDate(GregorianCalendar.getInstance());
    	try {
			db.createTodo(newtodo);
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}