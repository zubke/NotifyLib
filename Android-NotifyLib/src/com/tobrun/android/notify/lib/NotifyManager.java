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
package com.tobrun.android.notify.lib;

import java.util.concurrent.LinkedBlockingQueue;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.android_notifylib.R;
import com.tobrun.android.notify.lib.notify.Notify;
import com.tobrun.android.notify.lib.widget.NotifyFlipper;
import com.tobrun.android.notify.lib.widget.NotifyFlipper.State;

/**
 * 
 * @author tvn
 * 
 */
public class NotifyManager extends Fragment implements OnClickListener {

	private LinkedBlockingQueue<Notify> mQueue;
	private Notify mCurrentNotify;
	private NotifyFlipper mNotifyFlipper;
	private NotifyCallback mNotifyCallback;

	private Handler mHandler;
	private boolean mRunning = false;
	private boolean isRestarting = false;

	private final int mIndex = 0;
	private final State mState = State.OPENING;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		// will not be called during a configuration change
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mQueue = new LinkedBlockingQueue<Notify>();
		mHandler = new Handler();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return mNotifyFlipper = (NotifyFlipper) inflater.inflate(R.layout.notify_flipper, container, false);
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mNotifyFlipper.restoreState(view.getContext(), mCurrentNotify, mQueue.peek(), mState, mIndex);
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		if (activity instanceof NotifyCallback) {
			mNotifyCallback = (NotifyCallback) activity;
		} else {
			throw new IllegalStateException("Activity should implement the Notify Interface.");
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (isActive()) {
			startRepeatingTask();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (isActive()) {
			stopRepeatingTask();
			isRestarting = true;
		}
	}

	public void add(final Notify notify) {

		if (mNotifyFlipper.isClosing()) {
			mNotifyFlipper.postDelayed(new Runnable() {
				@Override
				public void run() {
					addNotify(notify);
				}
			}, 500);
		} else {
			addNotify(notify);
		}

	}

	private synchronized void addNotify(final Notify notify) {
		mQueue.add(notify);
		if (!mRunning) {
			startRepeatingTask();
		}
	}

	private synchronized Notify pollNotify() {
		return mQueue.poll();
	}

	private synchronized void startRepeatingTask() {
		mRunning = true;
		mStatusChecker.run();
	}

	private synchronized void stopRepeatingTask() {
		mHandler.removeCallbacks(mStatusChecker);
		mRunning = false;
	}

	private final boolean isActive() {
		return (mCurrentNotify != null) || (mQueue.size() > 0);
	}

	private final Runnable mStatusChecker = new Runnable() {
		@Override
		public void run() {
			if (!isRestarting) {
				mCurrentNotify = pollNotify();
			} else {
				isRestarting = false;
			}
			mNotifyFlipper.showNotify(getActivity(), mCurrentNotify);
			if (mCurrentNotify != null) {
				if (!mCurrentNotify.isSticky()) {
					mNotifyFlipper.setOnClickListener(null);
					mHandler.postDelayed(mStatusChecker, mCurrentNotify.getDuration());
				} else {
					mNotifyFlipper.setOnClickListener(NotifyManager.this);
				}
			} else {
				stopRepeatingTask();
			}

		}
	};

	@Override
	public void onClick(final View v) {
		mHandler.post(mStatusChecker);
		mNotifyCallback.onNotifyDismiss(mCurrentNotify);
	}

}
