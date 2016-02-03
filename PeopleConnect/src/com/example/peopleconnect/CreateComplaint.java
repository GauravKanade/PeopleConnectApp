package com.example.peopleconnect;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.BreakIterator;
import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateComplaint extends Activity implements OnClickListener {
	Button bChooseCategory;
	ImageView image;
	Bitmap bmp;
	String encodedString;
	Context context = this;
	EditText title, description;
	int check;
	List<String> list;
	boolean imageclicked = false;
	GPSTracker tracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_create_complaint);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		check = 0;
		imageclicked = false;
		bChooseCategory = (Button) findViewById(R.id.bChooseCategoryComplaint);
		bChooseCategory.setOnClickListener(this);
		image = (ImageView) findViewById(R.id.ivCreateComplaint_Image);
		title = (EditText) findViewById(R.id.etComplaintName);
		description = (EditText) findViewById(R.id.etComplaintDescription);
		tracker = new GPSTracker(this);
		image.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.bChooseCategoryComplaint) {
			if (imageclicked == false)
				Toast.makeText(this, "Please click an image!!",
						Toast.LENGTH_LONG).show();
			else if (title.getText().toString() == "")
				Toast.makeText(this, "Please enter a title!!",
						Toast.LENGTH_LONG).show();
			else if (description.getText().toString() == "")
				Toast.makeText(this, "Please provide a description!!",
						Toast.LENGTH_LONG).show();
			else {
				double lat = 0, lon = 0;
				
				if (tracker.getLocation() == null)
					Toast.makeText(this, "GPS is not enabled",
							Toast.LENGTH_LONG).show();
				else {
					String add = "";
					if (tracker.canGetLocation) {
						Address address = tracker.getAddressObject(this);
						lat = tracker.latitude;
						lon = tracker.longitude;
						if (address != null)
							for (int i = 0; i < 10; i++) {
								String temp = address.getAddressLine(i);
								if (temp != null) {
									if (i != 0)
										add += ", " + temp;
									else
										add = temp;
								} else
									break;
							}
						String broadcast_locality = "";
						try {
							String admin = address.getAdminArea();
							String subAdmin = address.getSubAdminArea();
							String local = address.getLocality();
							String subLocal = address.getSubLocality();
							if (subLocal != null)
								broadcast_locality = subLocal;
							else if (local != null)
								broadcast_locality = local;
							else if (subAdmin != null)
								broadcast_locality = subAdmin;
							else if (admin != null)
								broadcast_locality = admin;
						} catch (Exception e) {
							Toast.makeText(this, e.getMessage(),
									Toast.LENGTH_LONG).show();
						}
						String city = address.getLocality();
						
						String imageName = createImageFromBitmap(bmp);

						Intent i = new Intent(
								"com.example.peopleconnect.CreateComplaintPage2");
						i.putExtra("city", city);
						i.putExtra("title", title.getText().toString());
						i.putExtra("description", description.getText()
								.toString());
						i.putExtra("photo", imageName);
						i.putExtra("encodedString", encodedString);
						i.putExtra("latitude", "" + lat);
						i.putExtra("longitude", "" + lon);
						i.putExtra("address", add);
						i.putExtra("broadcast_locality", broadcast_locality);
						i.putExtra("country", tracker.getCountryName(context));
						tracker.locationManager = null;
						tracker.stopUsingGPS();
						startActivity(i);
						finish();
					}
				}
			}
		} else if (v.getId() == R.id.ivCreateComplaint_Image) {
			Intent i2 = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(i2, 12345);
		}
	}

	public String createImageFromBitmap(Bitmap bitmap) {

		String fileName = "myImage" + (int) (Math.random() * 5966);// no .png or
																	// .jpg
																	// needed
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
			fo.write(bytes.toByteArray());
			byte[] byte_arr = bytes.toByteArray();
			encodedString = Base64.encodeToString(byte_arr, 0);
			// remember close file output
			fo.close();
		} catch (Exception e) {
			e.printStackTrace();
			fileName = null;
			Toast.makeText(this, "image not stored", Toast.LENGTH_LONG).show();
		}
		return fileName;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 12345) {
			imageclicked = true;
			Bundle extra = data.getExtras();
			bmp = (Bitmap) extra.get("data");
			image.setImageBitmap(bmp);
			tracker = new GPSTracker(this);

		}
	}

}
