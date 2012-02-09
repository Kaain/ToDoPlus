package de.fhb.mobile.ToDoListAndroidApp;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import cz.destil.settleup.gui.MultiSpinner;
import cz.destil.settleup.gui.MultiSpinner.MultiSpinnerListener;
import de.fhb.mobile.ToDoListAndroidApp.commons.AndroidContactsHelper;
import de.fhb.mobile.ToDoListAndroidApp.commons.DateHelper;
import de.fhb.mobile.ToDoListAndroidApp.exceptions.CreateException;
import de.fhb.mobile.ToDoListAndroidApp.exceptions.UpdateException;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoDatabase;

/**
 * The Class ToDoDetailsActivity.
 */
public class ToDoDetailsActivity extends Activity {

	/** The Constant DATE_DIALOG_ID. */
	private static final int DATE_DIALOG_ID = 1;

	/** The Constant TIME_DIALOG_ID. */
	private static final int TIME_DIALOG_ID = 2;

	/** The Constant MODE_NEW. */
	private static final int MODE_NEW = 10;

	/** The Constant MODE_EDIT. */
	private static final int MODE_EDIT = 20;

	/** The Constant REQUEST_CODE_SENDMESSAGES. */
	public static final int REQUEST_CODE_SENDMESSAGES = 1000;

	/** The Constant ARG_TODO_ID. */
	public static final String ARG_TODO_ID = "todoObjectID";

	/** The db. */
	private TodoDatabase db;

	/** The actual todo. */
	private Todo actualTodo;

	/** The mode. */
	private int mode;

	// DateTime vars
	/** The date year. */
	private int dateYear;

	/** The date month. */
	private int dateMonth;

	/** The date day. */
	private int dateDay;

	/** The time hour. */
	private int timeHour;

	/** The time minutes. */
	private int timeMinutes;

	/** The calendar. */
	private Calendar calendar;

	// ViewObjects
	/** The details name. */
	private EditText detailsName;

	/** The details description. */
	private EditText detailsDescription;

	/** The details finished. */
	private CheckBox detailsFinished;

	/** The details favorite. */
	private CheckBox detailsFavorite;

	/** The details contacts. */
	private MultiSpinner detailsContacts;

	/** The details date layout. */
	private LinearLayout detailsDateLayout;

	/** The details date value. */
	private TextView detailsDateValue;

	/** The details time layout. */
	private LinearLayout detailsTimeLayout;

	/** The details time value. */
	private TextView detailsTimeValue;

	/** The details confirm. */
	private Button detailsConfirm;

	/** The details delete. */
	private Button detailsDelete;

	/** The details send messages. */
	private Button detailsSendMessages;

	/** The details delete date. */
	private ImageButton detailsDeleteDate;

	/** The date set listener. */
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

	/** The time set listener. */
	private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			timeHour = hourOfDay;
			timeMinutes = minute;
			updateTime();
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(this.getClass().getName(), "onCreate DetailsActivity");

		setContentView(R.layout.edit_new);

		db = new TodoDatabase(this);
		db.open();

