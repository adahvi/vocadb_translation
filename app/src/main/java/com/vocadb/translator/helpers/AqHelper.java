package com.vocadb.translator.helpers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;

import com.vocadb.translator.base.Constants;
/**
 * Helper for AQuery library
 *
 * Created by Adah Valeña on 4/5/2016.
 */

public class AqHelper {

    Activity activity;
    private Map<String, String> headers;

    public AqHelper(Activity _activity) {
        this.activity = _activity;
        headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-type", "application/json");
        headers.put("User-Agent", Constants.MOBILE_AGENT);
    }


    public AjaxCallback<JSONObject> ajaxCallback(String _url, String _callback,
                                                 long expire) {
        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
        cb.url(_url).type(JSONObject.class).fileCache(true).expire(expire);
        if (_callback != null) {
            cb.weakHandler(activity, _callback);
        }

        for (String name : headers.keySet()) {
            cb.header(name, headers.get(name));
        }
        return cb;
    }

    public AjaxCallback<Object> ajax(String _url, Class<?> type, long expire,
                                     AjaxCallback<Object> cb) {
        for (String name : headers.keySet()) {
            cb.header(name, headers.get(name));
        }
        return cb.type((Class<Object>) type).url(_url).expire(expire);
    }

    public AjaxCallback<Object> ajaxObjectCallback(String _url,
                                                   String _callback, long expire, Class<?> _class) {
        AjaxCallback<Object> cb = new AjaxCallback<Object>();

        cb.url(_url).type((Class<Object>) _class).fileCache(true)
                .expire(expire);

        if (_callback != null) {
            cb.weakHandler(activity, _callback);
        }

        for (String name : headers.keySet()) {
            cb.header(name, headers.get(name));
        }
        return cb;
    }

    public AjaxCallback<String> ajaxStringCallback(String _url,
                                                   String _callback, long expire) {
        AjaxCallback<String> cb = new AjaxCallback<String>();
        cb.url(_url).type(String.class).fileCache(true).expire(expire);
        if (_callback != null) {
            cb.weakHandler(activity, _callback);
        }
        for (String name : headers.keySet()) {
            cb.header(name, headers.get(name));
        }
        return cb;
    }

    public AjaxCallback<String> ajaxStringCallback(String _url, String _callback) {
        return ajaxStringCallback(_url, _callback, Constants.CACHE_RESET);
    }

    public AjaxCallback<JSONObject> ajaxCallback(String _url, String _callback) {
        return ajaxCallback(_url, _callback, Constants.CACHE_RESET);
    }

    public AjaxCallback<JSONObject> ajaxCallback(String _url) {
        return ajaxCallback(_url, null, Constants.CACHE_RESET);
    }

    public BitmapAjaxCallback bitmapCallback(String _imgUrl,
                                             Bitmap _placeHolder, AQuery _aq, int _fallback, float _ratio,
                                             int _targetWidth, boolean _fileCache, boolean _memCache) {
        BitmapAjaxCallback cb = new BitmapAjaxCallback();
        cb.url(_imgUrl).animation(AQuery.FADE_IN_NETWORK).fallback(_fallback)
                .preset(_placeHolder).fileCache(_fileCache).memCache(_memCache)
                .targetWidth(_targetWidth);
        if (_ratio != 0) {
            cb.ratio(_ratio).anchor(1);
        }
        for (String name : headers.keySet()) {
            cb.header(name, headers.get(name));
        }
        return cb;
    }

}
