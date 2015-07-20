package il.co.togetthere;



import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;

public class AddNewTaskActivity extends Activity {

	private Task mTask;

	Calendar cal;
	int day;
	int month;
	int year;
	EditText dateEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_task);

		mTask = new Task();

		/**
		 * Full Screen Set UP
		 **/
		View decorView = getWindow().getDecorView();
		// Hide the status bar.
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
		// Remember that you should never show the action bar if the
		// status bar is hidden, so hide that too if necessary.
		android.app.ActionBar actionBar = getActionBar();
		actionBar.hide();

		/**
		 * Lower Bar Set UP
		 **/
		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.button_show_user_details_addNewTask);
		profilePictureView.setProfileId(LoginActivity.user.getID());

		profilePictureView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent infoIntent = new Intent(AddNewTaskActivity.this,
						UserInfoActivity.class);
				AddNewTaskActivity.this.startActivity(infoIntent);

			}
		});

		/**
		 * Date Chooser
		 **/
		ImageButton calanderButton;

		calanderButton = (ImageButton) findViewById(R.id.ButtonaddNewTaskDate);
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		dateEditText = (EditText) findViewById(R.id.addNewTaskDate);
		dateEditText.setKeyListener(null);
		dateEditText.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(0);

			}
		});

		calanderButton.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(0);

			}
		});

		/**
		 * Submit Button (Validation)
		 **/
		((Button) findViewById(R.id.addNewTaskSubmit))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						// Get Fields Content
						String date = ((EditText) findViewById(R.id.addNewTaskDate))
								.getText().toString();
						String address = ((EditText) findViewById(R.id.addNewTaskAddress))
								.getText().toString();
						String phone = ((EditText) findViewById(R.id.addNewTaskPhone))
								.getText().toString();
						String body = ((EditText) findViewById(R.id.addNewTaskBody))
								.getText().toString();
						String title = ((EditText) findViewById(R.id.addNewTaskTitle))
								.getText().toString();

						/**
						 * Set Task
						 **/
						// Title
						if (!title.equals("")) {
							mTask.setTitle(title);
						} else {
							Toast.makeText(getApplicationContext(),
									"Title is missing", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						// Body
						if (!body.equals("")) {
							mTask.setBody(body);
						} else {
							Toast.makeText(getApplicationContext(),
									"Description is missing",
									Toast.LENGTH_SHORT).show();
							return;
						}

						// Location
						if (!address.equals("")) {
							// mTask.setLocation(address); TODO- take from user
						} else {
							Toast.makeText(getApplicationContext(),
									"Location is missing", Toast.LENGTH_SHORT)
									.show();
							return;
						}

						// Date
						if (!date.equals("")) {
							// mTask.setDate(date); TODO- add to DB
						} else {
							Toast.makeText(getApplicationContext(),
									"Date is missing", Toast.LENGTH_SHORT)
									.show();
							return;
						}

						// Phone
						if (!phone.equals("")) {
							// mTask.setPhone(phone); TODO- take from user or delete
						} else {
							Toast.makeText(getApplicationContext(),
									"Your phone is missing", Toast.LENGTH_SHORT)
									.show();
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
									Toast.makeText(
											getApplicationContext(),
											"All set,Someone Will be In tuch shortly",
											Toast.LENGTH_LONG).show();
											mTask.setTaskOpener(LoginActivity.user);
											mTask.addTaskToDB();
									// Close Activity
									AddNewTaskActivity.this.finish();
									break;

								case DialogInterface.BUTTON_NEGATIVE:
									break;
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(
								AddNewTaskActivity.this);
						builder.setMessage("Are you sure of all the details?")
								.setPositiveButton("Yes", dialogClickListener)
								.setNegativeButton("No", dialogClickListener)
								.show();

					}
				});

	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			dateEditText.setText(selectedDay + " / " + (selectedMonth + 1)
					+ " / " + selectedYear);
		}
	};

}