package com.example.peopleconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class CreateUserPage1 extends Activity implements OnClickListener{
	
	Button b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create_user_page1);
		
		b = (Button) findViewById(R.id.bCreateUserToPage2);
		b.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.bAlreadyHaveAccount){
			Intent intent1 = new Intent(
					"com.example.peopleconnect.LoginWithPassword");
			startActivity(intent1);
			finish();
		} else if(v.getId()==R.id.bCreateUserToPage2) {
			Intent intent1 = new Intent(
					"com.example.peopleconnect.HomePage");
			startActivity(intent1);
			finish();
		}
		
	}

}
