package de.fhb.mobile.ToDoListAndroidApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import de.fhb.mobile.ToDoListAndroidApp.persistance.CreateException;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoDatabase;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoTable;

public class ToDoListActivity extends ListActivity {
	
	public static final int REQUEST_CODE_TODODETAILS = 1;
	public static final int REQUEST_CODE_ALLCONTACTS = 2;
	private static final int SORTING_FAVORITE_DATE = 20;
	private static final int SORTING_DATE_FAVORITE = 30;
	private static final int MODE_ALLTODOS = 1000;
	private static final int MODE_ALLTODOS_FOR_CONTACT = 2000;
	public static final String ARG_TODO_ID = "todoObjectID";
	private TodoDatabase db;
	private int sorting;
	private int mode;
	private long contactId;
	
	// View
	private Spinner spinner_sortby;
	private TextView headline;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);
        Log.i(this.getClass().toString(), "onCreate");
        mode = MODE_ALLTODOS;
        headline = (TextView) findViewById(R.id.alltodos_activityname);
        initSortSpinner();
        db = new TodoDatabase(this);
        db.open();
        
        //initTodos();
    }
    @Override
    public void onPause(){
    	Log.i(this.getClass().toString(), "onPause");
    	super.onPause();
    	db.close();
    }
    @Override
    public void onStop(){
    	Log.i(this.getClass().toString(), "onStop");
    	super.onStop();
    }
    @Override
    public void onResume(){
    	Log.i(this.getClass().toString(), "onResume");
    	super.onResume();
    }
    @Override
    public void onRestart(){
    	Log.i(this.getClass().toString(), "onRestart");
    	super.onRestart();
    	db.open();
    	initListAdapter(sorting);
    }
    
    private void initSortSpinner() {
    	Log.i(this.getClass().toString(), "initSortSpinner()");
    	spinner_sortby = (Spinner)findViewById(R.id.spinner_orderby);
		CharSequence[] objects = { "sort by importance + date", "sort by date + importance"};
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, objects );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_sortby.setAdapter(adapter);
		spinner_sortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent,
				        View view, int pos, long id)  {
					Log.i(this.getClass().toString(), "Sortspinner select: pos= " +pos);
					switch(pos){
					case 0:
						initListAdapter(SORTING_FAVORITE_DATE);
						break;
					case 1:
						initListAdapter(SORTING_DATE_FAVORITE);
						break;
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initListAdapter(int sorting){
		Log.i(this.getClass().toString(), "initListAdapter()");
		this.sorting = sorting;
		
		switch(mode){
		case MODE_ALLTODOS:
			initListAdapterForAllTodos();
			break;
		case MODE_ALLTODOS_FOR_CONTACT:
			initListAdapterForAllTodosForContact();
			break;
		}
		
    }
	
	private void initListAdapterForAllTodosForContact(){
		Log.i(this.getClass().toString(), "initListAdapterForAllTodosForContact()");
		List<Todo> list = new ArrayList<Todo>(0);
		
		headline.setText("All Todos for Contact");
		
		switch (sorting) {
		case SORTING_FAVORITE_DATE:
			list = db.getAllTodosForContact(TodoTable.KEY_FINISHED +" DESC, " +TodoTable.KEY_FAVORITE +" DESC, " +TodoTable.KEY_EXPIREDATE +" ASC", contactId);
			break;
		case SORTING_DATE_FAVORITE:
			list = db.getAllTodosForContact(TodoTable.KEY_FINISHED +" DESC, " +TodoTable.KEY_EXPIREDATE +" ASC, " +TodoTable.KEY_FAVORITE +" DESC", contactId);
			break;
		default:
			list = db.getAllTodosForContact(TodoTable.KEY_FINISHED +" DESC", contactId);
		}
    	ArrayAdapter<Todo> sadapter = new TodoAdapter(this,R.layout.listelement, list);
    	// setze den Adapter auf die Listenansicht
        this.setListAdapter(sadapter);
	}
	
	private void initListAdapterForAllTodos(){
		Log.i(this.getClass().toString(), "initListAdapterForAllTodos()");
		List<Todo> list = new ArrayList<Todo>(0);
		
		headline.setText("All Todos");
		
		switch (sorting) {
		case SORTING_FAVORITE_DATE:
			list = db.getAllTodos(TodoTable.KEY_FINISHED +" DESC, " +TodoTable.KEY_FAVORITE +" DESC, " +TodoTable.KEY_EXPIREDATE +" ASC");
			break;
		case SORTING_DATE_FAVORITE:
			list = db.getAllTodos(TodoTable.KEY_FINISHED +" DESC, " +TodoTable.KEY_EXPIREDATE +" ASC, " +TodoTable.KEY_FAVORITE +" DESC" );
			break;
		default:
			list = db.getAllTodos(TodoTable.KEY_FINISHED +" DESC");
		}
    	ArrayAdapter<Todo> sadapter = new TodoAdapter(this,R.layout.listelement, list);
    	// setze den Adapter auf die Listenansicht
        this.setListAdapter(sadapter);
	}
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
    	Log.i(this.getClass().toString(), "onListItemClick: " +position);
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
    	Log.i(this.getClass().toString(), "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(this.getClass().toString(), "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (REQUEST_CODE_ALLCONTACTS):
			if (resultCode == Activity.RESULT_OK) { 
				mode = MODE_ALLTODOS_FOR_CONTACT;
			contactId = data.getLongExtra(AllContactsActivity.ARG_CONTACT_ID,
					-1);
			}
			break;
		}
	}
	
    
    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
    	Log.i(this.getClass().toString(), "onPrepareOptionsMenu");
		MenuItem allTodosItem = menu.findItem(R.id.menu_item_all_todos);
		return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_item_create_new_todo:
        	startCreateActivity();
        	return true;
        case R.id.menu_item_all_todos:
        	initListAdapterForAllTodos();
            return true;
        case R.id.menu_item_all_contacts_with_todos:
        	startAllContactActivity();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void startAllContactActivity() {
    	Log.i(this.getClass().toString(), "startAllContactActivity()");
    	Intent intent = new Intent(ToDoListActivity.this,
    			AllContactsActivity.class);
		startActivityForResult(intent, REQUEST_CODE_ALLCONTACTS);
	}

	private void startCreateActivity(){
		Log.i(this.getClass().toString(), "startCreateActivity()");
    	Intent intent = new Intent(ToDoListActivity.this,
				ToDoDetailsActivity.class);
		// start the details activity with the intent
		startActivityForResult(intent,REQUEST_CODE_TODODETAILS);
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
    	    public ImageButton deleteButton;
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
			Log.i(this.getClass().toString(), "getView postion: " +position);
			ViewHolder viewHolder;
			Todo todo = list.get(position);
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(mLayout, null);

				viewHolder = new ViewHolder();
				viewHolder.hiddenID = (TextView) convertView
						.findViewById(R.id.list_hiddenid);
				viewHolder.finishedCheckBox = (CheckBox) convertView
						.findViewById(R.id.list_finished);
				viewHolder.favoriteCheckBox = (CheckBox) convertView
						.findViewById(R.id.list_favorite);
				viewHolder.todoName = (TextView) convertView
						.findViewById(R.id.list_todoname);
				viewHolder.expiredate = (TextView) convertView
						.findViewById(R.id.list_expiredate);
				viewHolder.deleteButton = (ImageButton) convertView
						.findViewById(R.id.list_delete_button);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			long id = todo.getId();
			boolean finished = todo.isFinished();
			boolean favorite = todo.isFavorite();
			String name = todo.getName();
			Calendar expiredate = todo.getExpireDate();
			if (expiredate != null) {
				Calendar calNow = GregorianCalendar.getInstance();
				if (expiredate.compareTo(calNow) == -1){
					viewHolder.expiredate.setBackgroundColor(Color.rgb(255, 255,
							0));
					viewHolder.expiredate.setTextColor(Color.BLACK);
				}else{
					viewHolder.expiredate.setTextColor(Color.LTGRAY);
					viewHolder.expiredate
							.setBackgroundColor(android.R.color.transparent);
				}
				viewHolder.expiredate.setText(todo.getExpireDateAsString());
			}else{
				viewHolder.expiredate.setText("");
			}
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
			
			viewHolder.deleteButton.setOnClickListener(null);
			viewHolder.deleteButton.setOnClickListener(new DeleteOnClickListener(id));
			viewHolder.deleteButton.setFocusable(false);
			return convertView;
		}
	}
    
    private void initTodos(){
    	List<Long> list = new ArrayList<Long>();
    	list.add(1l);
    	list.add(2342344l);
    	Todo newtodo = new Todo();
    	newtodo.setName("testSth" +(int)Math.random()*100);
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
			Log.i(this.getClass().toString(), "UpdateDBOnCheckedChanged " 
				+db.updateBooleanColumns(id, column, isChecked));
		}
    }
    private class DeleteOnClickListener implements OnClickListener{

    	long id;
    	
    	public DeleteOnClickListener(long id) {
			this.id = id;
		}
    	
		@Override
		public void onClick(View v) {
			db.deleteTodo(id);
			initListAdapter(sorting);
		}
    	
    }
}