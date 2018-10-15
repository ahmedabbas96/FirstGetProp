package com.example.ahmed.firstappgetprop;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class GetProbService extends Service {

    /** A client is binding to the service with bindService() */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** The service is starting, When startService() called */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            //start getprop command
            Process getProbProcess = Runtime.getRuntime().exec("getprop");
            //get getprop result and put it in BufferedReader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getProbProcess.getInputStream()));

            // put every line in BufferedReader in StringBuilder
            StringBuilder getPropResult = new StringBuilder();
            StringBuilder CipherProp = new StringBuilder();
            String Line;
            while((Line = bufferedReader.readLine()) != null){
                getPropResult.append(Line + "\n");
                CipherProp.append(EncryptAES.encrypt(Line));
            }

            String encryptionKey = EncryptAES.key;

            // save CipherText and encryptionKey in SharedPreference
            @SuppressLint("WrongConstant")
            SharedPreferences preferences = getSharedPreferences("CipherPreference",Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("key",encryptionKey);
            editor.putString("cipher",CipherProp.toString());
            editor.commit();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    /** Called when The service is no longer used and is being destroyed by stopService()*/
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
