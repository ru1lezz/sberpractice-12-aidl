package com.example.android.aidlpractice;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements WriterFragment.OnFragmentInteractionListener {

    private IDataInterface mDataInterface;
    private ReaderFragment mReaderFragment;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDataInterface = IDataInterface.Stub.asInterface(service);

            try {
                mReaderFragment.setValue(mDataInterface.getValue());
            } catch (RemoteException ex) {
                Log.e(getClass().getName(), ex.getLocalizedMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDataInterface = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mReaderFragment = ReaderFragment.newInstance();
        WriterFragment writerFragment = WriterFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.reader_frame_layout, mReaderFragment)
                .replace(R.id.writer_frame_layout, writerFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(MyService.newIntent(MainActivity.this), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mServiceConnection);
    }

    @Override
    public void onFragmentInteraction(String value) {
        try {
            mDataInterface.setValue(value);
            mReaderFragment.setValue(mDataInterface.getValue());
        } catch (RemoteException ex) {
            Log.e(getClass().getName(), ex.getLocalizedMessage());
        }
    }
}
