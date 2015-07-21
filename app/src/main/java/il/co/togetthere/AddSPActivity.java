package il.co.togetthere;

import il.co.togetthere.db.AmazonClientManager;
import il.co.togetthere.db.DynamoDBManagerTask;
import il.co.togetthere.db.DynamoDBManagerType;
import il.co.togetthere.db.ServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.widget.ProfilePictureView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddSPActivity extends Activity {
	private Spinner mTypeList;
	private ArrayAdapter<String> mListAdapter;
	private ServiceProvider mSP;
	public static AmazonClientManager clientManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_sp);

		/**
		 * Full Screen Set UP
		 **/
		// Hide the status bar.
		//View decorView = getWindow().getDecorView();
		//int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		//decorView.setSystemUiVisibility(uiOptions);
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		//android.app.ActionBar actionBar = getActionBar();
		//actionBar.hide();

		// Define Font
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GOTHIC.TTF");

		/**
		 * Upper Bar
		 **/
		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.button_show_user_details_addNewSP);
		profilePictureView.setProfileId(LoginActivity.user.getID());
		profilePictureView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent infoIntent = new Intent(AddSPActivity.this,
						UserInfoActivity.class);
				AddSPActivity.this.startActivity(infoIntent);

			}
		});

		mSP = new ServiceProvider();
		clientManager = new AmazonClientManager(this);

		/**
		 * Titles
		 */
		TextView title = (TextView) findViewById(R.id.title_activity_add_sp);
		title.setTypeface(font);

		EditText addNewSPName = (EditText) findViewById(R.id.addNewSPName);
		addNewSPName.setTypeface(font);

		EditText addNewSPSPAddress = (EditText) findViewById(R.id.addNewSPSPAddress);
		addNewSPSPAddress.setTypeface(font);

		EditText addNewSPSPPhone = (EditText) findViewById(R.id.addNewSPSPPhone);
		addNewSPSPPhone.setTypeface(font);

		TextView title_addNewSPSpinner = (TextView) findViewById(R.id.title_addNewSPSpinner);
		title_addNewSPSpinner.setTypeface(font);

		EditText addNewSPDescription = (EditText) findViewById(R.id.addNewSPDescription);
		addNewSPDescription.setTypeface(font);

		EditText addNewSPDiscount = (EditText) findViewById(R.id.addNewSPDiscount);
		addNewSPDiscount.setTypeface(font);

		TextView addNewSPRankText = (TextView) findViewById(R.id.addNewSPRankText);
		addNewSPRankText.setTypeface(font);

		TextView textViewRank1 = (TextView) findViewById(R.id.textViewRank1);
		textViewRank1.setTypeface(font);

		TextView textViewRank2 = (TextView) findViewById(R.id.textViewRank2);
		textViewRank2.setTypeface(font);

		TextView textViewRank3 = (TextView) findViewById(R.id.textViewRank3);
		textViewRank3.setTypeface(font);

		TextView textViewRank4 = (TextView) findViewById(R.id.textViewRank4);
		textViewRank4.setTypeface(font);

		TextView textViewRank5 = (TextView) findViewById(R.id.textViewRank5);
		textViewRank5.setTypeface(font);

		/**
		 * Type List Chooser
		 **/
		mTypeList = (Spinner) findViewById(R.id.addNewSPSpinner);
		String[] types = getResources().getStringArray(R.array.types);
		ArrayList<String> typesList = new ArrayList<>();
		typesList.add(0, "Choose Type");
		typesList.addAll(Arrays.asList(types));
		mListAdapter = new SpinnerAdapter(this, R.layout.type_list_item,
				typesList);
		mTypeList.setAdapter(mListAdapter);

		mTypeList.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

			@Override
			public void onItemSelected(AdapterView<?> adapter, View v,
					int position, long id) {
				String[] types = getResources().getStringArray(R.array.types);
				if (position != 0) {
					if (position == 3) {
						mSP.setType("PublicServices");
					} else {
						mSP.setType(types[position - 1]);
					}
				} else {
					mSP.setType("");
				}
			}

		});

		/**
		 * Submit Button- Validation
		 **/
		Button submit = (Button) findViewById(R.id.addNewSPSubmit);
		submit.setTypeface(font);
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Get Fields Content
				String name = ((EditText) findViewById(R.id.addNewSPName))
						.getText().toString();
				String address = ((EditText) findViewById(R.id.addNewSPSPAddress))
						.getText().toString();
				String phone = ((EditText) findViewById(R.id.addNewSPSPPhone))
						.getText().toString();
				String description = ((EditText) findViewById(R.id.addNewSPDescription))
						.getText().toString();
				String discount = ((EditText) findViewById(R.id.addNewSPDiscount))
						.getText().toString();
				int discountNum = (discount.equals("") ? -1 : Integer
						.parseInt(discount));

				/**
				 * Set SP
				 **/

				// Name
				if (!name.equals("")) {
					mSP.setName(name);
				} else {
					Toast.makeText(getApplicationContext(),
							"Location name is missing",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// Adress
				if (!address.equals("")) {
					mSP.setAddress(address);
				} else {
					Toast.makeText(getApplicationContext(),
							"Location address is missing",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// Phone
				if (!phone.equals("")) {
					mSP.setPhone(phone);
				} else {
					Toast.makeText(getApplicationContext(),
							"Location phone is missing",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// Description
				if (!description.equals("")) {
					// mSP.setDescription(phone);
				}

				// Discount
				if (discountNum != -1) {
					mSP.setDiscount(discountNum);
				} else {
					Toast.makeText(getApplicationContext(),
							"Discount amount is missing",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// Type
				if (mSP.getType().equals("")) {
					Toast.makeText(getApplicationContext(),
							"You must select location type",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// Re-Validate User (Are You Sure?)
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
										int which) {
						switch (which) {
							case DialogInterface.BUTTON_POSITIVE:

								// OK message
								Toast.makeText(getApplicationContext(),
										"Thank You!", Toast.LENGTH_SHORT)
										.show();

								// send mSP to DB and finish;
								DynamoDBManagerTask addProvider = new DynamoDBManagerTask();
								addProvider.mSerivceProvider = mSP;
								addProvider
										.execute(DynamoDBManagerType.INSERT_SERVICE_PROVIDER
												.toString());

								// Close Activity
								AddSPActivity.this.finish();
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(
						AddSPActivity.this);
				builder.setMessage(
						"Are you sure you want to add " + name + "?")
						.setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener)
						.show();

			}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_s, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return (id == R.id.action_settings) ? true : super.onOptionsItemSelected(item);
	}

	public void rankClicked(View v) {
		String rankType = v.getTag().toString();
		if (v.getAlpha() == 0.25) {
			v.setAlpha(1);
			updateServiceProviderRank(rankType, true);
		} else {
			v.setAlpha((float) 0.25);
			updateServiceProviderRank(rankType, false);
		}
	}

	private void updateServiceProviderRank(String rankType, boolean b) {
		// TODO Auto-generated method stub
	}

	private static class SpinnerAdapter extends ArrayAdapter<String> {
		// Initialise custom font, for example:
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/GOTHIC.TTF");


		// (In reality I used a manager which caches the Typeface objects)
		// Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

		private SpinnerAdapter(Context context, int resource, List<String> items) {
			super(context, resource, items);
		}

		// Affects default (closed) state of the spinner
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView) super.getView(position, convertView, parent);
			view.setTypeface(font);
			return view;
		}

		// Affects opened state of the spinner
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView) super.getDropDownView(position, convertView, parent);
			view.setTypeface(font);
			return view;
		}
	}
}
