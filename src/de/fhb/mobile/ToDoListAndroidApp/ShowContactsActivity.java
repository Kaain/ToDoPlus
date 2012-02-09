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

// TODO: Auto-generated Javadoc
/**
 * The Class ShowContactsActivity.
 */
public class ShowContactsActivity extends ListActivity {

	/** The Constant REQUEST_CODE_CONTACTS. */
	public static final int REQUEST_CODE_CONTACTS = 200;

	/** The Constant ARG_CONTACT_ID. */
	public static final String ARG_CONTACT_ID = "contactID";

	/** The Constant RESULT_CODE_CONTACTS. */
	public static final int RESULT_CODE_CONTACTS = 100;

	/** The Constant MODE_ALLCONTACTS. */
	private static final int MODE_ALLCONTACTS = 4000;

	/** The Constant MODE_CONTACTS_FOR_TODO. */
	private static final int MODE_CONTACTS_FOR_TODO = 5000;

	/** The db. */
	private TodoDatabase db;

	/** The mode. */
	private int mode;

	/** The actual todo. */
	private Todo actualTodo;

	// View
	/** The activityname. */
	private TextView activityname;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_all_contacts);
		activityname = (TextView) findViewById(R.id.allcontacts_activityname);

		db = new TodoDatabase(this);
		db.open();

		List<Long> contactsLong;

		long todoID = getIntent().getLongExtra(ToDoDetailsActivity.ARG_TODO_ID,
				-1);

		if (todoID == -1) {
			mode = MODE_ALLCONTACTS;
			contactsLong = db.getAllContacts();
			actualTodo = null;
		} else {
			mode = MODE_CONTACTS_FOR_TODO;
			contactsLong = db.getAllContacts(todoID);
			actualTodo = db.getTodoById(todoID);
			activityname.setText("Contacts for " + actualTodo.getName());
		}

		ContactAdapter adapter = new ContactAdapter(this,
				android.R.layout.simple_list_item_1,
				AndroidContactsHelper.getContacts(getContentResolver(),
						contactsLong));
		setListAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		Log.i(this.getClass().toString(), "onPause");
		super.onPause();
		db.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop() {
		Log.i(this.getClass().toString(), "onStop");
		super.onStop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		Log.i(this.getClass().toString(), "onResume");
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	public void onRestart() {
		Log.i(this.getClass().toString(), "onRestart");
		super.onRestart();
		db.open();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i(this.getClass().toString(), "onListItemClick: " + position);
		Contact contact = (Contact) l.getItemAtPosition(position);
		Log.i(this.getClass().toString(), "onListItemClick: Contact_id "
				+ contact.getId());
		if (mode == MODE_ALLCONTACTS) {

			Intent resultIntent = new Intent(ShowContactsActivity.this,
					ToDoDetailsActivity.class);
			resultIntent.putExtra(ARG_CONTACT_ID, contact.getId());
			setResult(Activity.RESULT_OK, resultIntent);
			finish();

		} else if (mode == MODE_CONTACTS_FOR_TODO) {
			Intent messageIntent = new Intent(ShowContactsActivity.this,
					SendMessageActivity.class);
			messageIntent.putExtra(ARG_CONTACT_ID, contact.getId());
			messageIntent.putExtra(ToDoDetailsActivity.ARG_TODO_ID,
					actualTodo.getId());
			startActivity(messageIntent);
		}
	}

	/**
	 * The Class ContactAdapter.
	 */
	private class ContactAdapter extends ArrayAdapter<Contact> {

		/** The m context. */
		private final Context mContext;

		/** The m layout. */
		private final int mLayout;

		/** The m layout inflater. */
		private final LayoutInflater mLayoutInflater;

		/** The list. */
		private List<Contact> list;

		/**
		 * The Class ViewHolder.
		 */
		private final class ViewHolder {

			/** The name. */
			public TextView name;
		}

		/**
		 * Instantiates a new contact adapter.
		 * 
		 * @param context
		 *            the context
		 * @param layout
		 *            the layout
		 * @param list
		 *            the list
		 */
		public ContactAdapter(Context context, int layout, List<Contact> list) {
			super(context, layout, list);
			this.mContext = context;
			this.mLayout = layout;
			this.list = list;
			this.mLayoutInflater = LayoutInflater.from(mContext);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
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
