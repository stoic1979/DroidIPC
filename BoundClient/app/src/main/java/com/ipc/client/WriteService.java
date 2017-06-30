package com.ipc.client;

import android.app.Service;
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
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by nsk on 30/6/17.
 */

public class WriteService extends Service implements Runnable {

    int i = 0;

    // MESSAGE IDs
    private static final int MSG_CLIENT_CONNECTS = 5000;
    private static final int MSG_CLIENT_BIND = 5001;
    private static final int MSG_CLIENT_UNBIND = 5002;
    private static final String MSG_SAY_HELLO = "hello msg ";
    private static final int MSG_ACK_HELLO = 1001;
    private static final int MSG_SEND_BUNDLE = 2;
    private static final int MSG_ACK_BUNDLE = 1002;
    // MESSAGE IDs


    Thread thread;
    boolean running = false;

    private final String TAG = "WriteService";


    MessageHandler mMessageHandler = null;
    Messenger mMessenger = null;

    /**
     * Messenger for communicating with the service.
     */
    Messenger mService = null;

    /**
     * Flag indicating whether we have called bind on the service.
     */
    boolean mBound;

    /**
     * Class for interacting with the main interface of the service.
     */

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


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, " service onCreate()");
        HandlerThread mthread = new HandlerThread("MessageThread",
                Process.THREAD_PRIORITY_BACKGROUND);
        mthread.start();
        mMessageHandler = new MessageHandler(mthread.getLooper());
        mMessenger = new Messenger(mMessageHandler);

        Intent i = new Intent();
        i.setAction("com.ipc.bservice.BoundService");
        i.setPackage("com.ipc.bservice");
        boolean ret = bindService(i, mConnection, Context.BIND_AUTO_CREATE);
        Log.e(TAG, "initService() bound with " + ret);


        thread = new Thread(this);
        thread.start();
        running = true;

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        Log.e(TAG, " run()");

        while (running) {
            doWork();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }//run

    private void doWork() {
        Log.e(TAG, " doWork()");

        if (!mBound) return;
        Bundle bundle = new Bundle();
        bundle.putString("message", "msg " + i++);

        Message msg = Message.obtain(null, MSG_SEND_BUNDLE, 0, (int) Math.random());
        msg.replyTo = mMessenger;
        msg.setData(bundle);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }//doWork



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
                    Log.e(TAG, String.format("  message[%s]", bundle.getString("message")));
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }

    }//MessageHandler

}
