package de.fhb.mobile.ToDoListAndroidApp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import cz.destil.settleup.gui.MultiSpinner;
import cz.destil.settleup.gui.MultiSpinner.MultiSpinnerListener;

public class ToDoNewActivity extends Activity {
	private List<String> allContacts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(this.getClass().getName(), "init");
		// set the list view as content view
		setContentView(R.layout.details);
		Log.i(this.getClass().getName(), "setContentView(R.layout.details)");
		allContacts = new ArrayList<String>();
		allContacts.add("Contacts1");
		allContacts.add("Contacts2");
		allContacts.add("Contacts3");
		allContacts.add("Contacts4");
		allContacts.add("Contacts5");
		allContacts.add("Contacts6");
		allContacts.add("Contacts7");
		allContacts.add("Contacts8");
		allContacts.add("Contacts9");
		allContacts.add("Contacts10");
		allContacts.add("Contacts11");
		allContacts.add("Contacts12");
		allContacts.add("Contacts13");
		allContacts.add("Contacts14");
		allContacts.add("Contacts15");
		MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.multi_spinner);
		multiSpinner.setItems(allContacts, "Items", new MultiSpinnerListener() {

			@Override
			public void onItemsSelected(boolean[] selected) {
				// TODO Auto-generated method stub
			}
		});

	}
}
