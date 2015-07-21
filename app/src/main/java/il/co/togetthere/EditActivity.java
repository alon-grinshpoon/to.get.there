package il.co.togetthere;

import il.co.togetthere.db.AmazonClientManager;
import il.co.togetthere.db.DynamoDBManagerTask;
import il.co.togetthere.db.DynamoDBManagerType;
import il.co.togetthere.db.ServiceProvider;
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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;

public class EditActivity extends Activity {
	private ServiceProvider mSP;
	public static AmazonClientManager clientManager = null;

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
		profilePictureView.setProfileId(LoginActivity.user.getID());
		profilePictureView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent infoIntent = new Intent(EditActivity.this,
						UserInfoActivity.class);
				EditActivity.this.startActivity(infoIntent);

			}
		});

		Intent inIntent = getIntent();
		int pageNumber = inIntent.getIntExtra("SP_NUMBER", 1);
		Log.i("Position", "Position is" + pageNumber);
		mSP = ScreenSlideActivity.getCurrSP(pageNumber);

		clientManager = new AmazonClientManager(this);

		// Define Font
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GOTHIC.TTF");

		/**
		 * Title
		 */
		TextView title = ((TextView) findViewById(R.id.title_activity_edit_sp));
		title.setTypeface(font);

		/**
		 * Submit Button- Validation
		 **/
		TextView titleName = ((TextView) findViewById(R.id.title_editSPName));
		titleName.setTypeface(font);
		EditText name = ((EditText) findViewById(R.id.editSPName));
		name.setText(mSP.getName());
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

		TextView titleDescription = ((TextView) findViewById(R.id.title_editSPDescription));
		titleDescription.setTypeface(font);
		EditText description = ((EditText) findViewById(R.id.editSPDescription));
		// TODO description.setText(mSP.getDescription());
		description.setTypeface(font);

		TextView titleDiscount = ((TextView) findViewById(R.id.title_editSPDiscount));
		titleDescription.setTypeface(font);
		EditText discount = ((EditText) findViewById(R.id.editSPDiscount));
		discount.setText("" + mSP.getDiscount());
		discount.setTypeface(font);

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
				String description = ((EditText) findViewById(R.id.editSPDescription))
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
					mSP.setName(name);
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

				// Description
				// if (!description.equals("")) {
				// mSP.setDescription(phone);
				// }

				// Discount
				if (discountNum != -1) {
					mSP.setDiscount(discountNum);
				} else {
					Toast.makeText(getApplicationContext(),
							"Discount amount is missing",
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

								// update mSP in DB and finish;
								DynamoDBManagerTask addProvider = new DynamoDBManagerTask();
								addProvider.mSerivceProvider = mSP;
								addProvider.execute(DynamoDBManagerType.INSERT_SERVICE_PROVIDER
										.toString());

								// Close Activity
								EditActivity.this.finish();
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(
						EditActivity.this);
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

}
