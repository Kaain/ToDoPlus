package de.fhb.mobile.ToDoListAndroidApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import de.fhb.mobile.ToDoListAndroidApp.persistance.CreateException;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoDatabase;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoTable;

public class ToDoListActivity extends ListActivity {
	
	public static final int REQUEST_CODE_TODODETAILS = 1;
	public static final String ARG_TODO_ID = "todoObjectID";
	private TodoDatabase db;
	Button createTodoButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);
        Log.i(this.getClass().toString(), "onCreate");

        db = new TodoDatabase(this);
        
        createTodoButton = (Button)findViewById(R.id.newTodoButton);
        createTodoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	Intent intent = new Intent(ToDoListActivity.this,
						ToDoDetailsActivity.class);

				// start the details activity with the intent
				startActivityForResult(intent,REQUEST_CODE_TODODETAILS);
			}
		});
        //initTodos();
        initListAdapter();
    }
    
    private void initListAdapter(){
    	db = db.open();
    	List<Todo> list = db.fetchAllTodos();
    	db.close();
    	
    	ArrayAdapter<Todo> sadapter = new TodoAdapter(this,R.layout.listelement, list);
    	// setze den Adapter auf die Listenansicht
        this.setListAdapter(sadapter);
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
    	Intent intent = new Intent(ToDoListActivity.this,
				ToDoDetailsActivity.class);
    	Todo todo = (Todo)l.getItemAtPosition(position);
    	Log.i(this.getClass().toString(), "onListItemClick: Todo_id " + todo.getId());
		// pass the item to the intent
		intent.putExtra(ARG_TODO_ID, todo.getId());
		// start the details activity with the intent
		startActivityForResult(intent,REQUEST_CODE_TODODETAILS);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data){
    	Log.i(this.getClass().toString(), "onActivityResult");
		initListAdapter();
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.all_todos:
        	//TODO
            return true;
        case R.id.all_contacts_with_todos:
        	//TODO
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private class TodoAdapter extends ArrayAdapter<Todo> {
    	
    	private final Context mContext;
    	private final int mLayout;
    	private final LayoutInflater mLayoutInflater;
    	private List<Todo> list;

    	private final class ViewHolder {
    		public TextView hiddenID;
    		public CheckBox finishedCheckBox;
    		public CheckBox favoriteCheckBox;
    	    public TextView todoName;
    	    public TextView expiredate;
    	}


		public TodoAdapter(Context context, int layout, List<Todo> list) {
    		super(context, layout, list);
    		this.mContext = context;
    	    this.mLayout = layout;
    	    this.list = list;
    	    this.mLayoutInflater = LayoutInflater.from(mContext);
    	}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i(this.getClass().toString(), "getView");
			ViewHolder viewHolder;
			Todo todo = list.get(position);
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(mLayout, null);

				viewHolder = new ViewHolder();
				viewHolder.hiddenID = (TextView) convertView
						.findViewById(R.id.hiddenId);
				viewHolder.finishedCheckBox = (CheckBox) convertView
						.findViewById(R.id.finishedCheckbox);
				viewHolder.favoriteCheckBox = (CheckBox) convertView
						.findViewById(R.id.favoriteCheckbox);
				viewHolder.todoName = (TextView) convertView
						.findViewById(R.id.todoNameText);
				viewHolder.expiredate = (TextView) convertView
						.findViewById(R.id.expiredateText);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			long id = todo.getId();
			boolean finished = todo.isFinished();
			boolean favorite = todo.isFavorite();
			String name = todo.getName();
			Calendar expiredate = todo.getExpireDate();
			Calendar calNow = GregorianCalendar.getInstance();
			if (expiredate.compareTo(calNow) == -1)
				viewHolder.expiredate.setBackgroundColor(Color.MAGENTA);
			else
				viewHolder.expiredate
						.setBackgroundColor(android.R.color.transparent);
			
			viewHolder.hiddenID.setText(String.valueOf(id));
			viewHolder.finishedCheckBox.setOnCheckedChangeListener(null);
			viewHolder.finishedCheckBox.setChecked(finished);
			viewHolder.finishedCheckBox
					.setOnCheckedChangeListener(new UpdateDBOnCheckedChangeListener(id, TodoTable.KEY_FINISHED));
					
			viewHolder.favoriteCheckBox.setOnCheckedChangeListener(null);
			viewHolder.favoriteCheckBox.setChecked(favorite);
			viewHolder.favoriteCheckBox
					.setOnCheckedChangeListener(new UpdateDBOnCheckedChangeListener(id, TodoTable.KEY_FAVORITE));
					
			viewHolder.todoName.setText(name);
			viewHolder.expiredate.setText(todo.getExpireDateAsString());
			return convertView;
		}
	}
    
    private void initTodos(){
    	List<Long> list = new ArrayList<Long>();
    	list.add(1l);
    	list.add(2342344l);
    	Todo newtodo = new Todo();
    	newtodo.setName("testSth" +Math.random()*100);
    	newtodo.setFinished(false);
    	newtodo.setFavorite(true);
    	newtodo.setExpireDate(GregorianCalendar.getInstance());
    	newtodo.setContacts(list);
    	try {
			db.createTodo(newtodo);
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private class UpdateDBOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{

    	private long id;
    	private String column;
    	
    	public UpdateDBOnCheckedChangeListener(long id, String column) {
			this.id = id;
			this.column = column;
		}
    	
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			db.open();
			Log.i(this.getClass().toString(), "UpdateDBOnCheckedChanged " +db.updateBooleanColumns(id, column, isChecked));
			db.close();
		}
    	
    }
}