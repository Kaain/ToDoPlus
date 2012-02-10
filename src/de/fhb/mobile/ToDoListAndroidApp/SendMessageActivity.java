package de.fhb.mobile.ToDoListAndroidApp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.fhb.mobile.ToDoListAndroidApp.commons.AndroidContactsHelper;
import de.fhb.mobile.ToDoListAndroidApp.communication.GmailSender;
import de.fhb.mobile.ToDoListAndroidApp.communication.MyPhoneStateListener;
import de.fhb.mobile.ToDoListAndroidApp.communication.SMSDeliveryMonitor;
import de.fhb.mobile.ToDoListAndroidApp.communication.SMSSendingMonitor;
import de.fhb.mobile.ToDoListAndroidApp.exceptions.MailException;
import de.fhb.mobile.ToDoListAndroidApp.exceptions.SMSException;
import de.fhb.mobile.ToDoListAndroidApp.models.Contact;
import de.fhb.mobile.ToDoListAndroidApp.models.Todo;
import de.fhb.mobile.ToDoListAndroidApp.persistance.TodoDatabase;
import de.fhb.mobile.shared.logging.Logger;
import de.fhb.mobile.shared.logging.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class SendMessageActivity.
 */
public class SendMessageActivity extends Activity {

	/** the logger. */
	protected static Logger logger = LoggerFactory
			.getLogger(SendMessageActivity.class);

	/**
	 * constants for identifying the intents used for calling back this activity
	 * with status notifications.
	 */
	public static final String SMS_SENT = SendMessageActivity.class.getName()
			+ ".SMS_SENT";

	/** The Constant SMS_DELIVERED. */
	public static final String SMS_DELIVERED = SendMessageActivity.class
			.getName() + ".SMS_DELIVERED";

	/** the View elements. */
	private TextView contactName;

	/** The contact mail. */
	private EditText contactMail;

	/** The contact number. */
	private EditText contactNumber;

	/** The msg content. */
	private EditText msgContent;

	/** two receivers for tracking sms sending. */
	private BroadcastReceiver smsSendingMonitor;

	/** The sms delivery monitor. */
	private BroadcastReceiver smsDeliveryMonitor;

	/** the phone state listener. */
	private PhoneStateListener phoneStateListener;

	/** The db. */
	private TodoDatabase db;

	/** The actual todo. */
	private Todo actualTodo;

	/** The actual contact. */
	private Contact actualContact;

