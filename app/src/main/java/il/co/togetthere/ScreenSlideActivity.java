package il.co.togetthere;
import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.db.ServiceProviderCategory;
import il.co.togetthere.server.Server;

import java.io.IOException;
import java.util.List;

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
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;

/**
 * @see ScreenSlidePageFragment
 */
public class ScreenSlideActivity extends FragmentActivity {
	private static int NUM_PAGES = 1;
	// current service provider type
	private static String mServiceProviderType;
	private int mCurr;
	public static List<ServiceProvider> mServiceProviderArr;
	public static List<Task> mTaskArr;

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
		profilePictureView.setProfileId(LoginActivity.user.getFacebook_id());

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
			//mThread.execute(DynamoDBManagerType.GET_TASKS.toString());
		} else {
			//mThread.execute(DynamoDBManagerType.GET_PROVIDER.toString(), ServiceProvider.stringToEnum(getmServiceProviderType()).toString());
			try {
				ServiceProviderCategory category = ServiceProviderCategory.stringToEnum(mServiceProviderType);
				mServiceProviderArr = Server.getSPsOfCategory(category);
				showPager();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Oops! Failed to load all " + mServiceProviderType + "...",
						Toast.LENGTH_SHORT).show();
			}
		}

		/**
		 * Set Upper Bar Functions
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

		// Configure Settings Button
		findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PopupMenu popupMenu = new PopupMenu(ScreenSlideActivity.this, view);
				popupMenu.setOnMenuItemClickListener(new SettingListener(ScreenSlideActivity.this));
				popupMenu.inflate(R.menu.settings_menu);
				popupMenu.show();
			}
		});

		// Rank Button Handler
		LinearLayout rankButton = (LinearLayout) findViewById(R.id.button_rank);
		if (getmServiceProviderType().equals("help")) {
			rankButton.setVisibility(View.INVISIBLE);
		} else {
			rankButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					PopupMenu popupMenu = new PopupMenu(ScreenSlideActivity.this, view);
					popupMenu.setOnMenuItemClickListener(new RankingListener(ScreenSlideActivity.this));
					popupMenu.inflate(R.menu.ranking_menu);
					popupMenu.show();
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

	/*
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
	*/

	public static String getmServiceProviderType() {
		return mServiceProviderType;
	}

	public static void setmServiceProviderType(String mServiceProviderType) {
		ScreenSlideActivity.mServiceProviderType = mServiceProviderType;
	}
}
