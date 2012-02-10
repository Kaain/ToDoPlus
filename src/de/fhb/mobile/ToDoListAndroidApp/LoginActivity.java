package de.fhb.mobile.ToDoListAndroidApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
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

/**
 * The Class LoginActivity.
 */
public class LoginActivity extends Activity {

	// the ui elements
	/** The mail field. */
	private EditText mailField;

	/** The password field. */
	private EditText passwordField;

	/** The login button. */
	private Button loginButton;

	/** The error field. */
	private TextView errorField;

	/** The mail input value. */
	private String mailInputValue = null;

	/** The password input value. */
	private String passwordInputValue = null;

	/** The correct log in. */
	private boolean correctLogIn = false;

	// validitiy of editText1 and editText2
	/** The valid mail input. */
	private boolean validMailInput = true;

	/** The valid password input. */
	private boolean validPasswordInput = true;

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

		// init the ui elements
		errorField = (TextView) findViewById(R.id.errorField);
		initMailField();
		initPasswordField();
		initLoginButton();
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
								"Bitte warten Sie...",
								"während Ihre Daten überprüft werden.");
					}

					// the background process
					@Override
					protected Object doInBackground(Void... params) {
						try {
							Thread.sleep(1500);
							if (mailInputValue.equals("t@t.t")
									&& passwordInputValue.equals("123456")) {
								loadTodos();
								startActivity(new Intent(LoginActivity.this,
										ToDoListActivity.class));
								correctLogIn = true;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Object response) {
						dialog.cancel();
						if (correctLogIn == false) {
							errorField
									.setText("Falsche E-Mail/Falsches Password");
							validMailInput = false;
							validPasswordInput = false;
							loginButton.setEnabled(false);
						}
					}

				}.execute();
			}
		});
	}

	/**
	 * Load todos.
	 */
	private void loadTodos() {
		// TODO
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
			mailInputValue = text;
		}
	}

	/**
	 * Process mail input changed.
	 */
	private void processMailInputChanged() {
		if (mailInputValue != null) {
			mailInputValue = null;
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
			passwordInputValue = text;
		}
		updateLoginButtonState();
	}

	/**
	 * Process password input changed.
	 */
	private void processPasswordInputChanged() {
		if (passwordInputValue != null) {
			passwordInputValue = null;
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
				&& mailInputValue != null && passwordInputValue != null);
	}
}