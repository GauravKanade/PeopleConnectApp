package com.example.peopleconnect;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class HomePage extends Activity implements OnClickListener {
	Button bMyAccount, bComplain, bPublicOpinion, bSettings, bCreateComplaint;
	ImageView bSearch;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	CustomDrawerAdapter adapter;

	List<DrawerItem> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home_page);
		init();
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.gravatar, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		// mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// SelectItem(0);
		}

	}

	protected void init() {
		bMyAccount = (Button) findViewById(R.id.BMyAccount);
		bComplain = (Button) findViewById(R.id.BComplains);
		bPublicOpinion = (Button) findViewById(R.id.BPublicOpinions);
		bSettings = (Button) findViewById(R.id.BSettings);
		bSearch = (ImageView) findViewById(R.id.ivUser);
		bCreateComplaint = (Button) findViewById(R.id.BCreateComplaintMainPage);

		dataList = new ArrayList<DrawerItem>();
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		bMyAccount.setOnClickListener(this);
		bComplain.setOnClickListener(this);
		bPublicOpinion.setOnClickListener(this);
		bSettings.setOnClickListener(this);
		bSearch.setOnClickListener(this);
		bCreateComplaint.setOnClickListener(this);
		dataList.add(new DrawerItem("My Complaints", R.drawable.ic_action_email));
		dataList.add(new DrawerItem("My Profile", R.drawable.ic_action_good));
		dataList.add(new DrawerItem("Public Questions",
				R.drawable.ic_action_labels));
		dataList.add(new DrawerItem("Settings", R.drawable.ic_action_settings));
		dataList.add(new DrawerItem("Help", R.drawable.ic_action_help));

		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
				dataList);

		mDrawerList.setAdapter(adapter);

	}

	public void SelectItem(int possition) {

		switch (possition) {
		case 0:
			Intent i = new Intent("com.example.peopleconnect.Complains");
			startActivity(i);
			break;
		case 1:

			break;
		case 2:
			Intent i3 = new Intent("com.example.peopleconnect.PublicOpinionsTabs");
			startActivity(i3);
			break;
		case 3:
			Intent i2 = new Intent("com.example.peopleconnect.PublicOpinions");
			startActivity(i2);

			break;
		default:
			break;
		}

		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i;
		switch (v.getId()) {
		case R.id.BComplains:
			startComplaints();

			break;
		case R.id.BCreateComplaintMainPage:
			i = new Intent("com.example.peopleconnect.CreateComplaint");
			startActivity(i);
			break;
		case R.id.BPublicOpinions:
			i = new Intent("com.example.peopleconnect.PublicOpinionsTabs");
			startActivity(i);
			break;
		case R.id.BMyAccount:
			i = new Intent("com.example.peopleconnect.CreateUserPage2");
			startActivity(i);
			
			break;
		case R.id.BSettings:
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case R.id.ivUser:
			mDrawerLayout.openDrawer(mDrawerList);
			break;
		}
	}

	private void startComplaints() {
		// TODO Auto-generated method stub
		// Get Data
		Intent i = new Intent("com.example.peopleconnect.Complains");
		startActivity(i);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		// mDrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.

		return false;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		// mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SelectItem(position);

		}
	}
}