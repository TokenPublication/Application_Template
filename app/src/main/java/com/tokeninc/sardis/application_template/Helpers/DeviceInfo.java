package com.tokeninc.sardis.application_template.Helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;

public class DeviceInfo {
    final String TAG = "DeviceInfo";

    Messenger mService = null;
    Intent serviceIntent;
    Context context;
    final String SERVICE_PACKAGE_NAME = "com.token.v1.os.launcher";
    final String SERVICE_CLASS_PATH = "com.token.v1.os.launcher.services.DeviceInfoService";

    final int DEFAULT_RESPONSE_TIMEOUT_SEC = 10;

    public static class MessageCodes
    {
        public static final int SAY_HELLO = 1; // just for testing purposes
        public static final int GET_LYNX_VERSION = 2;
        public static final int GET_MODEM_VERSION = 3;
        public static final int GET_FISCAL_ID = 4;
        public static final int GET_IMEI_NUMBER = 5;
        public static final int GET_IMSI_NUMBER = 6;
        public static final int GET_SPECIFIED_FIELDS = 7;
    }

    // values must be same as Message Codes, for user usage
    public static class Field
    {
        public static final int LYNX_VERSION = 2;
        public static final int MODEM_VERSION = 3;
        public static final int FISCAL_ID = 4;
        public static final int IMEI_NUMBER = 5;
        public static final int IMSI_NUMBER = 6;
    }

    // must be same as MessageCodes


    public DeviceInfo(Context context)
    {
        this.context = context;
        serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName(SERVICE_PACKAGE_NAME, SERVICE_CLASS_PATH));
        context.bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

    }

    private boolean bound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.

            mService = new Messenger(service);
            bound = true;
            Log.i(TAG, "onServiceConnected: ");

            for(int i = 0; i < onConnectedHandlers.size(); i++)
                onConnectedHandlers.get(i).onConnected();

            onConnectedHandlers.clear();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.

            bound = false;
            mService = null;
            Log.i(TAG, "onServiceDisconnected: ");
        }
    };

    public void sayHello() {
        if (!bound) return;
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, 1);

        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    interface OnConnectedHandler
    {
        void onConnected();
    }

    ArrayList<OnConnectedHandler> onConnectedHandlers = new ArrayList<>();

    public void unbind()
    {
        bound = false;
        context.unbindService(mConnection);
        mService = null;
    }

    public void getFiscalId(DeviceInfoResponseHandler deviceInfoResponseHandler)
    {
        getStringAnswer(MessageCodes.GET_FISCAL_ID, deviceInfoResponseHandler);
    }

    public void getImeiNumber(DeviceInfoResponseHandler deviceInfoResponseHandler)
    {
        getStringAnswer(MessageCodes.GET_IMEI_NUMBER, deviceInfoResponseHandler);
    }

    public void getImsiNumber(DeviceInfoResponseHandler deviceInfoResponseHandler)
    {
        getStringAnswer(MessageCodes.GET_IMSI_NUMBER, deviceInfoResponseHandler);
    }

    public void getLynxVersion(DeviceInfoResponseHandler deviceInfoResponseHandler)
    {
        getStringAnswer(MessageCodes.GET_LYNX_VERSION, deviceInfoResponseHandler);
    }

    public void getModemVersion(DeviceInfoResponseHandler deviceInfoResponseHandler)
    {
        getStringAnswer(MessageCodes.GET_MODEM_VERSION, deviceInfoResponseHandler);
    }

    public interface DeviceInfoResponseHandler
    {
        void onSuccess(String result);
        void onFail(String errMessage);
    }

    final String BUNDLE_REQUEST_KEY = "reqObj";
    final String BUNDLE_RESPONSE_KEY = "resObj";

    private void getStringAnswer(final int requestId, DeviceInfoResponseHandler deviceInfoResponseHandler) {
        if (!bound) {
            // deviceInfoResponseHandler.onFail("Device info service was not bound");
            onConnectedHandlers.add(()-> getStringAnswer(requestId, deviceInfoResponseHandler));
            return;
        }

        try {
            Message message = Message.obtain(null, requestId);
            message.replyTo = new Messenger(new ReplyMsgHandler(resObj -> {
                Bundle responseBundle = (Bundle) resObj;
                if(responseBundle != null && responseBundle.containsKey(BUNDLE_RESPONSE_KEY))
                    deviceInfoResponseHandler.onSuccess(responseBundle.getString(BUNDLE_RESPONSE_KEY));
                else
                    deviceInfoResponseHandler.onFail("Response was null");
            }));

            mService.send(message);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            deviceInfoResponseHandler.onFail("Unexpected Error: DI155");
        }
    }

    public interface DeviceInfoMultipleResponseHandler
    {
        void onSuccess(String[] results);
    }

    public void getFields(DeviceInfoMultipleResponseHandler deviceInfoResponseHandler, final int... fields) {
        if (!bound) {
            // deviceInfoResponseHandler.onFail("Device info service was not bound");
            onConnectedHandlers.add(()-> getFields(deviceInfoResponseHandler, fields));
            return;
        }

        try {
            Bundle inputs = new Bundle();
            inputs.putIntArray(BUNDLE_REQUEST_KEY, fields);
            Message message = Message.obtain(null, MessageCodes.GET_SPECIFIED_FIELDS);
            message.obj = inputs;
            message.replyTo = new Messenger(new ReplyMsgHandler(resObj -> {
                Bundle responseBundle = (Bundle) resObj;
                if(responseBundle != null && responseBundle.containsKey(BUNDLE_RESPONSE_KEY))
                    deviceInfoResponseHandler.onSuccess(responseBundle.getStringArray(BUNDLE_RESPONSE_KEY));
                else
                    deviceInfoResponseHandler.onSuccess(new String[fields.length]);
            }));

            mService.send(message);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            deviceInfoResponseHandler.onSuccess(new String[fields.length]);
        }
    }

    interface ResponseHandlerInterface
    {
        void onResponse(Object resObj);
    }

    class ReplyMsgHandler extends Handler {
        ResponseHandlerInterface responseHandlerInterface;

        ReplyMsgHandler(ResponseHandlerInterface responseHandlerInterface)
        {
            this.responseHandlerInterface = responseHandlerInterface;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            responseHandlerInterface.onResponse(msg.obj);
        }
    }
}
