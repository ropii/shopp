package com.example.shop.functions;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

public class NetworkChangeReceiver extends BroadcastReceiver {
    Context mContext;

    ProgressDialog dialog2;
    AlertDialog dialog1;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String status = NetworkUtil.getConnectivityStatusString(context);

        Log.e("Receiver ", "" + status);

        if (status.equals("Not connected to Internet")) {

            Log.e("Receiver ", "no connection");// your code when internet lost

            AlertDialog.Builder noInternetDialog= new AlertDialog.Builder(context);
            noInternetDialog.setTitle("NETWORK ERROR")
                    .setMessage("the app needs internet connections, please connect to the internet")
                    .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false);
            noInternetDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();

                    dialog2=new ProgressDialog(context);
                    dialog2.setTitle("Waiting for internet");
                    dialog2.setCancelable(false);
                    dialog2.setMessage("Waiting...");
                    dialog2.show();
                    dialog1.dismiss();
                }
            });
            dialog1=noInternetDialog.create();
            dialog1.show();


        }
        else {
            Log.e("Receiver ", "connected to internet");//your code when internet connection come back


            if (dialog1!=null){
                dialog1.dismiss();
                Log.e("Receiver ", dialog1+"                    -");//your code when internet connection come back
            }
            if (dialog2!=null){
                dialog2.dismiss();
                Log.e("Receiver ", dialog2+"                +");//your code when internet connection come back

            }


        }

    }
}



