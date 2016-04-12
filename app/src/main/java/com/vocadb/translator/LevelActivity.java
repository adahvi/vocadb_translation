package com.vocadb.translator;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vocadb.translator.base.BaseActivity;
import com.vocadb.translator.base.Constants;

/**
 * Copyright 2016 VocaDB Software Development
 * Created by Adah Valeña on 4/5/2016.
 *
 * Level Activity class
 */
public class LevelActivity extends ListActivity implements ActionBar.OnNavigationListener, View.OnClickListener {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    // UI Declarations
    private RadioGroup rgLevels;
    private RadioButton rbCollege;
    private RadioButton rbHigh3;
    private RadioButton rbHigh2;
    private RadioButton rbHigh1;
    private RadioButton rbMiddle3;
    private RadioButton rbMiddle2;
    private RadioButton rbMiddle1;
    private RadioButton rbElementary;
    private RadioButton rbNothing;
    private Button btnCancel;
    private Button btnOk;

    // Int
    private int color;
    private int level;
    protected int selectedPosition;
    private Integer[] values;

    // String
    private String[] levelValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        // Call init method
        init();

        // Initialize UI components
//        rgLevels = (RadioGroup) findViewById(R.id.levels_radio_group);
//        rbCollege = (RadioButton) findViewById(R.id.college_radio_button);
//        rbHigh3 = (RadioButton) findViewById(R.id.high3_radio_button);
//        rbHigh2 = (RadioButton) findViewById(R.id.high2_radio_button);
//        rbHigh1 = (RadioButton) findViewById(R.id.high1_radio_button);
//        rbMiddle3 = (RadioButton) findViewById(R.id.middle3_radio_button);
//        rbMiddle2 = (RadioButton) findViewById(R.id.middle2_radio_button);
//        rbMiddle1 = (RadioButton) findViewById(R.id.middle1_radio_button);
//        rbElementary = (RadioButton) findViewById(R.id.elementary_radio_button);
//        rbNothing = (RadioButton) findViewById(R.id.nothing_radio_button);

        btnCancel = (Button) findViewById(R.id.cancel_button);
        btnOk = (Button) findViewById(R.id.ok_button);

        // Set view theme
        setTheme();

        level = prefs.getInt("level",21);

        selectedPosition = prefs.getInt("selectedPosition", 6);

        // Set onclick listeners for button
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        // I do no use these values anywhere inside the ArrayAdapter. I could, but don't.
        values = new Integer[] {41,33,32,31,23,22,21,13,1};
        levelValues = new String[] {"College","High 3","High 2","High 1","Middle 3","Middle 2","Middle 1","Elementary","Nothing"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.level_row, R.id.leveltextView, levelValues) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.level_row, null);
                    v.findViewById(R.id.levelradioButton1);
                }
                TextView tv = (TextView)v.findViewById(R.id.leveltextView);
                String l_name = levelValues[position];
                tv.setText(l_name);
                RadioButton r = (RadioButton)v.findViewById(R.id.levelradioButton1);
                r.setChecked(position == selectedPosition);
                r.setTag(position);
                r.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        selectedPosition = (Integer)view.getTag();
                        notifyDataSetChanged();
                    }
                });
                return v;
            }

        };
        setListAdapter(adapter);
    }

    /**
     * Method to initialize classes/libraries
     */
    private void init() {
        // Preference Manager
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();
    }

    /**
     * Method to change screen theme based on user preferences
     */
    private void setTheme() {
        // Get color from shared preferences
        color = getSavedColor();
        // Change toolbar title color
        getToolbarTitle().setTextColor(color);
        // Change button colors
        btnCancel.setBackgroundColor(color);
        btnOk.setBackgroundColor(color);
    }

    /**
     * Method to retrieve user preferred color/theme
     */
    public int getSavedColor() {
        // Get color from shared preference
        return prefs.getInt("color", Constants.defaultColor);
    }

    /**
     * Method to get toolbar title
     */
    public TextView getToolbarTitle() {
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        return toolbarTitle;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.cancel_button:
                // Finish and go back to previous activity
                finish();
                break;

            case R.id.ok_button:
//                // Get selected value from levels radio group
//                int selectedId = rgLevels.getCheckedRadioButtonId();
//                // Find the radio button by the returned id
//                RadioButton rbSelected = (RadioButton) findViewById(selectedId);

                editor.putInt("level", values[selectedPosition]);
                editor.putInt("selectedPosition",selectedPosition);
                editor.commit();

                // TODO Save level value to Shared prefs
                // Finish and go back to previous activity
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }
}
