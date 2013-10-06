package com.tobrun.android.notify.lib;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * 
 * @author tvn
 * 
 */
public abstract class NotifyActivity extends FragmentActivity implements NotifyCallback {

	private NotifyManager mNotifyManager;
	private final static String TAG_NOTIFY = "notifylib";

	@Override
	protected void onCreate(final Bundle arg0) {
		super.onCreate(arg0);
		final FragmentManager fm = getSupportFragmentManager();
		mNotifyManager = (NotifyManager) fm.findFragmentByTag(TAG_NOTIFY);
		// If the NotifyManager is non-null, then it is currently being
		// retained across a configuration change.
		if (mNotifyManager == null) {
			mNotifyManager = new NotifyManager();
			fm.beginTransaction().add(android.R.id.content, mNotifyManager, TAG_NOTIFY).commit();
		}
	}

	protected NotifyManager getNotifyManager() {
		if ((mNotifyManager == null) && mNotifyManager.isAdded()) {
			throw new IllegalStateException(
					"NotifyMangerFragment wasn't correctly added to the Activity, This method can only be called after onCreate from NotifyActivity is called.");
		}
		return mNotifyManager;
	}

}
