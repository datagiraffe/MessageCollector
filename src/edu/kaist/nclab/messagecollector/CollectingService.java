package edu.kaist.nclab.messagecollector;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class CollectingService extends AccessibilityService {


	//ActivityManager actMng = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
	@Override
	public void onCreate() {
		Log.v(TAG, "onCreate");
		Toast.makeText(getApplicationContext(), "Anne : bis spaeter",
				Toast.LENGTH_LONG).show();
		super.onCreate();

	}

	/**
	 * Returns the name of the application that is currently in the foreground.
	 * If it is not possible, if that Applications Package name cannot be found,
	 * this method returns the String "unknown"
	 * 
	 * @return the name of the foreground application
	 */
	private String getForegroundApplicationName() {
		ActivityManager actMng = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		// The first in the list of RunningTasks is always the foreground task.
		RunningTaskInfo foregroundTaskInfo = actMng.getRunningTasks(1).get(0);
		
		// http://stackoverflow.com/questions/2166961/determining-the-current-foreground-application-from-a-background-task-or-service
		String foregroundTaskPackageName = foregroundTaskInfo .topActivity.getPackageName();
		PackageManager pm = this.getPackageManager();
		PackageInfo foregroundAppPackageInfo;
		String foregroundTaskAppName;
		try {
			foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
			foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();
		} catch (NameNotFoundException e) {
			foregroundTaskAppName = "unknown";
		}
		
		return foregroundTaskAppName;
	}

	public static final String TAG = "CollectingService";

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		Log.v(TAG, "application name: "+getForegroundApplicationName());
		Log.v(TAG, "***** onAccessibilityEvent with Text " + event.getText()
				+ " package: " + event.getPackageName());
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
			// getSystemService(Context.LOCATION_SERVICE)
			// And log them to LogCat
			Log.v(TAG, "Message: " + eventText + " at: " + eventTime);

		}
	}

	@Override
	public void onInterrupt() {
		Log.v(TAG, "***** onInterrupt");

	}

	@Override
	public void onServiceConnected() {
		Log.v(TAG, "***** onServiceConnected");

		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
		info.notificationTimeout = 100;
		info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
		setServiceInfo(info);

	}

}
