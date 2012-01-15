package de.fhb.mobile.ToDoListAndroidApp;

import de.fhb.mobile.ToDoListAndroidApp.R;
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

public class LoginActivity extends Activity {
	
	// the ui elements
	private EditText mailField;
	private EditText passwordField;
	private Button loginButton;
	private TextView errorField;
	
	// the model elements
	private String mailInputValue = null;
	private String passwordInputValue = null;
	
	private boolean correctLogIn = false;
	
	// validitiy of editText1 and editText2
	private boolean validMailInput = true;
	private boolean validPasswordInput = true;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set the list view as content view
        setContentView(R.layout.login);
        
        // init the ui elements
        errorField = (TextView)findViewById(R.id.errorField);
        initMailField();
        initPasswordField();
        initLoginButton();
    }
    
    private void initMailField(){
    	mailField = (EditText)findViewById(R.id.mailField);
    	mailField.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    	
        mailField.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus == false)
					processMailInput(mailField.getText().toString());
			}
		});
        
        mailField.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_NEXT) {
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
    
    private void initPasswordField(){
    	passwordField = (EditText)findViewById(R.id.passwordField);
        passwordField.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus == false)
					processPasswordInput(passwordField.getText().toString());
			}
		});
        
        passwordField.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE) {
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
    
    private void initLoginButton(){
    	loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setEnabled(false);
        
        loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Object>() {

					private ProgressDialog dialog = null;
					
					@Override
					protected void onPreExecute() {
						dialog = ProgressDialog.show(LoginActivity.this,
								"Bitte warten Sie...", "während Ihre Daten überprüft werden.");
					}
					
					// the background process
					@Override
					protected Object doInBackground(Void... params) {
						try {
							Thread.sleep(1500);
							if(mailInputValue.equals("t@t.t") && passwordInputValue.equals("123456")) {
								startActivity(new Intent(LoginActivity.this, ToDoListActivity.class));
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
						if(correctLogIn == false) {
							errorField.setText("Falsche E-Mail/Falsches Password");
							validMailInput = false;
							validPasswordInput = false;
						}
					}
					
				}.execute();
			}
		});
    }
    
    private void processMailInput(String text) {
    	// if we have an invalid input, we display an error message...
    	if(text.indexOf('.') == -1 || text.indexOf('@') == -1) {
    		errorField.setText("Bitte geben Sie eine gültige E-Mail Adresse ein.");
    		validMailInput = false;
    	} else {
    		validMailInput = true;
    		mailInputValue = text;
    	}
    }
    
    private void processMailInputChanged() {
    	if(mailInputValue != null) {
    		mailInputValue = null;
    	}
    	
    	if(!validMailInput) {
    		validMailInput = true;
    		errorField.setText("");
    	}
    	updateLoginButtonState();
    }
    
    private void processPasswordInput(String text) {
    	// if we have an invalid input, we display an error message...
		if (text.length() < 6) {
    		errorField.setText("Bitte geben Sie 6 Zeichen ein.");
    		validPasswordInput = false;
		} else {
			validPasswordInput = true;
			passwordInputValue = text;
		}
		updateLoginButtonState();
    }
    
    private void processPasswordInputChanged() {
    	if(passwordInputValue != null) {
    		passwordInputValue = null;
    	}
    	
    	if(!validPasswordInput) {
    		validPasswordInput = true;
    		errorField.setText("");
    	}
    	updateLoginButtonState();
    }
    
    private void updateLoginButtonState() {
		loginButton.setEnabled(validMailInput && validPasswordInput
				&& mailInputValue != null && passwordInputValue != null);
    }
}