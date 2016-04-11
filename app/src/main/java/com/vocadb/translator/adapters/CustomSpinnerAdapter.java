package com.vocadb.translator.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vocadb.translator.R;
import com.vocadb.translator.base.Constants;

public class CustomSpinnerAdapter extends ArrayAdapter<String>
{
	private Activity context;
	ArrayList<String> data = null;
	private SharedPreferences prefs;
	private int color;
	private boolean isdark;

	public CustomSpinnerAdapter(Activity context, int resource, ArrayList<String> data) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		color = prefs.getInt("color", Constants.defaultColor);
		isdark = isColorDark(color);
	}



	private boolean isColorDark(int color) {
		double a = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
		if(a<0.5){
			return false; // It's a light color
		}else{
			return true; // It's a dark color

		}
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
		return getCustomView(position, convertView, parent);
	}

	private View getCustomView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if(row == null)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.simple_spinner_item_one, parent, false);
		}

		String item = data.get(position);

		if(item != null)
		{   // Parse the data from each object and set it.
			TextView myCountry = (TextView) row.findViewById(R.id.text_lang);

			if(myCountry != null)
				if(isdark)
				{
					myCountry.setTextColor(Color.WHITE);
					//    	 row.setBackgroundColor(Color.BLACK);
				}
				else
				{
					myCountry.setTextColor(Color.BLACK);
					//	 row.setBackgroundColor(Color.WHITE);


				}
			myCountry.setText(item);

		}

		return row;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{   // This view starts when we click the spinner.
		return getDropDownCustomView(position, convertView, parent);
	}

	private View getDropDownCustomView(int position, View convertView,
									   ViewGroup parent) {
		View row = convertView;
		if(row == null)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.simple_spinner_item_two, parent, false);
		}

		String item = data.get(position);

		if(item != null)
		{   // Parse the data from each object and set it.
			TextView myCountry = (TextView) row.findViewById(R.id.text_lang);

			if(myCountry != null)
				if(isdark)
				{
					myCountry.setTextColor(Color.WHITE);
					row.setBackgroundColor(Color.BLACK);
				}
				else
				{
					myCountry.setTextColor(Color.BLACK);
					row.setBackgroundColor(Color.WHITE);


				}
			myCountry.setText(item);

		}

		return row;
	}
}

