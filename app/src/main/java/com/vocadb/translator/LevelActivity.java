package com.vocadb.translator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LevelActivity extends AppCompatActivity implements View.OnClickListener {

    // UI References
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize UI components
        rgLevels = (RadioGroup) findViewById(R.id.levels_radio_group);
        rbCollege = (RadioButton) findViewById(R.id.college_radio_button);
        rbHigh3 = (RadioButton) findViewById(R.id.high3_radio_button);
        rbHigh2 = (RadioButton) findViewById(R.id.high2_radio_button);
        rbHigh1 = (RadioButton) findViewById(R.id.high1_radio_button);
        rbMiddle3 = (RadioButton) findViewById(R.id.middle3_radio_button);
        rbMiddle2 = (RadioButton) findViewById(R.id.middle2_radio_button);
        rbMiddle1 = (RadioButton) findViewById(R.id.middle1_radio_button);
        rbElementary = (RadioButton) findViewById(R.id.elementary_radio_button);
        rbNothing = (RadioButton) findViewById(R.id.nothing_radio_button);
        btnCancel = (Button) findViewById(R.id.cancel_button);
        btnOk = (Button) findViewById(R.id.ok_button);

        // Set onclick listeners for button
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.cancel_button:
                // Finish and go back to previous activity
                finish();
                break;

            case R.id.ok_button:
                // Get selected value from levels radio group
                int selectedId = rgLevels.getCheckedRadioButtonId();
                // Find the radio button by the returned id
                RadioButton rbSelected = (RadioButton) findViewById(selectedId);

                // TODO Save level value to Shared prefs
                // Finish and go back to previous activity
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
