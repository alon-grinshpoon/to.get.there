package il.co.togetthere;
import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.db.ServiceProviderCategory;
import il.co.togetthere.db.Task;
import il.co.togetthere.server.AsyncRequest;
import il.co.togetthere.server.AsyncResponse;
import il.co.togetthere.server.AsyncResult;
import il.co.togetthere.server.Server;

import android.annotation.SuppressLint;
import java.util.List;
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
public class ScreenSlideActivity extends FragmentActivity implements
		AsyncResponse {
	private static int NUM_PAGES = 1;
	// current service provider type
	private static String serviceProviderCategory;
	public static int currentIndex;
	public static List<ServiceProvider> serviceProviderList;
	public static List<Task> taskList;

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
		setServiceProviderCategory(inIntent.getStringExtra("TYPE_EXTRA"));

		if (getServiceProviderCategory() != null
				&& getServiceProviderCategory().equals("help")) {
			//mThread.execute(DynamoDBManagerType.GET_TASKS.toString());
		} else if (getServiceProviderCategory().equals("search")) {
			AsyncRequest asyncRequest = new AsyncRequest(ScreenSlideActivity.this);
			String query = inIntent.getStringExtra("SEARCH_QUERY");
			asyncRequest.execute(Server.SERVER_ACTION_SEARCH_BY_STRING, query);
		} else {
			AsyncRequest asyncRequest = new AsyncRequest(ScreenSlideActivity.this);
			ServiceProviderCategory category = ServiceProviderCategory.stringToEnum(serviceProviderCategory);
			asyncRequest.execute(Server.SERVER_ACTION_GET_SPS_OF_CATEGORY, category);
		}

		/**
		 * Set Upper Bar Functions
		 */
		((LinearLayout) findViewById(R.id.button_add_new_location))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent addNewIntent;
						if (getServiceProviderCategory().equals("help")) {
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
		if (getServiceProviderCategory().equals("help")) {
			rankButton.setVisibility(View.INVISIBLE);
		} else {
			rankButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					PopupMenu popupMenu = new PopupMenu(ScreenSlideActivity.this, view);
					popupMenu.setOnMenuItemClickListener(new RankingListener(ScreenSlideActivity.this, serviceProviderList));
					popupMenu.inflate(R.menu.ranking_menu);
					popupMenu.show();
				}
			});
		}

		LinearLayout editButton = (LinearLayout) findViewById(R.id.button_edit);
		if (getServiceProviderCategory().equals("help")) {
			editButton.setVisibility(View.INVISIBLE);
		} else {
			editButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent editIntent = null;
					editIntent = new Intent(ScreenSlideActivity.this,
							EditActivity.class);
					editIntent.putExtra("SP_NUMBER", currentIndex);
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

		if (getServiceProviderCategory().equals("help")) {
			NUM_PAGES = taskList.size();
			currentIndex = 0;
			mPagerAdapter = new ScreenSlidePagerAdapterTask(
					getFragmentManager());
		} else {
			NUM_PAGES = serviceProviderList.size();
			currentIndex = 0;
			mPagerAdapter = new ScreenSlidePagerAdapterSP(getFragmentManager());
		}

		if (NUM_PAGES == 0){
			Toast.makeText(getApplicationContext(), "Sorry, No results to show.",
					Toast.LENGTH_LONG).show();
			finish();
		}

		mPager.setAdapter(mPagerAdapter);

		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				currentIndex = position;
				Log.i("Position", "Position is " + position);
				invalidateOptionsMenu();
			}
		});

	}

	@Override
	public void handleResult(AsyncResult result) {
		if (result.errored()){
			Toast.makeText(getApplicationContext(), "Oops! Failed to load " + serviceProviderCategory + "...",
					Toast.LENGTH_SHORT).show();
			finish();
		} else {
			if (getServiceProviderCategory() != null
					&& getServiceProviderCategory().equals("help")) {
				taskList = result.getTaskList();
			} else {
				serviceProviderList = result.getServiceProviderList();
			}
			showPager();
		}
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
					getServiceProviderCategory());
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	public static ServiceProvider getServiceProvider(int position) {
		return serviceProviderList.get(position);
	}

	public ServiceProvider getCurrentServiceProvider() {
		return serviceProviderList.get(currentIndex);
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
					getServiceProviderCategory());
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	public static Task getTask(int position) {
		return taskList.get(position);
	}

	public static Task getCurrentTask() {
		return taskList.get(currentIndex);
	}

	public static String getServiceProviderCategory() {
		return serviceProviderCategory;
	}

	public static void setServiceProviderCategory(String serviceProviderCategory) {
		ScreenSlideActivity.serviceProviderCategory = serviceProviderCategory;
	}
}