		long todoID = getIntent()
				.getLongExtra(ToDoListActivity.ARG_TODO_ID, -1);
		if (todoID == -1) {
			mode = MODE_NEW;
			initActivityForNewTodo();
		} else {
			mode = MODE_EDIT;
			initActivityForEditTodo(todoID);
		}
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
	 * @see android.app.Activity#onDestroy()
	 */
	public void onDestroy() {
		Log.i(this.getClass().toString(), "onDestroy");
		super.onDestroy();
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

	/**
	 * Inits the view components.
	 */
	private void initViewComponents() {
		Log.i(this.getClass().toString(), "initViewComponents()");
		detailsName = (EditText) findViewById(R.id.details_name);
		detailsName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				actualTodo.setName(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

		});
		detailsDescription = (EditText) findViewById(R.id.details_description);
		detailsDescription.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				actualTodo.setDescription(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
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
		detailsContacts
				.setItems(AndroidContactsHelper.getAllContacts(this
						.getContentResolver()), "Contacts", false,
						new MultiSpinnerListener() {

							@Override
							public void onItemsSelected(boolean[] selected) {
								Log.i(this.getClass().toString(),
										"contacts selected");
								actualTodo.setContacts(detailsContacts
										.getSelectedContacts());
							}
						});

		detailsDateLayout = (LinearLayout) findViewById(R.id.details_date_layout);
		detailsDateValue = (TextView) findViewById(R.id.details_date_value);
		initDatePicker();
		detailsDeleteDate = (ImageButton) findViewById(R.id.details_delete_date);
		detailsDeleteDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				detailsDateValue.setText("");
				detailsTimeValue.setText("");
			}
		});

		detailsTimeLayout = (LinearLayout) findViewById(R.id.details_time_layout);
		detailsTimeValue = (TextView) findViewById(R.id.details_time_value);
		initTimePicker();

		detailsConfirm = (Button) findViewById(R.id.details_confirm);
		detailsConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "detailsConfirm clicked");
				try {
					updateDatabase();
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

		detailsSendMessages = (Button) findViewById(R.id.details_send_messages);
		detailsSendMessages.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					updateDatabase();
				} catch (UpdateException e) {
					makeToast(e.getMessage());
					e.printStackTrace();
				} catch (CreateException e) {
					makeToast(e.getMessage());
					e.printStackTrace();
				}
				Intent intent = new Intent(ToDoDetailsActivity.this,
						ShowContactsActivity.class);
				intent.putExtra(ARG_TODO_ID, actualTodo.getId());
				startActivityForResult(intent, REQUEST_CODE_SENDMESSAGES);
			}
		});

		detailsDelete = (Button) findViewById(R.id.details_delete);
		detailsDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				db.deleteTodo(actualTodo.getId());
				finish();
			}
		});
		if (mode == MODE_NEW) {
			detailsFinished.setVisibility(View.INVISIBLE);
			detailsDelete.setVisibility(View.INVISIBLE);
			detailsSendMessages.setVisibility(View.INVISIBLE);
			detailsConfirm.setText("Create");
		} else if (mode == MODE_EDIT) {
			detailsConfirm.setText("Save");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Log.i(this.getClass().toString(), "onCreateDialog() - " + id);
		switch (id) {

		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, dateSetListener, dateYear,
					dateMonth, dateDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timeSetListener, timeHour,
					timeMinutes, true);
		}
		return null;
	}

	/**
	 * Makes a toast.
	 * 
	 * @param text
	 *            the text
	 */
	private void makeToast(String text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
		toast.show();
	}

	/**
	 * Updates the date.
	 */
	private void updateDate() {
		Log.i(this.getClass().toString(), "updateDate()");
		calendar.set(Calendar.DAY_OF_MONTH, dateDay);
		calendar.set(Calendar.MONTH, dateMonth);
		calendar.set(Calendar.YEAR, dateYear);
		detailsDateValue.setText(DateHelper.getDateAsString(calendar));
		if (detailsTimeValue.getText().length() < 1) {
			updateTime();
		}
	}

	/**
	 * Updates the time.
	 */
	private void updateTime() {
		Log.i(this.getClass().toString(), "updateTime()");
		calendar.set(Calendar.HOUR_OF_DAY, timeHour);
		calendar.set(Calendar.MINUTE, timeMinutes);
		detailsTimeValue.setText(DateHelper.getTimeAsString(calendar));
		if (detailsDateValue.getText().length() < 1) {
			updateDate();
		}
	}

	/**
	 * Inits the activity for new todo.
	 */
	private void initActivityForNewTodo() {
		Log.i(this.getClass().toString(), "initActivityForNewTodo");
		actualTodo = new Todo();
		calendar = GregorianCalendar.getInstance();
		initViewComponents();
	}

	/**
	 * Inits the activity for edit todo.
	 * 
	 * @param todoID
	 *            the todo id
	 */
	private void initActivityForEditTodo(long todoID) {
		Log.i(this.getClass().toString(), "initActivityforEditTodo ID: "
				+ todoID);
		actualTodo = db.getTodoById(todoID);
		calendar = actualTodo.getExpireDate();
		if (calendar == null) {
			calendar = GregorianCalendar.getInstance();
		}
		initViewComponents();
		initData();
	}

	/**
	 * Inits the data to the view components.
	 */
	private void initData() {
		Log.i(this.getClass().toString(), "initData()");
		detailsName.setText(actualTodo.getName());
		detailsDescription.setText(actualTodo.getDescription());
		detailsFavorite.setChecked(actualTodo.isFavorite());
		detailsFinished.setChecked(actualTodo.isFinished());
		String date = DateHelper.getDateAsString(actualTodo.getExpireDate());
		detailsDateValue.setText(date);
		String time = DateHelper.getTimeAsString(actualTodo.getExpireDate());
		detailsTimeValue.setText(time);
		detailsContacts.setSelectedContacts(actualTodo.getContacts());
	}

	/**
	 * Inits the date picker.
	 */
	private void initDatePicker() {
		Log.i(this.getClass().toString(), "initDatePicker()");
		if (detailsDateValue.getText().length() < 1) {
			calendar.roll(Calendar.DAY_OF_MONTH, true);
		}
		dateYear = calendar.get(Calendar.YEAR);
		dateMonth = calendar.get(Calendar.MONTH);
		dateDay = calendar.get(Calendar.DAY_OF_MONTH);
		detailsDateLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "Click on DateLayout");
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	/**
	 * Inits the time picker.
	 */
	private void initTimePicker() {
		Log.i(this.getClass().toString(), "initTimePicker()");
		if (detailsTimeValue.getText().length() < 1) {
			calendar.roll(Calendar.HOUR_OF_DAY, true);
		}
		timeHour = calendar.get(Calendar.HOUR_OF_DAY);
		timeMinutes = calendar.get(Calendar.MINUTE);
		detailsTimeLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "Click on TimeLayout");
				showDialog(TIME_DIALOG_ID);
			}
		});
	}

	/**
	 * Update database.
	 * 
	 * @throws UpdateException
	 *             the update exception
	 * @throws CreateException
	 *             the create exception
	 */
	private void updateDatabase() throws UpdateException, CreateException {
		Log.i(this.getClass().toString(), "updateDatabase()");
		if (!detailsDateValue.getText().toString().isEmpty())
			actualTodo.setExpireDate(calendar);
		else
			actualTodo.setExpireDate(null);
		switch (mode) {
		case MODE_NEW:
			db.createTodo(actualTodo);
			break;
		case MODE_EDIT:
			db.updateTodo(actualTodo);
			break;
		}
	}
}
