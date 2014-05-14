package edu.kaist.nclab.messagecollector;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;


public class CollectingService extends AccessibilityService {
  
	@Override
	public void onCreate() {
		Log.v(TAG, "onCreate");
		Toast.makeText(getApplicationContext(), "Anne : bis spaeter", Toast.LENGTH_LONG).show();
		super.onCreate();
		
	}

	public static final String TAG = "CollectingService";
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		Log.v(TAG, "***** onAccessibilityEvent with Text "+ event.getText() + " package: "+event.getPackageName());
		CharSequence charPackageName = event.getPackageName();
		String eventPackageName;
		
		if (charPackageName == null) {
			eventPackageName = "ep";
		} else {
			eventPackageName = charPackageName.toString();
		}
		// IF it is a Kakao Talk Message Event of length >= 3
		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
			&& event.getText().toString().length() >= 3
			&& eventPackageName.contentEquals("com.kakao.talk")) {
			
			// Then collect the following data
			long eventTime = event.getEventTime();
			String eventText = event.getText().toString();
			//getSystemService(Context.LOCATION_SERVICE)
			// And log them to LogCat
			Log.v(TAG, "Message: "+ eventText + " at: "+ eventTime);
			
		}
	}

	@Override
	public void onInterrupt() {
		Log.v(TAG, "***** onInterrupt");
		
	}
	
	@Override
    public void onServiceConnected()
    {
        Log.v(TAG, "***** onServiceConnected");

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.notificationTimeout = 100;
        info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
        setServiceInfo(info);

    }

}
