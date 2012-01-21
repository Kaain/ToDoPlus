package de.fhb.mobile.ToDoListAndroidApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cz.destil.settleup.gui.MultiSpinner;
import cz.destil.settleup.gui.MultiSpinner.MultiSpinnerListener;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoDatabase;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class ToDoDetailsActivity extends Activity{
	
	private static final int DATE_DIALOG_ID = 1;
	private static final int TIME_DIALOG_ID = 2;
	private static final int MODE_NEW = 10;
	private static final int MODE_EDIT = 20;
	private List<String> allContacts;
	private TodoDatabase database;
	private Todo actualTodo;
	
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
		
		long todoID = getIntent().getLongExtra(ToDoListActivity.ARG_TODO_ID, -1);
		if(todoID == -1)
			initActivityForNewTodo();
		else
			initActivityforEditTodo(todoID);
			
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
        /*
        MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.multi_spinner);
		multiSpinner.setItems(allContacts, "Items", new MultiSpinnerListener() {
			
			@Override
			public void onItemsSelected(boolean[] selected) {
				// TODO Auto-generated method stub
			}
		});
	*/
	}
	
	private void initViewComponents(int mode){
		 detailsName = (EditText)findViewById(R.id.details_name);
		 detailsDescription = (EditText)findViewById(R.id.details_description);
		 detailsFinished = (CheckBox)findViewById(R.id.details_finished);
		 detailsFavorite = (CheckBox)findViewById(R.id.details_favorite);
		 detailsContacts = (MultiSpinner)findViewById(R.id.details_contacts);
		 detailsdateLayout = (LinearLayout)findViewById(R.id.details_date_layout);
		 detailsDateValue = (TextView)findViewById(R.id.details_date_value);
		 detailstimeLayout = (LinearLayout)findViewById(R.id.details_time_layout);
		 detailsTimeValue = (TextView)findViewById(R.id.details_time_value);
		 detailsConfirm = (Button)findViewById(R.id.details_confirm);
		 
		 initDatePicker();
		 initTimePicker();
		 
		 if(mode == MODE_NEW){
			 detailsFinished.setVisibility(View.INVISIBLE);
			 detailsConfirm.setText("Create");
		 }
		 else if(mode == MODE_EDIT){
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
		initViewComponents(MODE_NEW);
		
	}

	private void initActivityforEditTodo(long todoID) {
		Log.i(this.getClass().toString(), "initActivityforEditTodo ID: " +todoID);
		actualTodo = database.getTodoById(todoID);	
		initViewComponents(MODE_EDIT);
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
}
