/*
 * Copyright (C) 2013 Tobrun Van Nuland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tobrun.android.notify.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
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
				getNotifyManager().add(new Notify());
			}
		});

		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View arg0) {
				final Notify notify = new Notify();
				notify.setSticky(true);
				getNotifyManager().add(notify);
			}
		});
	}

	@Override
	public View onCreateNotifyView(final LayoutInflater inflater, final ViewGroup container) {
		return inflater.inflate(R.layout.notification, container);
	}

	@Override
	public void onNotifyViewCreated(final View view, final Notify notify) {
		final TextView textView = (TextView) view.findViewById(R.id.textView1);
		textView.setText(String.valueOf(notify.getId()));
	}

	@Override
	public void onNotifyDismiss(final Notify notify) {
		Toast.makeText(getApplicationContext(), "Notify was dismissed", Toast.LENGTH_SHORT).show();
	}

}
