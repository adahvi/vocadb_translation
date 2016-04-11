package com.vocadb.translator.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Copyright 2016 VocaDB Software Development
 * Created by Adah Valeña on 4/5/2016.
 *
 * Base application class
 */
public class VocaDB extends Application {

    public VocaDB() {
        super();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
