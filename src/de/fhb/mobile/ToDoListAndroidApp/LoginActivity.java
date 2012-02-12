package de.fhb.mobile.ToDoListAndroidApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import de.fhb.mobile.ToDoListAndroidApp.communication.IServerCommunicationREST;
import de.fhb.mobile.ToDoListAndroidApp.communication.ServerCommunicationREST;
import de.fhb.mobile.ToDoListAndroidApp.exceptions.OneConnectToServerException;
import de.fhb.mobile.ToDoListAndroidApp.models.User;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoDatabase;

/**
 * The Class LoginActivity.
 */
public class LoginActivity extends Activity {
	
	public static final String ARG_SERVER_CONNECTION = "serverConnection";

	private TodoDatabase db;
	// the ui elements
	/** The mail field. */
	private EditText mailField;

	/** The password field. */
	private EditText passwordField;

	/** The login button. */
	private Button loginButton;

	/** The error field. */
	private TextView errorField;

	/** The correct log in. */
	private boolean correctLogIn = false;

	// validitiy of editText1 and editText2
	/** The valid mail input. */
	private boolean validMailInput = true;

	/** The valid password input. */
	private boolean validPasswordInput = true;

	private IServerCommunicationREST server;
	
	private User actualUser;

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the list view as content view
		setContentView(R.layout.login);

		db = new TodoDatabase(this);
		db.open();
		
		actualUser = new User();
		
		server = new ServerCommunicationREST();

		// init the ui elements
		errorField = (TextView) findViewById(R.id.errorField);
		initMailField();
		initPasswordField();
		initLoginButton();
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

	/**
	 * Inits the mail field.
	 */
	private void initMailField() {
		mailField = (EditText) findViewById(R.id.mailField);
		mailField.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

		mailField.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == false)
					processMailInput(mailField.getText().toString());
			}
		});

		mailField.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					String text = v.getText().toString();
					processMailInput(text);
				}
				return false;
			}
		});

		mailField.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				processMailInputChanged();
				return false;
			}
		});
	}

	/**
	 * Inits the password field.
	 */
	private void initPasswordField() {
		passwordField = (EditText) findViewById(R.id.passwordField);
		passwordField.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == false)
					processPasswordInput(passwordField.getText().toString());
			}
		});

		passwordField.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					String text = v.getText().toString();
					processPasswordInput(text);
				}
				return false;
			}
		});

		passwordField.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				processPasswordInputChanged();
				return false;
			}
		});
	}

	/**
	 * Inits the login button.
	 */
	private void initLoginButton() {
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setEnabled(false);

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Object>() {

					private ProgressDialog dialog = null;

					@Override
					protected void onPreExecute() {
						dialog = ProgressDialog.show(LoginActivity.this,
								"Please wait...",
								"while logging in ...");
					}

					// the background process
					@Override
					protected Object doInBackground(Void... params) {
						int isAuthenticated = server.authentification(
									actualUser.getUsername(), actualUser.getPassword());
						return isAuthenticated;
					}

					@Override
					protected void onPostExecute(Object response) {
						dialog.cancel();
						int isAuthenticated = (Integer) response;
						Intent intent = new Intent(LoginActivity.this,
								ToDoListActivity.class);
						db.createUser(actualUser);
						switch (isAuthenticated) {
						case 1:
							actualUser.setOneConnectWithServer(true);
							break;
						case -1:
							intent.putExtra(ARG_SERVER_CONNECTION, false);
							Toast toastTimeout = Toast.makeText(
									getApplicationContext(),
									"No connection to Server, login with local database",
									Toast.LENGTH_LONG);
							toastTimeout.show();
							break;
						case -2:
							intent.putExtra(ARG_SERVER_CONNECTION, false);
							Toast toastIO = Toast.makeText(
									getApplicationContext(),
									"Problem with connection to Server, login with local database",
									Toast.LENGTH_LONG);
							toastIO.show();
							break;
						}
						try {
							boolean isAuthDB;
							if (isAuthenticated != 0) {

								isAuthDB = db.authenticateUser(actualUser);

							} else {
								isAuthDB = false;
							}

							if (isAuthDB) {
								startActivity(intent);
							} else {
								errorField
										.setText("Falsche E-Mail/Falsches Password");
								validMailInput = false;
								validPasswordInput = false;
								loginButton.setEnabled(false);
							}

						} catch (OneConnectToServerException e) {
							Toast toastIO = Toast.makeText(
									getApplicationContext(), e.getMessage(),
									Toast.LENGTH_LONG);
							toastIO.show();
							e.printStackTrace();
						}

					}

				}.execute();
			}
		});
	}

	/**
	 * Process mail input.
	 * 
	 * @param text
	 *            the text
	 */
	private void processMailInput(String text) {
		// if we have an invalid input, we display an error message...
		if (text.indexOf('.') == -1 || text.indexOf('@') == -1) {
			errorField
					.setText("Bitte geben Sie eine gültige E-Mail Adresse ein.");
			validMailInput = false;
		} else {
			validMailInput = true;
			actualUser.setUsername(text);
		}
	}

	/**
	 * Process mail input changed.
	 */
	private void processMailInputChanged() {
		if (actualUser.getUsername() != null) {
			actualUser.setUsername(null);
		}

		if (!validMailInput) {
			validMailInput = true;
			errorField.setText("");
		}
		updateLoginButtonState();
	}

	/**
	 * Process password input.
	 * 
	 * @param text
	 *            the text
	 */
	private void processPasswordInput(String text) {
		// if we have an invalid input, we display an error message...
		if (text.length() < 6) {
			errorField
					.setText("Bitte geben Sie 6 Zeichen langes Password ein.");
			validPasswordInput = false;
		} else {
			validPasswordInput = true;
			actualUser.setPassword(text);
		}
		updateLoginButtonState();
	}

	/**
	 * Process password input changed.
	 */
	private void processPasswordInputChanged() {
		if (actualUser.getPassword() != null) {
			actualUser.setPassword(null);
		}

		if (!validPasswordInput) {
			validPasswordInput = true;
			errorField.setText("");
		}
		updateLoginButtonState();
	}

	/**
	 * Update login button state.
	 */
	private void updateLoginButtonState() {
		loginButton.setEnabled(validMailInput && validPasswordInput
				&& actualUser.getUsername() != null && actualUser.getPassword() != null);
	}
}