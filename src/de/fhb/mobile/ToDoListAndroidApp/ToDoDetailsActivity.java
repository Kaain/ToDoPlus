package de.fhb.mobile.ToDoListAndroidApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cz.destil.settleup.gui.MultiSpinner;
import cz.destil.settleup.gui.MultiSpinner.MultiSpinnerListener;
import de.fhb.mobile.ToDoListAndroidApp.commons.DateHelper;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import de.fhb.mobile.ToDoListAndroidApp.persistance.CreateException;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoDatabase;
import de.fhb.mobile.ToDoListAndroidApp.persistance.UpdateException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ToDoDetailsActivity extends Activity{
	
	private static final int DATE_DIALOG_ID = 1;
	private static final int TIME_DIALOG_ID = 2;
	private static final int MODE_NEW = 10;
	private static final int MODE_EDIT = 20;
	private List<String> allContacts;
	private TodoDatabase database;
	private Todo actualTodo;
	private int mode;
	
	// DateTime vars
	private int dateYear;
	private int dateMonth;
	private int dateDay;
	private int timeHour;
	private int timeMinutes;
	
	//ViewObjects
	private EditText detailsName;
	private EditText detailsDescription;
	private CheckBox detailsFinished;
	private CheckBox detailsFavorite;
	private MultiSpinner detailsContacts;
	private LinearLayout detailsdateLayout;
	private TextView detailsDateValue;
	private LinearLayout detailstimeLayout;
	private TextView detailsTimeValue;
	private Button detailsConfirm;
	
	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			dateYear = year;
			dateMonth = monthOfYear;
			dateDay = dayOfMonth;
			updateDate();
		}
	};
	
	private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			timeHour = hourOfDay;
			timeMinutes = minute;
			updateTime();
		}
	};

                
	
	
	@Override
    public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.i(this.getClass().getName(), "onCreate DetailsActivity");
		
        setContentView(R.layout.edit_new);
        
        database = new TodoDatabase(this);
		database.open();
		
		allContacts = getAllContacts();
		
		long todoID = getIntent().getLongExtra(ToDoListActivity.ARG_TODO_ID, -1);
		if(todoID == -1){
			mode = MODE_NEW;
			initActivityForNewTodo();
		}else{
			mode = MODE_EDIT;
			initActivityforEditTodo(todoID);
		}
	}
	
	private void initViewComponents() {
		detailsName = (EditText) findViewById(R.id.details_name);
		detailsName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				actualTodo.setName(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

		});
		detailsDescription = (EditText) findViewById(R.id.details_description);
		detailsDescription.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				actualTodo.setDescription(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		detailsFinished = (CheckBox) findViewById(R.id.details_finished);
		detailsFinished
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Log.i(this.getClass().toString(),
								"detailsFinished checkedChanged: " + isChecked);
						actualTodo.setFinished(isChecked);
					}
				});
		detailsFavorite = (CheckBox) findViewById(R.id.details_favorite);
		detailsFavorite
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Log.i(this.getClass().toString(),
								"detailsFavorite checkedChanged: " + isChecked);
						actualTodo.setFavorite(isChecked);
					}
				});
		detailsContacts = (MultiSpinner) findViewById(R.id.details_contacts);
		detailsContacts.setItems(allContacts, "All Contacts", false,
				new MultiSpinnerListener() {

					@Override
					public void onItemsSelected(boolean[] selected) {
						Log.i(this.getClass().toString(), "contacts selected");

					}
				});

		detailsdateLayout = (LinearLayout) findViewById(R.id.details_date_layout);
		detailsDateValue = (TextView) findViewById(R.id.details_date_value);
		initDatePicker();

		detailstimeLayout = (LinearLayout) findViewById(R.id.details_time_layout);
		detailsTimeValue = (TextView) findViewById(R.id.details_time_value);
		initTimePicker();

		detailsConfirm = (Button) findViewById(R.id.details_confirm);
		detailsConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "detailsConfirm clicked");
				try {
					updateDatabase();
					database.close();
					finish();
				} catch (UpdateException e) {
					makeToast(e.getMessage());
					e.printStackTrace();
				} catch (CreateException e) {
					makeToast(e.getMessage());
					e.printStackTrace();
				}
			}
		});

		if (mode == MODE_NEW) {
			detailsFinished.setVisibility(View.INVISIBLE);
			detailsConfirm.setText("Create");
		} else if (mode == MODE_EDIT) {
			detailsConfirm.setText("Save");
		}
	}
	@Override
	protected Dialog onCreateDialog(int id){
		
		switch (id) {

        case DATE_DIALOG_ID:
        	return new DatePickerDialog(this, dateSetListener, dateYear, dateMonth, dateDay);
        case TIME_DIALOG_ID:
        	return new TimePickerDialog(this, timeSetListener, timeHour, timeMinutes, true);
        }
        return null;
	}
	
	private void makeToast(String text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
		toast.show();
	}
	
	private void updateDate() {
        detailsDateValue.setText((dateMonth + 1) +"-" +dateDay +"-" +dateYear);
    }
	
	private void updateTime() {
        detailsTimeValue.setText(pad(timeHour) +":" +pad(timeMinutes));
    }
	private static String pad(int c) {
	    if (c >= 10)
	        return String.valueOf(c);
	    else
	        return "0" + String.valueOf(c);
	}
	

	private void initActivityForNewTodo() {
		Log.i(this.getClass().toString(), "initActivityForNewTodo");
		actualTodo = new Todo();
		initViewComponents();
	}

	private void initActivityforEditTodo(long todoID) {
		Log.i(this.getClass().toString(), "initActivityforEditTodo ID: " +todoID);
		actualTodo = database.getTodoById(todoID);	
		initViewComponents();
		initData();
	}
	
	private void initData(){
		detailsName.setText(actualTodo.getName());
		detailsDescription.setText(actualTodo.getDescription());
		detailsFavorite.setChecked(actualTodo.isFavorite());
		detailsFinished.setChecked(actualTodo.isFinished());
		String date = DateHelper.getDateAsString(actualTodo.getExpireDate());
		detailsDateValue.setText(date);
		String time = DateHelper.getTimeAsString(actualTodo.getExpireDate());
		detailsTimeValue.setText(time);
	}
	
	private void initDatePicker(){
		Calendar cal = GregorianCalendar.getInstance();
		cal.roll(Calendar.DAY_OF_MONTH, true);
		dateYear = cal.get(Calendar.YEAR);
		dateMonth = cal.get(Calendar.MONTH);
		dateDay = cal.get(Calendar.DAY_OF_MONTH);
		detailsdateLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "Click on DateLayout");
				showDialog(DATE_DIALOG_ID);
			}
		});
	}
	
	private void initTimePicker(){
		Calendar cal = GregorianCalendar.getInstance();
		cal.roll(Calendar.HOUR_OF_DAY, true);
		timeHour = cal.get(Calendar.HOUR_OF_DAY);
		timeMinutes = cal.get(Calendar.MINUTE);
		detailstimeLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "Click on TimeLayout");
				showDialog(TIME_DIALOG_ID);
			}
		});
	}
	
	private List<String> getAllContacts(){
		List<String> contacts = new ArrayList<String>();
		Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while(people.moveToNext()) {
		   int idFieldColumnIndex = people.getColumnIndex(PhoneLookup._ID);
		   long id = people.getLong(idFieldColumnIndex);
		   int nameFieldColumnIndex = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
		   String name = people.getString(nameFieldColumnIndex);
		   contacts.add(name);
		}
		people.close();
		return contacts;
	}
	private void updateDatabase() throws UpdateException, CreateException {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(dateYear, dateMonth, dateDay, timeHour, timeMinutes);
		actualTodo.setExpireDate(cal);
		switch(mode){
		case MODE_NEW:
			database.createTodo(actualTodo);
			break;
		case MODE_EDIT:
			database.updateTodo(actualTodo);
			break;
		}
	}
}
