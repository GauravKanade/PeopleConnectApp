package com.example.peopleconnect;

import java.util.StringTokenizer;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Complains extends Activity {

	ProgressDialog progress;
	Context context = this;
	ListView lv;
	String ids[];
	String heading[];
	String sender[];
	String area[];
	String time[];
	Bitmap images[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getData();
		setContentView(R.layout.activity_complains);
		progress = ProgressDialog.show(Complains.this, "Please Wait",
				"Getting Recent Complaints");

	}

	private void getData() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("user_id", ""+Utility.user_id);
		try {
			client.get(Utility.getURL(context) + "GetComplains",params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							String response = new String(arg2);

							StringTokenizer st1 = new StringTokenizer(response,
									"$$$$");

							int n = st1.countTokens();
							n--;
							ids = new String[n];
							heading = new String[n];
							sender = new String[n];
							area = new String[n];
							time = new String[n];
							images = new Bitmap[n];
							for (int i = 0; i < n; i++) {
								String comp = st1.nextToken();
								StringTokenizer st2 = new StringTokenizer(comp,
										"####");
								// Toast.makeText(context,
								// "Complains: "+st2.countTokens(),
								// Toast.LENGTH_LONG).show();
								ids[i] = st2.nextToken();
								heading[i] = st2.nextToken();
								sender[i] = st2.nextToken();
								time[i] = getDateFormat(st2.nextToken());
								area[i] = st2.nextToken();
							}
							init();
							progress.dismiss();
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							progress.dismiss();
							Toast.makeText(context, arg3.getMessage(),
									Toast.LENGTH_LONG).show();
						}
					});

		} catch (Exception e) {

		}
	}

	protected void init() {
		CustomListComplain adapter = new CustomListComplain(Complains.this,
				heading, sender, area, time);
		lv = (ListView) findViewById(R.id.lvComplains);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(Complains.this,
				// "You Clicked at " + heading[+position],
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(
						"com.example.peopleconnect.ComplainSingle");
				intent.putExtra("complaint_id", ids[position]);
				intent.putExtra("title", heading[position]);
				intent.putExtra("sender", sender[position]);
				intent.putExtra("time", time[position]);
				intent.putExtra("area", area[position]);
				startActivity(intent);
			}
		});
	}

	String getDateFormat(String timeStamp) {
		String finaltime = "";
		String year = timeStamp.substring(0, 4);
		int m = Integer.parseInt(timeStamp.substring(5, 7));
		int d = Integer.parseInt(timeStamp.substring(8, 10));
		int hour = Integer.parseInt(timeStamp.substring(11, 13));
		String min = timeStamp.substring(14, 16);
		String time = "";
		if (hour == 12) {
			time = hour + ":" + min + "pm";
		} else if (hour > 12) {
			time = (hour - 12) + ":" + min + "pm";
		} else {
			time = hour + ":" + min + "am";
		}
		String month = "";
		switch (m) {
		case 1:
			month = "Jan";
			break;
		case 2:
			month = "Feb";
			break;
		case 3:
			month = "Mar";
			break;
		case 4:
			month = "Apr";
			break;
		case 5:
			month = "May";
			break;
		case 6:
			month = "Jun";
			break;
		case 7:
			month = "Jul";
			break;
		case 8:
			month = "Aug";
			break;
		case 9:
			month = "Sep";
			break;
		case 10:
			month = "Oct";
			break;
		case 11:
			month = "Nov";
			break;
		case 12:
			month = "Dec";
			break;
		}
		finaltime = d + " " + month + "," + year + " " + time;
		return finaltime;
	}
}
