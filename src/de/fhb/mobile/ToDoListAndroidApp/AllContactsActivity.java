package de.fhb.mobile.ToDoListAndroidApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.fhb.mobile.ToDoListAndroidApp.commons.AndroidContactsHelper;
import de.fhb.mobile.ToDoListAndroidApp.models.Contact;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoDatabase;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoTable;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class AllContactsActivity extends ListActivity{
	public static final int REQUEST_CODE_CONTACTS = 200;
	public static final String ARG_CONTACT_ID = "contactID";
	public static final int RESULT_CODE_CONTACTS = 100;
	private TodoDatabase db;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		db = new TodoDatabase(this);
		db.open();
		List<Long> contactsLong = db.getAllContacts();
		
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
    	
    	Intent resultIntent = new Intent(AllContactsActivity.this,
				ToDoDetailsActivity.class);
    	Contact contact = (Contact)l.getItemAtPosition(position);
    	Log.i(this.getClass().toString(), "onListItemClick: Contact_id " + contact.getId());
		// pass the item to the intent
    	resultIntent.putExtra(ARG_CONTACT_ID, contact.getId());
		// start the details activity with the intent
		setResult(Activity.RESULT_OK, resultIntent);
    	
		finish();
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
