package com.tobrun.android.notify.sample;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.tobrun.android.notify.lib.NotifyActivity;
import com.tobrun.android.notify.lib.notify.Notify;

/**
 * WORK IN PROGRESS
 * 
 * @author tvn
 * 
 */
public class MainActivity extends NotifyActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View arg0) {
				getNotifyManager().add(new Notify("Hello, Toast like implementation", R.layout.notification));
			}
		});

		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View arg0) {
				final Notify notify = new Notify("Hello, Click on me to dismiss", R.layout.notification);
				notify.setIsSticky(true);
				getNotifyManager().add(notify);
			}
		});
	}

	@Override
	public void onNotifyDismiss(final Notify notify) {
		Toast.makeText(getApplicationContext(), "Notify was dismissed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNotifyViewCreated(final View view, final Notify notify) {
		// TODO Auto-generated method stub
	}

}
