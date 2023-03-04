package com.germangascon.notificacionestest.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * SmsReceiver
 *
 * @author Germán Gascón
 * @version 0.2, 2021-03-04
 * @since 0.1, 2019-03-04
 **/
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Log.i(TAG, "SMS recibido");
            Bundle b = intent.getExtras();
            try {
                if (b != null) {
                    /* Si hay datos Extras obtenemos los "pdus" */
                    /* "pdus" Un mensaje SMS largo puede estar dividido en varios mensajes */
                    Object[] pdus = (Object[]) b.get("pdus");
                    if (pdus.length == 0) {
                        return;
                    }
                    SmsMessage[] mensajes = new SmsMessage[pdus.length];
                    StringBuilder sbOrigin = new StringBuilder();
                    StringBuilder sbBody = new StringBuilder();
                    for (int i = 0; i < pdus.length; i++) {
                        mensajes[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        sbOrigin.append(mensajes[i].getOriginatingAddress());
                        sbBody.append(mensajes[i].getMessageBody());
                    }
                    Log.i("SmsReceiver", "Mensaje de: " + sbOrigin.toString() + " Texto: " + sbBody.toString());
                    Toast.makeText(context, "Mensaje de: " + sbOrigin.toString() + "\n" + "Texto: " + sbBody.toString(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error al procesar el mensaje SMS");
            }
        }
    }
}
