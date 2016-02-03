package com.example.peopleconnect;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class ErrorInConnection extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_error_in_connection);
		
		Bundle basket = getIntent().getExtras();
		String error = basket.getString("error");
		Toast.makeText(this, error, Toast.LENGTH_LONG).show();
	}

}
