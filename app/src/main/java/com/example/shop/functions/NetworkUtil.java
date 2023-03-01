package com.example.shop.functions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


// תודה לעופרי
@SuppressWarnings("ALL")
public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        //בודק מה החיבורים של המכשיר
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //מביא את החיבור הקיים
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {
            //מעביר אם יש חיבור wifi
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            //מעביר אם יש חיבור mobile
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        //מעביר אם אין חיבור
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        //getConnectivityStatus המצב שהועבר מהפעולה
        int conn = NetworkUtil.getConnectivityStatus(context);
        //מאתחל הודעה של מצב החיבור
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            //status = "Wifi enabled";
            status = "Internet connection available";
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            //status = "Mobile data enabled";
            status = "Internet connection available";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }
}