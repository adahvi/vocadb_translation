package com.vocadb.translator.base;

import java.util.ArrayList;

/**
 * Created by User on 4/5/2016.
 */
public interface Constants {

    public static final long CACHE_DEFAULT_EXPIRE = 15 * 60 * 1000;
    public static final long CACHE_RESET = -1;
    public static final long NO_EXPIRATION = 0;

    public static final String MOBILE_AGENT = "Mozilla/5.0 (Linux; U; Android 2.2) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533 gzip";

    // API Keys
    public static final String TRANSLATOR_API_KEY = "3065";
    public static final String DICTIONARY_API_KEY = "8888";

    // API URLs
    public static final String TRANSLATOR_API_URL = "http://www.appsmithing.com/v2_voca/api_vocadb_trans.php";
    public static final String DICTIONARY_API_URL = "http://www.appsmithing.com/v2_voca/api_vocadb_dic.php";

    // URLs
    public static final String MEDIA_URL = "http://www.vocadb.co.kr/dic_media/audio/usw/";

    public static int defaultColor = 0xFF0071c4;
    public static int level = 9;
    public static int sound = 0; //0:tts, 1:voca
    public static int basicLang = 0;
    public static ArrayList<Integer> mainLang = new ArrayList<Integer>();

}
