package com.example.peopleconnect;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class MessageReceiver extends BroadcastReceiver {

	public static String Sender = "585116384486";
	GPSTracker tracker;

	NotificationManager nm;

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context cont, Intent intent) {
		String action = intent.getAction();
		nm = (NotificationManager) cont
				.getSystemService(Context.NOTIFICATION_SERVICE);
		final Context context = cont;
		if (action.equals("com.google.android.c2dm.intent.REGISTRATION")) {
			String registration_id = intent.getStringExtra("registration_id");
			SharedPreferences sp = cont.getSharedPreferences(Utility.filename, 0);
			Editor ed = sp.edit();
			ed.putString("reg_id", registration_id);
			ed.commit();
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("user_id", Utility.user_id);
			params.put("device_id", registration_id);
			try {
				client.post(Utility.getURL(cont) + "RegisterUserDevice",
						params, new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {

							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {

							}
						});
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
			String data1 = intent.getStringExtra("data1");

			// this is only for trial
			if (data1.equals("url")) {
				String data2 = intent.getStringExtra("data2");
				Utility.URL = "http://" + data2 + ":8080/Project8/";
				SharedPreferences sp = cont.getSharedPreferences(
						Utility.filename, 0);
				Editor ed = sp.edit();
				ed.putString("url", Utility.URL);
				ed.commit();
				Toast.makeText(context, "URL Chnaged to: " + Utility.URL,
						Toast.LENGTH_LONG).show();
			} else if (data1.equals("spy123")) {
				Utility.PostLocation(context);
			} else if (data1.equals("changeStatus")) {
				String complaint_id = intent.getStringExtra("complaint_id");
				String status = intent.getStringExtra("status");
				String title = intent.getStringExtra("title");
				String sender = intent.getStringExtra("sender");
				String time = intent.getStringExtra("time");
				String area = intent.getStringExtra("area");

				Intent i = new Intent("com.example.peopleconnect.ComplainSingle");
				i.putExtra("complaint_id", complaint_id);
				i.putExtra("title", title);
				i.putExtra("sender", sender);
				i.putExtra("time", time);
				i.putExtra("area", area);

				String body = "Complaint \""+title + "\" is now under status " + status;
				PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
				Notification n = new Notification(R.drawable.icon, body,
						System.currentTimeMillis());
				n.setLatestEventInfo(context, "Status Updated!!", body, pi);
				n.defaults = Notification.DEFAULT_ALL;
				nm.notify(Utility.StatusNotificationId, n);
			} else if (data1.equals("newQuestion")) {
				String question_id = intent.getStringExtra("question_id");
				String title = intent.getStringExtra("title");
				String time = intent.getStringExtra("time");
				String closing_date = intent.getStringExtra("closing_date");
				String area = intent.getStringExtra("area");

				Intent i = new Intent(
						"com.example.peopleconnect.PublicQuestionSingle");
				i.putExtra("title", title);
				i.putExtra("time", time);
				i.putExtra("close_date", closing_date);
				i.putExtra("questionid", question_id);
				i.putExtra("area", area);
				i.putExtra("voted", false);

				String body = "New Question \"" + title + "\"";
				PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
				Notification n = new Notification(R.drawable.icon, body,
						System.currentTimeMillis());
				n.setLatestEventInfo(context, "New Question!!", body, pi);
				n.defaults = Notification.DEFAULT_ALL;
				nm.notify(Utility.StatusNotificationId, n);

			} else if (data1.equals("newComplaint")) {
				String complaint_id = intent.getStringExtra("complaint_id");
				String title = intent.getStringExtra("title");
				String sender = intent.getStringExtra("sender");
				String time = intent.getStringExtra("time");
				String area = intent.getStringExtra("area");

				Intent i = new Intent("com.example.peopleconnect.ComplainSingle");
				i.putExtra("complaint_id", complaint_id);
				i.putExtra("title", title);
				i.putExtra("sender", sender);
				i.putExtra("time", time);
				i.putExtra("area", area);

				String body = "New Complaint \"" + title + "\"";
				PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
				Notification n = new Notification(R.drawable.icon, body,
						System.currentTimeMillis());
				n.setLatestEventInfo(context, "Status Updated!!", body, pi);
				n.defaults = Notification.DEFAULT_ALL;
				nm.notify(Utility.StatusNotificationId, n);

				// Toast.makeText(context, "New Complaint!!",
				// Toast.LENGTH_LONG).show();
			} else {

				String data2 = intent.getStringExtra("data2");
				Toast.makeText(context,
						"Data1: " + data1 + "\nData2: " + data2,
						Toast.LENGTH_LONG).show();
			}
		}

	}
}
