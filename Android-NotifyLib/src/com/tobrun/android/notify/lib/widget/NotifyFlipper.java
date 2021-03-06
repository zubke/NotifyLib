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
package com.tobrun.android.notify.lib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ViewAnimator;

import com.example.android_notifylib.R;
import com.tobrun.android.notify.lib.NotifyCallback;
import com.tobrun.android.notify.lib.notify.Notify;

/**
 * 
 * @author tvn
 * 
 */
public class NotifyFlipper extends ViewAnimator {

	public final static int OPENING = 1;
	public final static int CLOSING = 2;

	private State mState = State.CLOSED;

	private ViewGroup mCurrentView;
	private ViewGroup mNextView;
	private NotifyCallback mNotifyCallback;

	public enum State {
		OPENING(NotifyFlipper.OPENING), OPENEND(View.VISIBLE), CLOSING(NotifyFlipper.CLOSING), CLOSED(View.GONE);

		private int mViewState;

		private State(final int value) {
			mViewState = value;
		}

		public int getState() {
			return mViewState;
		}

	}

	public NotifyFlipper(final Context context) {
		super(context);
		init(context);
	}

	public NotifyFlipper(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(final Context context) {
		if (context instanceof NotifyCallback) {
			mNotifyCallback = (NotifyCallback) context;
		} else {
			throw new IllegalStateException("Context should implement the NotifyCallback interface");
		}
		setInAnimation(context, R.anim.in_from_right);
		setOutAnimation(context, R.anim.out_to_left);
		addView(mCurrentView = generateContainer(context));
		addView(mNextView = generateContainer(context));
	}

	private ViewGroup generateContainer(final Context context) {
		final FrameLayout out = new FrameLayout(context);
		final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		out.setLayoutParams(params);
		return out;
	}

	public static BitmapDrawable createShadedBackground(final Context context, final int drawableId) {
		final Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), drawableId);
		final BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bmp);
		bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
		return bitmapDrawable;
	}

	public void close(final Context context) {
		final Animation outAnim = AnimationUtils.loadAnimation(context, R.anim.out_to_bottom);
		outAnim.setAnimationListener(new AnimationListenerHideView());
		startAnimation(outAnim);
	}

	public void open(final Context context) {
		setVisibility(View.VISIBLE);
		final Animation inAnim = AnimationUtils.loadAnimation(context, R.anim.in_from_bottom);
		inAnim.setAnimationListener(new AnimationListenerShowView());
		startAnimation(inAnim);
	}

	private void restoreDisplayStateAnimation(final int index) {
		final Animation animation = getInAnimation();
		setInAnimation(null);
		setDisplayedChild(index);
		setInAnimation(animation);
	}

	public void restoreState(final Context context, final Notify notify, final Notify nextNotify, final State state,
			final int index) {
		restoreDisplayStateAnimation(index);
		if (notify != null) {
			switch (state) {
			case CLOSED:
				setVisibility(View.GONE);
				break;
			case CLOSING:
				setVisibility(View.GONE);
				break;
			case OPENEND:
				setVisibility(View.VISIBLE);
				break;
			case OPENING:
				setVisibility(View.VISIBLE);
				break;
			default:
				throw new IllegalStateException(
						"Exeception caught while invoking restoreNotify, A valid state int should be provided. See State-enum for correct values.");
			}
			showNotify(context, notify);
		} else {
			mState = State.CLOSED;
		}
	}

	public void inflateNotify(final Context context, final Notify notify) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		if (isClosed()) {
			if (getDisplayedChild() == 0) {
				if (mCurrentView.getChildCount() > 0) {
					mCurrentView.removeAllViews();
				}
				handleCreateNotify(inflater, mCurrentView, notify);
			} else {
				if (mNextView.getChildCount() > 0) {
					mNextView.removeAllViews();
				}
				handleCreateNotify(inflater, mNextView, notify);
			}
		} else {
			// isOpenend()
			if (getDisplayedChild() == 0) {
				if (mNextView.getChildCount() > 0) {
					mNextView.removeAllViews();
				}
				handleCreateNotify(inflater, mNextView, notify);
			} else {
				if (mCurrentView.getChildCount() > 0) {
					mCurrentView.removeAllViews();
				}
				handleCreateNotify(inflater, mCurrentView, notify);
			}
		}
	}

	public void handleCreateNotify(final LayoutInflater inflater, final ViewGroup container, final Notify notify) {
		final View view = mNotifyCallback.onCreateNotifyView(inflater, container);
		if (view != null) {
			mNotifyCallback.onNotifyViewCreated(view, notify);
		}
	}

	public void showNotify(final Context context, final Notify notify) {
		if (notify == null) {
			// No notifies to be shown
			if (isOpenend()) {
				close(context);
			}
		} else {
			// Notifies found
			inflateNotify(context, notify);
			if (isClosed()) {
				open(context);
			} else {
				if (getDisplayedChild() == 0) {
					setDisplayedChild(1);
				} else {
					setDisplayedChild(0);
				}
			}
		}
	}

	public final boolean isCurrentView() {
		return getDisplayedChild() == 0;
	}

	public final boolean isNextView() {
		return getDisplayedChild() == 1;
	}

	public int getState() {
		return mState.getState();
	}

	public boolean isClosed() {
		return mState == State.CLOSED;
	}

	public boolean isOpenend() {
		return mState == State.OPENEND;
	}

	public boolean isOpening() {
		return mState == State.OPENING;
	}

	public boolean isClosing() {
		return mState == State.CLOSING;
	}

	class AnimationListenerHideView implements AnimationListener {

		@Override
		public void onAnimationEnd(final Animation animation) {
			mState = State.CLOSED;
			NotifyFlipper.this.setVisibility(View.GONE);
		}

		@Override
		public void onAnimationRepeat(final Animation animation) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onAnimationStart(final Animation animation) {
			mState = State.CLOSING;
		}
	}

	class AnimationListenerShowView implements AnimationListener {

		@Override
		public void onAnimationEnd(final Animation animation) {
			mState = State.OPENEND;
			NotifyFlipper.this.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(final Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(final Animation animation) {
			mState = State.CLOSED;
		}

	}

	public View getCurrentViews() {
		return mCurrentView;
	}
}
