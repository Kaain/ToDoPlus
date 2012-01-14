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
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ToDoActivity extends Activity {
	
	// the ui elements
	private EditText editText1;
	private EditText editText2;
	private Button loginButton;
	private TextView textView1;
	
	// the model elements
	private String textInput1Value = null;
	private String textInput2Value = null;
	
	private boolean correctLogIn = false;
	
	// validitiy of editText1 and editText2
	boolean invalidTextInput1 = false;
	boolean invalidTextInput2 = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set the list view as content view
        setContentView(R.layout.main);
        
        // access the ui elements
        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        loginButton = (Button)findViewById(R.id.login);
        textView1 = (TextView)findViewById(R.id.textView1);
        
        loginButton.setEnabled(false);
        
        // constrain the input type on editText1
        editText1.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        
        // constrain the input type on editText2
//        editText2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        
		/*
		 * set two listeners on the editText1 which detect finalisation of
		 * input, indicated by IME_ACTION_NEXT and also listen to any key press
		 */
        editText1.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_NEXT) {
					String text = v.getText().toString();
					processTextInput1(text);
				}
				return false;
			}
		});
        
        editText1.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				processTextInput1Changed();
				return false;
			}
		});
        
		/*
		 * set two listeners on the editText2 which detect finalisation of
		 * input, indicated by IME_ACTION_DONE and also listen to any key press
		 */
        editText2.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE) {
					String text = v.getText().toString();
					processTextInput2(text);
				}
				return false;
			}
		});
        
        editText2.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				processTextInput2Changed();
				return false;
			}
		});
        
        loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Object>() {

					private ProgressDialog dialog = null;
					
					@Override
					protected void onPreExecute() {
						dialog = ProgressDialog.show(ToDoActivity.this,
								"Bitte warten Sie...", "während Ihre Daten überprüft werden.");
					}
					
					// the background process
					@Override
					protected Object doInBackground(Void... params) {
						try {
							Thread.sleep(1500);
							if(textInput1Value.equals("t@t.t") && textInput2Value.equals("123456")) {
								startActivity(new Intent(ToDoActivity.this, ToDoListActivity.class));
								correctLogIn = true;
//							} else {
//								textView1.setText("Falsche E-Mail/Falsches Password");
//								invalidTextInput1 = true;
//								invalidTextInput2 = true;
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
					
					@Override
					protected void onPostExecute(Object response) {
						dialog.cancel();
						if(correctLogIn == false) {
							textView1.setText("Falsche E-Mail/Falsches Password");
							invalidTextInput1 = true;
							invalidTextInput2 = true;
						}
					}
					
				}.execute();
/*
				if(textInput1Value.equals("w@x.m") && textInput2Value.equals("123456")) {
					startActivity(new Intent(ToDoActivity.this, ToDoListActivity.class));
				} else {
					textView1.setText("Falsche E-Mail/Falsches Password");
					invalidTextInput1 = true;
					invalidTextInput2 = true;
				}
*/
			}
		});

    }
    
    private void processTextInput1(String text) {
    	// if we have an invalid input, we display an error message...
    	if(text.indexOf('.') == -1 || text.indexOf('@') == -1) {
    		textView1.setText("Bitte geben Sie eine gültige E-Mail Adresse ein.");
    		invalidTextInput1 = true;
    	} else {
    		invalidTextInput1 = false;
    		textInput1Value = text;
    	}
    }
    
    private void processTextInput1Changed() {
    	if(textInput1Value != null) {
    		textInput1Value = null;
    	}
    	
    	if(invalidTextInput1) {
    		invalidTextInput1 = false;
    		textView1.setText("");
    	}
    	updateLoginButtonState();
    }
    
    private void processTextInput2(String text) {
    	// if we have an invalid input, we display an error message...
		if (text.length() < 6) {
    		textView1.setText("Bitte geben Sie 6 Zeichen ein.");
    		invalidTextInput2 = true;
		} else {
			invalidTextInput2 = false;
			textInput2Value = text;
		}
		updateLoginButtonState();
    }
    
    private void processTextInput2Changed() {
    	if(textInput2Value != null) {
    		textInput2Value = null;
    	}
    	
    	if(invalidTextInput2) {
    		invalidTextInput2 = false;
    		textView1.setText("");
    	}
    	updateLoginButtonState();
    }
    
    private void updateLoginButtonState() {
    	loginButton.setEnabled(invalidTextInput1 == false
    			&& invalidTextInput2 == false
    			&& textInput1Value != null
    			&& textInput2Value != null);
    }
}