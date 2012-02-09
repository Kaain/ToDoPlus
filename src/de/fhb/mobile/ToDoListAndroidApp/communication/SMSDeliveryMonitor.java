package de.fhb.mobile.ToDoListAndroidApp.communication;

import de.fhb.mobile.shared.logging.Logger;
import de.fhb.mobile.shared.logging.LoggerFactory;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

//http://mobiforge.com/developing/story/sms-messaging-android
public class SMSDeliveryMonitor extends BroadcastReceiver {
	
	protected static Logger logger = LoggerFactory
			.getLogger(SMSDeliveryMonitor.class);
	
	@Override
    public void onReceive(Context context, Intent arg1) {
		
		logger.info("onReceive(): context is: " + context);
		
        switch (getResultCode())
        {
            case Activity.RESULT_OK:
                Toast.makeText(context, "SMS has been delivered", 
                        Toast.LENGTH_SHORT).show();
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(context, "SMS not delivered", 
                        Toast.LENGTH_SHORT).show();
                break;                        
        }
    }

}
