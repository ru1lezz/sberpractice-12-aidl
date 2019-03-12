package com.example.android.aidlpractice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;

public class MyService extends Service {

    private static final String VALUE = "value";

    private SharedPreferences mSharedPreferences;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new IDataInterface.Stub() {
            @Override
            public String getValue() throws RemoteException {
                return mSharedPreferences.getString(VALUE, getString(R.string.default_value));
            }

            @Override
            public void setValue(String value) throws RemoteException {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(VALUE, value);
                editor.apply();
            }
        };
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MyService.class);
        intent.setAction(IDataInterface.class.getName());
        return intent;
    }
}
