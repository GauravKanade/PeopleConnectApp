package com.example.peopleconnect;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateUserPage2 extends Activity implements OnItemSelectedListener {

	EditText name, phone, email;
	Spinner spState, spCity, spArea;
	Context context = this;

	String states[] = { "Karnataka", "Delhi", "Maharashtra" };
	String cities[][] = { { "Bangalore", "Mangalore", "hubli" },
			{ "New Delhi", "Agra" }, { "Mumbai", "Nagpur", "Kolhapur" } };
	String areas[][] = { { "Katriguppe", "Hoskerehalli", "Banashankari" },
			{ "Beach", "Fish market" }, { "Shivnagar", "Ganesh Bhavan" },
			{ "Gandhiville", "India Gate" }, { "Taj Mahal" },
			{ "Dombiville", "AshaNagar", "Chatrapati Shijavi Terminus" },
			{ "Orange" }, { "Lakshmi Temple" } };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create_user_page2);
		spState = (Spinner) findViewById(R.id.spStates);
		spCity = (Spinner) findViewById(R.id.spCity);
		spArea = (Spinner) findViewById(R.id.spArea);
		spState.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String sp1 = String.valueOf(spState.getSelectedItem());
				int city=0;
				for (int i = 0; i < states.length; i++) {
					city++;
					if (sp1.contentEquals(states[i])) {

						ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
								context, android.R.layout.simple_spinner_item,
								cities[i]);
						dataAdapter
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						// dataAdapter.notifyDataSetChanged();
						spCity.setAdapter(dataAdapter);
						break;
					}
				}
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		spCity.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String sp1 = String.valueOf(spCity.getSelectedItem());
				int Sindex = spState.getSelectedItemPosition();
				int index = 0;
				for(int i=0;i<Sindex;i++)
					index+=cities[i].length;
				for (int i = 0; i < cities[Sindex].length; i++) {
					if (sp1.contentEquals(cities[Sindex][i])) {

						ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
								context, android.R.layout.simple_spinner_item,
								areas[index+i]);
						dataAdapter
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						// dataAdapter.notifyDataSetChanged();
						spArea.setAdapter(dataAdapter);
						break;
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		init();
	}

	public void init() {
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, states);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapter.notifyDataSetChanged();
		spState.setAdapter(dataAdapter);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// spCity.setAdapter(null);

	}

}
