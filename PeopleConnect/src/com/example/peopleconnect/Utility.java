package com.example.peopleconnect;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Utility extends Activity {
	static String filename = "preferencesPeopleConnect";
	public static String URL = "http://192.168.1.35:8080/Project8/";
	public static String reg_id = "";
	private static GPSTracker tracker;
	public static int user_id = -1;
	public static SharedPreferences sp;
	public static int StatusNotificationId = 12789123;

	public static void PostLocation(Context con) {
		final Context context = con;
		try {
			tracker = new GPSTracker(context);
			double lat = 0.0, lon = 0;
			String add = "";

			if (tracker.getLocation() == null) {
				add = "GPS Not enabled";
			} else {
				if (tracker.canGetLocation) {
					Address address = tracker.getAddressObject(context);
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
				}
			}
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("lat", lat);
			params.put("long", lon);
			params.put("address", add);
			client.post(Utility.URL + "PrintLocation", params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated
							// method stub
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							Toast.makeText(context, "Failed URL Wrong",
									Toast.LENGTH_LONG).show();

						}
					});

		} catch (Exception e2) {
			Toast.makeText(context, "Exception: " + e2.getMessage(),
					Toast.LENGTH_LONG).show();
		} finally {
			tracker.stopUsingGPS();
		}

	}

	static String getURL(Context con) {
		SharedPreferences sp = con.getSharedPreferences(filename, 0);
		String url = sp.getString("url", "0000");
		URL = url;
		return url;
	}

	static boolean setUp(Context con) {
		sp = con.getSharedPreferences(filename, 0);
		String uid = sp.getString("user_id", "-1");
		String rid = sp.getString("reg_id", "0000");
		String url = sp.getString("url", "0000");

		if (uid.equals("-1")) {
			Editor edit =  sp.edit();
			edit.putString("url", URL);
			edit.commit();
			return false;
		}
		else {
			user_id = Integer.parseInt(uid);
		}
		if (url.equals("0000")) {
		} else {

			URL = url;
			reg_id = rid;
		}
		return true;

	}

}
