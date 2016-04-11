package com.vocadb.translator.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Connection Detector class to check if there's an active network connection
 *
 * Created by Adah Valeña on 4/5/2016.
 */
public class ConnectionDetectorUtil {

   private Context _context;

   public ConnectionDetectorUtil(Context context){
       this._context = context;
   }

   public boolean isConnectingToInternet(){
       ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
         if (connectivity != null) 
         {
             NetworkInfo[] info = connectivity.getAllNetworkInfo();
             if (info != null) 
                 for (int i = 0; i < info.length; i++) 
                     if (info[i].getState() == NetworkInfo.State.CONNECTED)
                     {
                         return true;
                     }

         }
         return false;
   }
}