package com.example.peopleconnect;
import java.util.Calendar;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CreatePublicQuestion extends Activity implements OnClickListener {

	private static final int DATE_PICKER_ID = 1111;
	Context context = this;
	int user_id = Utility.user_id;
	EditText etDescription, etOption1, etOption2, etOption3,
			etOption4, etOption5;
	RadioGroup rgRecipients;
	Button send, changeDate;
	boolean datePicked = false;
	ProgressDialog progress;

	int year, month, day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create_public_question);
		init();
	}

	private void init() {
		etDescription = (EditText) findViewById(R.id.etPublicQuestion);
		etOption1 = (EditText) findViewById(R.id.etOption1);
		etOption2 = (EditText) findViewById(R.id.etOption2);
		etOption3 = (EditText) findViewById(R.id.etOption3);
		etOption4 = (EditText) findViewById(R.id.etOption4);
		etOption5 = (EditText) findViewById(R.id.etOption5);
		send = (Button) findViewById(R.id.bCreatePublicQuestion);
		rgRecipients = (RadioGroup) findViewById(R.id.rgRecipientsPublicQuestion);
		changeDate = (Button) findViewById(R.id.bClosingDateCreateQuestion);
		send.setOnClickListener(this);
		changeDate.setOnClickListener(this);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bCreatePublicQuestion) {
			if (etDescription.getText().toString().length() == 0) {
				Toast.makeText(this, "Please enter the question",
						Toast.LENGTH_LONG).show();
			} else {
				int options = 0;
				if (etOption1.getText().toString().length() != 0)
					options++;
				if (etOption2.getText().toString().length() != 0)
					options++;
				if (etOption3.getText().toString().length() != 0)
					options++;
				if (etOption4.getText().toString().length() != 0)
					options++;
				if (etOption5.getText().toString().length() != 0)
					options++;

				if (options < 2) {
					Toast.makeText(this, "Please enter atleast two options!!",
							Toast.LENGTH_LONG).show();
				} else {
					final int recipient = rgRecipients
							.getCheckedRadioButtonId();
					if (recipient == -1)
						Toast.makeText(context,
								"Please select target recipient",
								Toast.LENGTH_LONG).show();
					else {
						if (datePicked == false) {
							Toast.makeText(context,
									"Please select closing date for voting!!",
									Toast.LENGTH_LONG).show();
						} else {
							final int optionsF = options;
							AlertDialog.Builder builder = new AlertDialog.Builder(
									this);
							// Add the buttons
							builder.setPositiveButton("Confirm",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// User clicked OK button
											progress = ProgressDialog.show(
													CreatePublicQuestion.this,
													"Wait", "Uploading...");
											post_question(recipient, optionsF);
										}
									});
							builder.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// User cancelled the dialog
										}
									});
							// Set other dialog properties
							builder.setTitle("Confirm??");
							builder.setMessage("This question will be posted under your name. I hereby confirm that this question is being posted by me with complete awareness of its implications and my intentions are clearly for the wellbeing of the society");
							builder.setIcon(R.drawable.ic_action_cloud);
							builder.show();

						}
					}

				}
			}
		} else if (v.getId() == R.id.bClosingDateCreateQuestion) {
			showDialog(DATE_PICKER_ID);
		}
	}

	private String getClosingDate() {
		String date = "";
		date = "" + year + "-";
		month++;
		if (month < 10)
			date += "0" + month;
		else
			date += "" + month;
		date += "-";
		if (day < 10)
			date += "0" + day;
		else
			date += "" + day;

		date += " 00:00:00";
		return date;
	}

	public void post_question(int recipient, int options) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		params.put("question", etDescription.getText().toString());
		params.put("option1", etOption1.getText().toString());
		params.put("option2", etOption2.getText().toString());
		params.put("option3", etOption3.getText().toString());
		params.put("option4", etOption4.getText().toString());
		params.put("option5", etOption5.getText().toString());
		params.put("no_of_options", options);
		int recipient_type = 0;
		if (recipient == R.id.rbAreaPublicQuestions)
			recipient_type = 1;
		else if (recipient == R.id.rbCityPublicQuestions)
			recipient_type = 2;
		else if (recipient == R.id.rbStatePublicQuestions)
			recipient_type = 3;
		else
			recipient_type = 4;
		params.put("recipient_type", recipient_type);

		String closingDate = getClosingDate();
		params.put("closing_date", closingDate);
		progress = ProgressDialog.show(this, "Please Wait",
				"Broadcasting your question");
		try {
			client.post(Utility.getURL(context)+ "PostPublicQuestion", params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							Toast.makeText(
									context,
									"Your Public is broadcasted to: "
											+ new String(arg2),
									Toast.LENGTH_LONG).show();
							Intent intent = new Intent(
									"com.example.peopleconnect.PublicOpinionsTabs");
							progress.dismiss();
							startActivity(intent);
							finish();
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							Toast.makeText(context,
									"Failure: " + arg3.getMessage(),
									Toast.LENGTH_LONG).show();
						}
					});
		} catch (Exception e) {
			Toast.makeText(context, "Exception: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:
			return new DatePickerDialog(this, pickerListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			datePicked = true;
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// Show selected date
			changeDate.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));

		}
	};

}
