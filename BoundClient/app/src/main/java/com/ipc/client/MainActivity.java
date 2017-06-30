package com.ipc.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.net.URISyntaxException;

public class MainActivity extends Activity {

/*    private final String TAG = "BoundClient";

    // MESSAGE IDs
    private static final int MSG_CLIENT_CONNECTS = 5000;
    private static final int MSG_CLIENT_BIND = 5001;
    private static final int MSG_CLIENT_UNBIND = 5002;
    private static final int MSG_SAY_HELLO = 1;
    private static final int MSG_ACK_HELLO = 1001;
    private static final int MSG_SEND_BUNDLE = 2;
    private static final int MSG_ACK_BUNDLE = 1002;
    // MESSAGE IDs

    // Handler that receives messages from the thread
    private final class MessageHandler extends Handler {

        public MessageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, String.format("what[%d] 1[%d] 2[%d]", msg.what, msg.arg1, msg.arg2));
            switch (msg.what) {
                case MSG_ACK_HELLO:
                    Log.e(TAG, "MSG_ACK_HELLO");
                    break;
                case MSG_ACK_BUNDLE:
                    Log.e(TAG, "MSG_ACK_BUNDLE");
                    Bundle bundle = msg.getData();
                    Log.e(TAG, String.format("  message[%s]", "ttt"));
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }

    }//MessageHandler

    MessageHandler mMessageHandler = null;
    Messenger mMessenger = null;

    *//**
     * Messenger for communicating with the service.
     *//*
    Messenger mService = null;

    *//**
     * Flag indicating whether we have called bind on the service.
     *//*
    boolean mBound;

    *//**
     * Class for interacting with the main interface of the service.
     *//*

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.e(TAG, "onServiceConnected()");

            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = new Messenger(service);
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "onServiceDisconnected()");

            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mBound = false;
        }
    };

    public void sendMessage(View view) {
        if (!mBound) return;
        Message msg = Message.obtain(null, MSG_SAY_HELLO, 0, 0);
        msg.replyTo = mMessenger;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendBundle(View view) {
        if (!mBound) return;
        Bundle bundle = new Bundle();
        bundle.putString("message", "message from client");

        Message msg = Message.obtain(null, MSG_SEND_BUNDLE, 0, (int) Math.random());
        msg.replyTo = mMessenger;
        msg.setData(bundle);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "bindService()");
        // Bind to the service


        Intent i = new Intent();
        i.setAction("com.ipc.bservice.BoundService");
        i.setPackage("com.ipc.bservice");
        boolean ret = bindService(i, mConnection, Context.BIND_AUTO_CREATE);
        Log.e(TAG, "initService() bound with " + ret);


     *//*   bindService(new Intent("com.ipc.bservice"), mConnection,
                Context.BIND_AUTO_CREATE);*//*

    }//onStart

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        // create MessageHandler on separate thread
        HandlerThread thread = new HandlerThread("MessageThread",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mMessageHandler = new MessageHandler(thread.getLooper());
        mMessenger = new Messenger(mMessageHandler);*/

        startService(new Intent(this,WriteService.class));


    }//onCreate

/*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this,WriteService.class));
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
