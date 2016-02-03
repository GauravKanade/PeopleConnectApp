package com.example.peopleconnect;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListComplain extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] Heading;
	private final String[] Area;
	private final String[] Sender;
	private final String[] Time;
	
	public CustomListComplain(Activity context, String[] heading, String[] sender, String[] area, String[] time) {
		super(context, R.layout.list_complain, heading);
		this.context = context;
		this.Heading = heading;
		this.Area = area;
		this.Sender = sender;
		this.Time = time;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_complain, null, true);
		
		TextView heading = (TextView) rowView.findViewById(R.id.tvComplainHeadingList);
		TextView sender = (TextView) rowView.findViewById(R.id.tvSenderComplainList);
		TextView area = (TextView) rowView.findViewById(R.id.tvAreaComplainList);
		TextView time = (TextView) rowView.findViewById(R.id.tvTimeComplainList);
		
		heading.setText(Heading[position]);
		sender.setText(Sender[position]);
		area.setText(Area[position]);
		time.setText(Time[position]);		
		return rowView;
	}
}