	/**
	 * onCreate.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sender);

		db = new TodoDatabase(this);
		db.open();

		long todoID = getIntent().getLongExtra(ToDoDetailsActivity.ARG_TODO_ID,
				-1);
		long contactID = getIntent().getLongExtra(
				ShowContactsActivity.ARG_CONTACT_ID, -2);

		actualTodo = db.getTodoById(todoID);
		actualContact = AndroidContactsHelper.getContact(getContentResolver(),
				contactID);

		// register a phonestatelistener
		this.phoneStateListener = new MyPhoneStateListener(this);
		((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
				.listen(this.phoneStateListener,
						PhoneStateListener.LISTEN_CALL_STATE);

		initViewComponents();
	}

	/**
	 * Inits the view components.
	 */
	private void initViewComponents() {

		contactName = (TextView) findViewById(R.id.message_contactName);
		contactName.setText("Message for " + actualContact.getDisplayName());

		contactMail = (EditText) findViewById(R.id.message_mail);
		contactMail.setText(actualContact.getMail());

		contactNumber = (EditText) findViewById(R.id.message_phoneNumber);
		contactNumber.setText(actualContact.getPhoneNumber());

		msgContent = (EditText) findViewById(R.id.messageText);
		msgContent.setText(getDefaultMessage());

		// then set onClick listeners on the action buttons
		findViewById(R.id.sendSMSAction).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						sendSMS(getNumber(), getMessageContent());
					}
				});

		findViewById(R.id.sendEmailAction).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						sendEmail(getMail(), getMessageContent());
					}
				});

		findViewById(R.id.placeCallAction).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						placeCall(getNumber());
					}
				});
	}

	/**
	 * Gets the default message.
	 * 
	 * @return the default message
	 */
	private String getDefaultMessage() {
		StringBuffer sb = new StringBuffer();
		sb.append("Todoname: " + actualTodo.getName());
		sb.append("\nDescription: " + actualTodo.getDescription());
		sb.append("\nExpires at: " + actualTodo.getExpireDateAsString());
		if (actualTodo.isFinished())
			sb.append("\nis finished: yes");
		else
			sb.append("\nis finished: no");
		return sb.toString();
	}

	/**
	 * Gets the mail address.
	 * 
	 * @return the mail
	 */
	protected String getMail() {
		return contactMail.getText().toString();
	}

	/**
	 * Gets the number.
	 * 
	 * @return the number
	 */
	private String getNumber() {
		return contactNumber.getText().toString();
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
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		Log.i(this.getClass().toString(), "onPause");
		super.onPause();
		db.close();
	}

	/**
	 * on finish we need to unregister the receivers.
	 */
	@Override
	public void finish() {
		if (this.smsDeliveryMonitor != null) {
			unregisterReceiver(this.smsDeliveryMonitor);
		}
		if (this.smsSendingMonitor != null) {
			unregisterReceiver(this.smsSendingMonitor);
		}

		// we also unregister the call state listener - look how this is done :)
		((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
				.listen(this.phoneStateListener, PhoneStateListener.LISTEN_NONE);

		super.finish();
	}

	/**
	 * get the content.
	 * 
	 * @return the message content
	 */
	protected String getMessageContent() {
		return msgContent.getText().toString();
	}

	/**
	 * send an sms.
	 * 
	 * @param receiver
	 *            the receiver
	 * @param message
	 *            the message
	 */
	protected void sendSMS(String receiver, String message) {

		final SmsManager manager = SmsManager.getDefault();
		logger.debug("using sms manager: " + manager);

		try {
			if (receiver.isEmpty())
				throw new SMSException("No phone number");
			else {
				// create two pending intents that specify callbacks to this
				// activity
				// given a status of sms sending/delivery
				PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
						new Intent(SMS_SENT), 0);

				PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
						new Intent(SMS_DELIVERED), 0);

				// associate the two monitors with the intents (using line
				// number
				// optimisation...)
				registerReceiver(
						this.smsSendingMonitor = new SMSSendingMonitor(),
						new IntentFilter(SMS_SENT));
				registerReceiver(
						this.smsDeliveryMonitor = new SMSDeliveryMonitor(),
						new IntentFilter(SMS_DELIVERED));

				// pass the intents to the sendTextMessage method
				manager.sendTextMessage(receiver, /** sender */
				null, message, sentPI, deliveredPI);
			}
		} catch (SMSException e) {
			Toast toast = Toast.makeText(this, e.getMessage(),
					Toast.LENGTH_SHORT);
			toast.show();
			e.printStackTrace();
		}

	}

	/**
	 * compose an sms.
	 * 
	 * @param receiver
	 *            the receiver
	 * @param message
	 *            the message
	 */
	// http://snipt.net/Martin/android-intent-usage/
	protected void composeSMS(String receiver, String message) {

		// the sms compose is identified by the tel: uri and the specified
		// action ACTION_SENDTO
		Uri smsUri = Uri.parse("smsto:" + receiver);
		Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
		smsIntent.putExtra("sms_body", message);

		startActivity(smsIntent);
	}

	/**
	 * send and email using the gmail emailer.
	 * 
	 * @param receiver
	 *            the receiver
	 * @param message
	 *            the message
	 */
	protected void sendEmail(String receiver, String message) {

		try {
			if (receiver.isEmpty())
				throw new MailException("No mail address");
			else {
				// forward the sms as email
				String user = "de.fhb.mobile@googlemail.com";
				GmailSender gmailer = new GmailSender(user, "brandenburg");
				try {
					gmailer.sendEmail("ToDoPlus: " + actualTodo.getName(),
							message, user, receiver);
					Toast.makeText(this, "Email has been sent.",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					Toast.makeText(this, "Exception: " + e, Toast.LENGTH_SHORT)
							.show();
				}
			}
		} catch (MailException e) {
			Toast toast = Toast.makeText(this, e.getMessage(),
					Toast.LENGTH_SHORT);
			toast.show();
			e.printStackTrace();
		}

	}

	/**
	 * compose an email using some app on the device.
	 * 
	 * @param receiver
	 *            the receiver
	 * @param message
	 *            the message
	 */
	protected void composeEmail(String receiver, String message) {
		GmailSender.composeEmail(this, "new email", message,
				"de.fhb.mobile@googlemail.com", receiver);
	}

	/**
	 * place a call.
	 * 
	 * @param receiver
	 *            the receiver
	 */
	protected void placeCall(String receiver) {
		String url = "tel:" + receiver;
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));

		startActivity(intent);
	}

}
