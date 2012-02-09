package de.fhb.mobile.ToDoListAndroidApp.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import de.fhb.mobile.shared.logging.Logger;
import de.fhb.mobile.shared.logging.LoggerFactory;

public class SMSReceiver extends BroadcastReceiver {

	protected static Logger logger = LoggerFactory.getLogger(SMSReceiver.class);
	
	/**
	 * this is taken from the Darcey/Conder book
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle data = intent.getExtras();
		
		logger.info("received SMS: " + data);
		
		// read out the message and the sender
		String message = "";
		String sender = null;
		
		// read out the data
		Object[] pdus = (Object[]) data.get("pdus");
		for (Object pdu : pdus) {
			SmsMessage part = SmsMessage.createFromPdu((byte[])pdu);			
			message += part.getDisplayMessageBody();
			
			if (sender == null) {
				sender = part.getDisplayOriginatingAddress();
			}
		}
		
		Toast.makeText(context, "Received sms from sender: " + message, Toast.LENGTH_SHORT).show();
		
	}

}
