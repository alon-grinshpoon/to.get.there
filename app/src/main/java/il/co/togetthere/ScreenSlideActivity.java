/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package il.co.togetthere;

import il.co.togetthere.db.AsyncResponse;
import il.co.togetthere.db.DynamoDBManagerTask;
import il.co.togetthere.db.DynamoDBManagerTaskResult;
import il.co.togetthere.db.DynamoDBManagerType;
import il.co.togetthere.db.ServiceProvider;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

/**
 * @see ScreenSlidePageFragment
 */
public class ScreenSlideActivity extends FragmentActivity implements
		AsyncResponse {
	DynamoDBManagerTask mThread = new DynamoDBManagerTask();
	private static int NUM_PAGES = 1;
	// current service provider type
	private static String mServiceProviderType;
	private int mCurr;
	public static ArrayList<ServiceProvider> mServiceProviderArr;
	public static ArrayList<Task> mTaskArr;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);

		// Start to import data from DB
		mThread.delegate = this;
		mCurr = -1;

		/**
		 * View Initialization
		 **/
		
		// Hide the status bar.
		//View decorView = getWindow().getDecorView();
		//int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		//decorView.setSystemUiVisibility(uiOptions);
		
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		//android.app.ActionBar actionBar = getActionBar();
		//actionBar.hide();

		/**
		 * Set Profile Picture
		 **/
		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.button_show_user_details_slide);
		profilePictureView.setProfileId(LoginActivity.user.getID());

		profilePictureView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent infoIntent = new Intent(ScreenSlideActivity.this,
						UserInfoActivity.class);
				ScreenSlideActivity.this.startActivity(infoIntent);

			}
		});

		/**
		 * Get Values
		 **/
		Intent inIntent = getIntent();
		setmServiceProviderType(inIntent.getStringExtra("TYPE_EXTRA"));

		if (getmServiceProviderType() != null
				&& getmServiceProviderType().equals("help")) {
			mThread.execute(DynamoDBManagerType.GET_TASKS.toString());
		} else {
			mThread.execute(DynamoDBManagerType.GET_PROVIDER.toString(),
					ServiceProvider.stringToEnum(getmServiceProviderType())
							.toString());
		}

		/**
		 * Set Lower Bar Functions
		 */
		((LinearLayout) findViewById(R.id.button_add_new_location))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent addNewIntent;
						if (getmServiceProviderType().equals("help")) {
							addNewIntent = new Intent(ScreenSlideActivity.this,
									AddNewTaskActivity.class);
							ScreenSlideActivity.this
									.startActivity(addNewIntent);
						} else {
							addNewIntent = new Intent(ScreenSlideActivity.this,
									AddSPActivity.class);
							ScreenSlideActivity.this
									.startActivity(addNewIntent);
						}

					}
				});

		// TODO "Rank" button Handler

		LinearLayout rankButton = (LinearLayout) findViewById(R.id.button_rank);
		if (getmServiceProviderType().equals("help")) {
			rankButton.setVisibility(View.INVISIBLE);
		} else {
			rankButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// TODO Rank Dialog

				}
			});
		}

		LinearLayout editButton = (LinearLayout) findViewById(R.id.button_edit);
		if (getmServiceProviderType().equals("help")) {
			editButton.setVisibility(View.INVISIBLE);
		} else {
			editButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent editIntent = null;
					editIntent = new Intent(ScreenSlideActivity.this,
							EditActivity.class);
					editIntent.putExtra("SP_NUMBER", mCurr);
					ScreenSlideActivity.this.startActivity(editIntent);

					// TODO - update SP when editing is finished
				}
			});
		}

		
		// Titles
		TextView titleRank = ((TextView) findViewById(R.id.button_rank_title));
		TextView titleAdd = ((TextView) findViewById(R.id.button_add_new_location_title));
		TextView titleEdit = ((TextView) findViewById(R.id.button_edit_title));
						
		// Define Font
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GOTHIC.TTF");
		titleRank.setTypeface(font);
		titleAdd.setTypeface(font);
		titleEdit.setTypeface(font);

	}

	/**
	 * View Pager
	 **/
	public void showPager() {
		((ProgressBar) findViewById(R.id.progress)).setVisibility(View.GONE);
		mPager = (ViewPager) findViewById(R.id.pager);

		if (getmServiceProviderType().equals("help")) {
			NUM_PAGES = mTaskArr.size();
			mCurr = 0;
			mPagerAdapter = new ScreenSlidePagerAdapterTask(
					getFragmentManager());
		} else {
			NUM_PAGES = mServiceProviderArr.size();
			mCurr = 0;
			mPagerAdapter = new ScreenSlidePagerAdapterSP(getFragmentManager());
		}

		mPager.setAdapter(mPagerAdapter);

		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mCurr = position;
				Log.i("Position", "Position is" + position);
				invalidateOptionsMenu();
			}
		});

	}

	/**
	 * Service Provider pager adapter
	 */
	private class ScreenSlidePagerAdapterSP extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapterSP(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ScreenSlidePageFragment.create(position,
					getmServiceProviderType());
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	public static ServiceProvider getCurrSP(int position) {
		return mServiceProviderArr.get(position);
	}

	/**
	 * Task Provider pager adapter
	 */
	private class ScreenSlidePagerAdapterTask extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapterTask(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ScreenSlidePageFragment.create(position,
					getmServiceProviderType());
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	public static Task getCurrTask(int position) {
		return mTaskArr.get(position);

	}

	@Override
	public void processFinish(DynamoDBManagerTaskResult result) {

		if (getmServiceProviderType() != null
				&& getmServiceProviderType().equals("help")) {
			mTaskArr = result.mTaskArray;
		} else {
			mServiceProviderArr = result.mServiceProviderArray;
		}
		Log.i("DB result", "db info loaded");
		showPager();

	}

	public static String getmServiceProviderType() {
		return mServiceProviderType;
	}

	public static void setmServiceProviderType(String mServiceProviderType) {
		ScreenSlideActivity.mServiceProviderType = mServiceProviderType;
	}
}
