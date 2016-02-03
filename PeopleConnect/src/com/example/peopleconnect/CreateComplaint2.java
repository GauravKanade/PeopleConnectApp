package com.example.peopleconnect;

import java.util.StringTokenizer;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateComplaint2 extends Activity implements OnClickListener {

	// private final String URL =
	// "http://10.0.2.2:8080/Project8/ReceiveComplaint";
	private String URL ;

	ProgressDialog progress;
	int user_id;
	TextView title, description, position, tvAddress;
	String image_name;
	Bitmap bmp;
	ImageButton buttons[];
	int chosenCategory = -1;
	Button confrim;
	String t, d;// title from the previous activity
	String encodedString;
	String address = "";
	String lat, lon, broadcast_locality, city, country;

	final Context contect = this;

	String categories[];
	int categoryIds[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create_complaint_page2);
		progress = ProgressDialog.show(CreateComplaint2.this,
				"Please Wait", "Getting the GPS Location");
		init();
		URL = Utility.getURL(this)+ "ReceiveComplaint";

	}

	private void putButtons() {
		// TODO Auto-generated method stub
		LinearLayout l = (LinearLayout) findViewById(R.id.llCategoryContainer);
		int n = categories.length;
		int numberOfLines = (int) Math.ceil(n / 3);

		l.setWeightSum(100);
		float weight = (float) (100.0 / numberOfLines);
		for (int i = 0; i < n; i += 3) {
			LinearLayout l1 = new LinearLayout(this);
			l1.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
					weight));

			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			param.weight = 0.33f;
			param.setMargins(2, 2, 2, 2);

			if (i < n) {
				Button b1 = new Button(this);
				b1.setLayoutParams(param);
				b1.setTextColor(Color.WHITE);
				b1.setText(categories[i]);
				// b1.setCompoundDrawablesWithIntrinsicBounds(0,
				// R.drawable.social_email, 0, 0);
				b1.setBackgroundColor(0xff227bad);
				b1.setTextSize(15);
				b1.setId(i);
				b1.setOnClickListener(this);
				l1.addView(b1);
			}
			if ((i + 1) < n) {
				Button b2 = new Button(this);
				b2.setLayoutParams(param);
				b2.setTextColor(Color.WHITE);
				b2.setText(categories[i + 1]);
				b2.setBackgroundColor(0xff227bad);
				// b2.setCompoundDrawablesWithIntrinsicBounds(0,
				// R.drawable.social_email, 0, 0);
				b2.setTextSize(15);
				b2.setId(i + 1);
				b2.setOnClickListener(this);
				l1.setOrientation(LinearLayout.HORIZONTAL);
				l1.addView(b2);
			} else {
				LinearLayout l2 = new LinearLayout(this);
				l2.setLayoutParams(param);
				l1.addView(l2);
			}
			if ((i + 2) < n) {
				Button b2 = new Button(this);
				b2.setLayoutParams(param);
				b2.setTextColor(Color.WHITE);
				b2.setText(categories[i + 2]);
				b2.setBackgroundColor(0xff227bad);
				// b2.setCompoundDrawablesWithIntrinsicBounds(0,
				// R.drawable.social_email, 0, 0);
				l1.setOrientation(LinearLayout.HORIZONTAL);
				b2.setTextSize(15);
				b2.setId(i + 2);
				b2.setOnClickListener(this);
				l1.addView(b2);
			} else {
				LinearLayout l2 = new LinearLayout(this);
				l2.setLayoutParams(param);
				l1.addView(l2);
			}
			l.addView(l1);
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		title = (TextView) findViewById(R.id.tvTitleComplaintCreate2);
		description = (TextView) findViewById(R.id.tvDescriptionComplaintCreate2);
		tvAddress = (TextView) findViewById(R.id.tvAddressComplaintCreate2);
		confrim = (Button) findViewById(R.id.bPostComplaint);
		confrim.setOnClickListener(this);

		Bundle data = getIntent().getExtras();
		t = data.getString("title");
		image_name = data.getString("photo");
		d = data.getString("description");
		encodedString = data.getString("encodedString");
		lat = data.getString("latitude");
		lon = data.getString("longitude");
		address = data.getString("address");
		broadcast_locality = data.getString("broadcast_locality");
		city = data.getString("city");
		country = data.getString("country");

		title.setText("Title:" + t);
		description.setText("Description: " + d);
		tvAddress.setText("Location:\n " + address);
		position.setText("Coordinates Lat:" + lat + ", Long: " + lon);

		getCategories();
		// categories = new String[1];
		// categories[0] = "Sample";
	}

	private void getCategories() {
		// TODO Auto-generated method stub
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams params = new RequestParams();
		params.put("user_id", Utility.user_id);

		try {
			client.post(Utility.getURL(this) + "GetCategories", params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							String response = new String(arg2);
							StringTokenizer st2 = new StringTokenizer(response,
									"$");
							int n = st2.countTokens();
							categories = new String[n];
							categoryIds = new int[n];
							for (int i = 0; i < n; i++) {
								String temp = st2.nextToken();
								StringTokenizer st3 = new StringTokenizer(temp,
										",");
								categoryIds[i] = Integer.parseInt(st3
										.nextToken());
								categories[i] = st3.nextToken();
							}
							putButtons();
							progress.dismiss();
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							Toast.makeText(contect, arg3.getMessage(),
									Toast.LENGTH_LONG).show();
							progress.dismiss();
						}
					});
		} catch (Exception e) {
			progress.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		LinearLayout ll = (LinearLayout) findViewById(R.id.llCategoryContainer);
		if (v.getId() >= 0 && v.getId() <= categories.length) {

			for (int i = 0; i < categories.length; i++) {
				Button b = (Button) ll.findViewById(i);
				if (i == v.getId()) {
					b.setBackgroundColor(0xff228fff);
					chosenCategory = i;
				} else
					b.setBackgroundColor(0xff227bad);
			}
		} else if (v.getId() == R.id.bPostComplaint) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// Add the buttons
			builder.setPositiveButton("Confirm",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked OK button
							progress = ProgressDialog.show(
									CreateComplaint2.this, "Wait",
									"Uploading...");
							sendComplaint();
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
						}
					});
			// Set other dialog properties
			builder.setTitle("Confirm??");
			builder.setMessage("The Complaint will be posted to the respective department. I here by agree that the complaint I am posting is valid and authentic");
			builder.setIcon(R.drawable.ic_action_cloud);
			builder.show();

		}
	}

	public void sendComplaint() {
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams params = new RequestParams();
		params.put("user_id", 1);
		params.put("title", t);
		params.put("description", d);
		params.put("category", categoryIds[chosenCategory]);
		params.put("image", encodedString);
		params.put("filename", image_name);
		params.put("latitude", lat);
		params.put("longitude", lon);
		params.put("address", address);
		params.put("broadcast_locality", broadcast_locality);
		params.put("city", city);
		params.put("country", country);

		try {
			client.post(URL, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					String response = new String(arg2);
					StringTokenizer st1 = new StringTokenizer(response, "$");
					String complaint_id = st1.nextToken();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							contect);
					// Add the buttons
					builder.setPositiveButton("Done",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User clicked OK button
									Intent i = new Intent(
											"com.example.project1.HomePage");
									startActivity(i);
									finish();
								}
							});

					// Set other dialog properties
					builder.setTitle("Success!! Complaint ID: " + complaint_id);
					builder.setMessage("The Complaint has been posted to the department that handles '"
							+ categories[chosenCategory]

							+ ".\nYou will be notified of its status when it has been changed by the respective authorities");
					builder.setIcon(R.drawable.ic_action_good);
					builder.show();

				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					progress.dismiss();
					Toast.makeText(contect, "Failure: " + arg3.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			});
		} catch (Exception e) {
			progress.dismiss();
			Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}
}
