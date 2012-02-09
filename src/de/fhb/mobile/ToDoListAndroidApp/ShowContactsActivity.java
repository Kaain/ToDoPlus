package de.fhb.mobile.ToDoListAndroidApp;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.fhb.mobile.ToDoListAndroidApp.commons.AndroidContactsHelper;
import de.fhb.mobile.ToDoListAndroidApp.models.Contact;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoDatabase;

public class ShowContactsActivity extends ListActivity{
	public static final int REQUEST_CODE_CONTACTS = 200;
	public static final String ARG_CONTACT_ID = "contactID";
	public static final int RESULT_CODE_CONTACTS = 100;
	private static final int MODE_ALLCONTACTS = 4000;
	private static final int MODE_CONTACTS_FOR_TODO = 5000;
	private TodoDatabase db;
	private int mode;
	private Todo actualTodo;
	
	// View
	private TextView activityname;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_all_contacts);
		activityname = (TextView) findViewById(R.id.allcontacts_activityname);
		
		db = new TodoDatabase(this);
		db.open();
		
		List<Long> contactsLong;
		
		long todoID = getIntent().getLongExtra(ToDoDetailsActivity.ARG_TODO_ID, -1);
		
		if(todoID == -1){
			mode = MODE_ALLCONTACTS;
			contactsLong = db.getAllContacts();
			actualTodo = null;
		}else{
			mode = MODE_CONTACTS_FOR_TODO;
			contactsLong = db.getAllContacts(todoID);
			actualTodo = db.getTodoById(todoID);
			Log.d(this.getClass().toString(), actualTodo.toString());
			activityname.setText("Contacts for "+actualTodo.getName());
		}
		
		ContactAdapter adapter = new ContactAdapter(this,
				android.R.layout.simple_list_item_1, AndroidContactsHelper.getContacts(getContentResolver(), contactsLong));
		setListAdapter(adapter);
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
    }
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id){
    	Log.i(this.getClass().toString(), "onListItemClick: " +position);
    	Contact contact = (Contact)l.getItemAtPosition(position);
    	Log.i(this.getClass().toString(), "onListItemClick: Contact_id " + contact.getId());
    	if(mode == MODE_ALLCONTACTS){
    		
    	Intent resultIntent = new Intent(ShowContactsActivity.this,
				ToDoDetailsActivity.class);
    	resultIntent.putExtra(ARG_CONTACT_ID, contact.getId());
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
		
    	}else if (mode == MODE_CONTACTS_FOR_TODO){
    		Intent messageIntent = new Intent(ShowContactsActivity.this, SendMessageActivity.class);
    		messageIntent.putExtra(ARG_CONTACT_ID, contact.getId());
    		messageIntent.putExtra(ToDoDetailsActivity.ARG_TODO_ID, actualTodo.getId());
    		startActivity(messageIntent);
    	}
    }

	private class ContactAdapter extends ArrayAdapter<Contact> {

		private final Context mContext;
		private final int mLayout;
		private final LayoutInflater mLayoutInflater;
		private List<Contact> list;

		private final class ViewHolder {
			public TextView name;
		}

		public ContactAdapter(Context context, int layout, List<Contact> list) {
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
			Contact contact = list.get(position);
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(mLayout, null);

				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(android.R.id.text1);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			String name = contact.getDisplayName();
			viewHolder.name.setText(name);
			return convertView;
		}
	}
}
