package com.vocadb.translator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vocadb.translator.base.BaseActivity;
import com.vocadb.translator.utilities.ColorPickerUtil;

import java.util.ArrayList;

/**
 * Copyright 2016 VocaDB Software Development
 * Created by Adah Valeña on 4/5/2016.
 *
 * Color Picker Activity class
 */
public class ColorPickerActivity extends BaseActivity implements ColorPickerUtil.OnColorSelectedListener,
        View.OnClickListener {

    // Color
    private int color;

    // UI References
    private ImageView redCircleImageView;
    private ImageView blueCircleImageView;
    private ImageView skyBlueCircleImageView;
    private ImageView darkBlueCircleImageView;

    private TextView redTextView;
    private TextView blueTextView;
    private TextView skyBlueTextView;
    private TextView darkBlueTextView;

    // Utility
    private ColorPickerUtil colorPickerUtil;

    // Resources
    private Resources res;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_color_picker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init resources
        res = this.getResources();

        // Set view theme
        setTheme();

        // Instantiate UI components
        // Image views
        redCircleImageView = (ImageView) findViewById(R.id.redCircleImageView);
        blueCircleImageView = (ImageView) findViewById(R.id.blueCircleImageView);
        skyBlueCircleImageView = (ImageView) findViewById(R.id.skyBlueCircleImageView);
        darkBlueCircleImageView = (ImageView) findViewById(R.id.darkBlueCircleImageView);
        // Set color image
        redCircleImageView.setImageDrawable(getCircleDrawable(R.color.Custom_RED));
        blueCircleImageView.setImageDrawable(getCircleDrawable(R.color.Custom_BLUE));
        skyBlueCircleImageView.setImageDrawable(getCircleDrawable(R.color.Custom_SK_BLUE));
        darkBlueCircleImageView.setImageDrawable(getCircleDrawable(R.color.Custom_DARK_BLUE));
        // Set onclick listeners
        redCircleImageView.setOnClickListener(this);
        blueCircleImageView.setOnClickListener(this);
        skyBlueCircleImageView.setOnClickListener(this);
        darkBlueCircleImageView.setOnClickListener(this);

        // Text views
        redTextView = (TextView) findViewById(R.id.redTextView);
        blueTextView = (TextView) findViewById(R.id.blueTextView);
        skyBlueTextView = (TextView) findViewById(R.id.skyBlueTextView);
        darkBlueTextView = (TextView) findViewById(R.id.darkBlueTextView);
        // Set onclick listeners
        redTextView.setOnClickListener(this);
        blueTextView.setOnClickListener(this);
        skyBlueTextView.setOnClickListener(this);
        darkBlueTextView.setOnClickListener(this);

        // Color picker
        colorPickerUtil = (ColorPickerUtil) findViewById(R.id.colorPicker);
        colorPickerUtil.setOnColorSelectedListener(this);
    }

    /**
     * Method to assign default colors
     *
     * @param customColor
     * @return
     */
    private Drawable getCircleDrawable(int customColor) {
        ShapeDrawable biggerCircle= new ShapeDrawable( new OvalShape());
        biggerCircle.setIntrinsicHeight( 60 );
        biggerCircle.setIntrinsicWidth( 60);
        biggerCircle.setBounds(new Rect(0, 0, 60, 60));

        // Check color
        if(customColor == R.color.Custom_RED) {
            biggerCircle.getPaint().setColor(Color.parseColor("#CC0000"));
        } else if(customColor == R.color.Custom_BLUE) {
            biggerCircle.getPaint().setColor(Color.parseColor("#0071C4"));
        } else if(customColor == R.color.Custom_DARK_BLUE) {
            biggerCircle.getPaint().setColor(Color.parseColor("#054688"));
        } else if(customColor == R.color.Custom_SK_BLUE) {
            biggerCircle.getPaint().setColor(Color.parseColor("#465DBB"));
        }
        return biggerCircle;
    }

    /**
     * Method to change screen theme based on user preferences
     */
    private void setTheme() {
        // Get color from shared preferences
        color = getSavedColor();
        // Change toolbar title color
        getToolbarTitle().setTextColor(color);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch(v.getId()) {
            case R.id.redTextView:
            case R.id.redCircleImageView:
                intent.putExtra("color", getResources().getColor(R.color.Custom_RED));
                setResult(3, intent);
                finish();
                break;

            case R.id.blueTextView:
            case R.id.blueCircleImageView:
                intent.putExtra("color", getResources().getColor(R.color.Custom_BLUE));
                setResult(3, intent);
                finish();
                break;

            case R.id.skyBlueTextView:
            case R.id.skyBlueCircleImageView:
                intent.putExtra("color", getResources().getColor(R.color.Custom_SK_BLUE));
                setResult(3, intent);
                finish();
                break;

            case R.id.darkBlueTextView:
            case R.id.darkBlueCircleImageView:
                intent.putExtra("color", getResources().getColor(R.color.Custom_DARK_BLUE));
                setResult(3, intent);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onColorChanged(int color) {
        Intent intent = new Intent();
        intent.putExtra("color", color);
        setResult(3,intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        // Get color from shared preferences
        color = getSavedColor();
        intent.putExtra("color", color);
        setResult(3,intent);
        finish();
    }

}
