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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tobrun.android.notify.lib.notify.Notify;

/**
 * 
 * @author tvn
 * 
 */
public interface NotifyCallback {

	View onCreateNotifyView(LayoutInflater inflater, ViewGroup container);

	void onNotifyViewCreated(View view, Notify notify);

	void onNotifyDismiss(Notify notify);

}
