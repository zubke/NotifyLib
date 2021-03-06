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
package com.tobrun.android.notify.lib.notify;

import java.util.concurrent.atomic.AtomicInteger;

public class Notify {

	private static AtomicInteger nextId = new AtomicInteger();
	private final int id;
	private final int mDuration = 5000;
	private boolean isSticky = false;

	public Notify() {
		id = nextId.incrementAndGet();
	}

	public int getDuration() {
		return mDuration;
	}

	public int getId() {
		return id;
	}

	public boolean isSticky() {
		return isSticky;
	}

	public void setSticky(final boolean sticky) {
		isSticky = sticky;
	}

}
