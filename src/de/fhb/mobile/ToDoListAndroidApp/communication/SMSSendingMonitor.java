package de.fhb.mobile.ToDoListAndroidApp.communication;

import de.fhb.mobile.shared.logging.Logger;
import de.fhb.mobile.shared.logging.LoggerFactory;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

// http://mobiforge.com/developing/story/sms-messaging-android
public class SMSSendingMonitor extends BroadcastReceiver {

	protected static Logger logger = LoggerFactory
			.getLogger(SMSSendingMonitor.class);

	public void onReceive(Context context, Intent arg1) {

		logger.info("onReceive(): context is: " + context);

		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Toast.makeText(context, "SMS has been sent", Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT)
					.show();
			break;
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
