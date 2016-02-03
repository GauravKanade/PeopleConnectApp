package com.example.peopleconnect;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {
	Context context = this;
	boolean firstUse=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		if (!Utility.setUp(context))
			firstUse=true;
		register();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("login_id", Utility.user_id);

		try {
			client.post(Utility.getURL(this) + "TestConnection",
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							Thread thread1 = new Thread() {
								public void run() {
									try {
										sleep(1500);
									} catch (Exception e2) {
										Intent intent1 = new Intent(
												"com.example.peopleconnect.ErrorInConnection");
										intent1.putExtra("error",
												e2.getMessage());
										startActivity(intent1);
										finish();
									} finally {
										 if(firstUse){
											
											Intent i = new Intent(
													"com.example.peopleconnect.CreateUserPage1");
											startActivity(i);
											finish();
										} else {
											Intent intent1 = new Intent(
													"com.example.peopleconnect.HomePage");
											startActivity(intent1);
											finish();
										}
									}
								}
							};
							thread1.start();
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							Toast.makeText(context, arg3.getMessage(),
									Toast.LENGTH_LONG).show();
							Intent intent1 = new Intent(
									"com.example.peopleconnect.ErrorInConnection");
							intent1.putExtra("error", arg3.getMessage());
							startActivity(intent1);
							finish();
						}
					});
		} catch (Exception e) {
			Intent intent1 = new Intent(
					"com.example.peopleconnect.ErrorInConnection");
			startActivity(intent1);
			finish();
		}
	}

	public void register() {
		Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
		intent.putExtra("app",
				PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		intent.putExtra("sender", MessageReceiver.Sender);
		startService(intent);
	}
}
