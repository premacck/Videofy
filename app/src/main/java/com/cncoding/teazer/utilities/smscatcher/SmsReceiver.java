package com.cncoding.teazer.utilities.smscatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 *
 * Created by Prem$ on 2/9/2018.
 */

public class SmsReceiver extends BroadcastReceiver {
    private OnSmsCatchListener callback;
    private String phoneNumberFilter;
    private String filter;

    /**
     * Set result callback
     * @param callback OnSmsCatchListener
     */
    public void setCallback(OnSmsCatchListener callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                if (pdusObj != null) {
                    for (Object aPdusObj : pdusObj) {
                        SmsMessage currentMessage = getIncomingMessage(aPdusObj, bundle);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        if (phoneNumberFilter != null && !phoneNumber.equals(phoneNumberFilter)) {
                            return;
                        }
                        String message = currentMessage.getDisplayMessageBody();
                        if (filter != null && !message.matches(filter)) {
                            return;
                        }

                        if (callback != null) {
                            callback.onSmsCatch(message);
                        }
                    } // end for loop
                }
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }

    /**
     * Set phone number filter
     *
     * @param phoneNumberFilter phone number
     */
    public void setPhoneNumberFilter(String phoneNumberFilter) {
        this.phoneNumberFilter = phoneNumberFilter;
    }

    /**
     * set message filter with regexp
     *
     * @param regularExpression regexp
     */
    public void setFilter(String regularExpression) {
        this.filter = regularExpression;
    }
}