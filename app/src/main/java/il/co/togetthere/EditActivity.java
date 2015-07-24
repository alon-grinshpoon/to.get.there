package il.co.togetthere;

import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.server.AsyncRequest;
import il.co.togetthere.server.AsyncResponse;
import il.co.togetthere.server.AsyncResult;
import il.co.togetthere.server.Server;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;

public class EditActivity extends Activity implements AsyncResponse {
	private ServiceProvider mSP;
	private int pageNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		/**
		 * Full Screen Set UP
		 **/
		// Hide the status bar.
		// View decorView = getWindow().getDecorView();
		// int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		// decorView.setSystemUiVisibility(uiOptions);
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		//android.app.ActionBar actionBar = getActionBar();
		//actionBar.hide();

		/**
		 * Upper Bar
		 **/
		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.button_show_user_details_editSP);
		profilePictureView.setProfileId(LoginActivity.user.getFacebook_id());
		profilePictureView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent infoIntent = new Intent(EditActivity.this,
						UserInfoActivity.class);
				EditActivity.this.startActivity(infoIntent);

			}
		});

		// Configure Settings Button
		findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PopupMenu popupMenu = new PopupMenu(EditActivity.this, view);
				popupMenu.setOnMenuItemClickListener(new SettingListener(EditActivity.this));
				popupMenu.inflate(R.menu.settings_menu);
				popupMenu.show();
			}
		});

		Intent inIntent = getIntent();
		pageNumber = inIntent.getIntExtra("SP_NUMBER", 1);
		Log.i("Position", "Position is" + pageNumber);
		mSP = ScreenSlideActivity.getServiceProvider(pageNumber);

		// Define Font
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GOTHIC.TTF");

		/**
		 * Title
		 */
		TextView title = ((TextView) findViewById(R.id.title_activity_edit_sp));
		title.setTypeface(font);

		TextView titleName = ((TextView) findViewById(R.id.title_editSPName));
		titleName.setTypeface(font);
		EditText name = ((EditText) findViewById(R.id.editSPName));
		name.setText(mSP.getSp_name());
		name.setTypeface(font);

		TextView titleAddress = ((TextView) findViewById(R.id.title_editSPSPAddress));
		titleAddress.setTypeface(font);
		EditText address = ((EditText) findViewById(R.id.editSPSPAddress));
		address.setText(mSP.getAddress());
		address.setTypeface(font);

		TextView titlePhone = ((TextView) findViewById(R.id.title_editSPPhone));
		titlePhone.setTypeface(font);
		EditText phone = ((EditText) findViewById(R.id.editSPPhone));
		phone.setTypeface(font);
		phone.setText(mSP.getPhone());

		TextView titleWebsite = ((TextView) findViewById(R.id.title_editSPWebsite));
		titleWebsite.setTypeface(font);
		EditText website = ((EditText) findViewById(R.id.editSPWebsite));
		website.setTypeface(font);
		website.setText(mSP.getWebsite());

		TextView titleDiscount = ((TextView) findViewById(R.id.title_editSPDiscount));
		titleDiscount.setTypeface(font);
		EditText discount = ((EditText) findViewById(R.id.editSPDiscount));
		discount.setText("" + mSP.getDiscount());
		discount.setTypeface(font);

		/**
		 * Accessibility Parameters
		 */
		final ImageButton parking = (ImageButton) findViewById(R.id.NewRankImageParking);
		final ImageButton entrance = (ImageButton) findViewById(R.id.NewRankImageEntrance);
		final ImageButton facilities = (ImageButton) findViewById(R.id.NewRankImageFurniture);
		final ImageButton toilets = (ImageButton) findViewById(R.id.NewRankImageToilets);
		final ImageButton elevator = (ImageButton) findViewById(R.id.NewRankImageElevator);

		if(mSP.isParking()) rankToggle(parking);
		if(mSP.isEntrance()) rankToggle(entrance);
		if(mSP.isFacilities()) rankToggle(facilities);
		if(mSP.isToilets()) rankToggle(toilets);
		if(mSP.isElevator()) rankToggle(elevator);

		parking.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSP.setParking(!mSP.isParking());
				rankToggle(view);
			}
		});

		entrance.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSP.setEntrance(!mSP.isEntrance());
				rankToggle(view);
			}
		});

		facilities.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSP.setFacilities(!mSP.isFacilities());
				rankToggle(view);
			}
		});

		toilets.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSP.setToilets(!mSP.isToilets());
				rankToggle(view);
			}
		});

		elevator.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSP.setElevator(!mSP.isElevator());
				rankToggle(view);
			}
		});

		/**
		 * Submit Button- Validation
		 **/
		Button submit =  (Button) findViewById(R.id.editSPSubmit);
		submit.setTypeface(font);
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Get Fields Content
				String name = ((EditText) findViewById(R.id.editSPName))
						.getText().toString();
				String address = ((EditText) findViewById(R.id.editSPSPAddress))
						.getText().toString();
				String phone = ((EditText) findViewById(R.id.editSPPhone))
						.getText().toString();
				String website = ((EditText) findViewById(R.id.editSPWebsite))
						.getText().toString();
				String discount = ((EditText) findViewById(R.id.editSPDiscount))
						.getText().toString();
				int discountNum = (discount.equals("") ? -1 : Integer
						.parseInt(discount));

				/**
				 * Set SP
				 **/

				// Name
				if (!name.equals("")) {
					mSP.setSp_name(name);
				} else {
					Toast.makeText(getApplicationContext(),
							"Location name is missing",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// Address
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

				// Website (Can edit with no website)
				/* if (!website.equals("")) {
					mSP.setWebsite(website);
				} else {
					Toast.makeText(getApplicationContext(),
							"Location website is missing",
							Toast.LENGTH_SHORT).show();
					return;
				}*/

				// Discount
				if (discountNum != -1) {
					mSP.setDiscount(discountNum);
				} else {
					Toast.makeText(getApplicationContext(),
							"Discount amount is missing",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// Parameters Descriptions
				String parkingDescription = ((EditText) findViewById(R.id.editViewRank1))
						.getText().toString();
				String entranceDescription = ((EditText) findViewById(R.id.editViewRank2))
						.getText().toString();
				String furnitureDescription = ((EditText) findViewById(R.id.editViewRank3))
						.getText().toString();
				String toiletsDescription = ((EditText) findViewById(R.id.editViewRank4))
						.getText().toString();
				String elevatorDescription = ((EditText) findViewById(R.id.editViewRank5))
						.getText().toString();

				if(!parkingDescription.equals("")){
					mSP.setParking_text(mSP.getParking_text() + "." + parkingDescription);
				}
				if(!entranceDescription.equals("")){
					mSP.setParking_text(mSP.getEntrance_text() + "." + entranceDescription);
				}
				if(!furnitureDescription.equals("")){
					mSP.setParking_text(mSP.getFacilities_text() + "." + furnitureDescription);
				}
				if(!toiletsDescription.equals("")){
					mSP.setParking_text(mSP.getToilets_text() + "." + toiletsDescription);
				}
				if(!elevatorDescription.equals("")){
					mSP.setParking_text(mSP.getElevator_text() + "." + elevatorDescription);
				}


				// Re-Validate User (Are You Sure?)
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
										int which) {
						switch (which) {
							case DialogInterface.BUTTON_POSITIVE:

								// update mSP in DB and finish;
								AsyncRequest asyncRequest = new AsyncRequest(EditActivity.this);
								asyncRequest.execute(Server.SERVER_ACTION_EDIT_SP, mSP);
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(
						EditActivity.this);
				builder.setMessage(
						"Are you sure you want to edit " + name + "?")
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

	public void rankToggle(View v) {
		if (v.getAlpha() == 0.25) {
			v.setAlpha(1);
		} else {
			v.setAlpha((float) 0.25);
		}
	}

	@Override
	public void handleResult(AsyncResult result) {
		if (result.errored()){
			Toast.makeText(getApplicationContext(), "Oops! Unable to edit a new entry.",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), "Great! " + mSP.getSp_name() + " was updated.",
					Toast.LENGTH_SHORT).show();
			// Update SP on screen when editing is finished
			Intent returnIntent = new Intent();
			returnIntent.putExtra("result", String.valueOf(pageNumber));
			setResult(RESULT_OK, returnIntent);
			// Close Activity
			finish();
		}
	}
}
