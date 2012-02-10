package de.fhb.mobile.ToDoListAndroidApp.communication;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import de.fhb.mobile.shared.logging.Logger;
import de.fhb.mobile.shared.logging.LoggerFactory;

public class MyPhoneStateListener extends PhoneStateListener {

	protected static Logger logger = LoggerFactory
			.getLogger(MyPhoneStateListener.class);

	/**
	 * the context used for feedback
	 */
	private Context context;

	public MyPhoneStateListener(Context context) {
		this.context = context;
	}

	public void onCallStateChanged(int state, String incomingNumber) {

		logger.info("onCallStateChanged: state is: " + state
				+ ", incomingNumber is: " + incomingNumber);

		String msg;

		if (state == TelephonyManager.CALL_STATE_RINGING) {
			msg = incomingNumber + " is calling";
		} else if (state == TelephonyManager.CALL_STATE_IDLE) {
			msg = "call state is idle";
			// "CALL_STATE_OFFHOOK": Device call state: Off-hook. At least one
			// call exists that is dialing, active, or on hold, and no calls are
			// ringing or waiting, see
			// http://developer.android.com/reference/android/telephony/TelephonyManager.html#CALL_STATE_OFFHOOK
		} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
			msg = "call state is offhook";
		} else {
			msg = "unknown call state: " + state;
		}

		Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();

	}

}
