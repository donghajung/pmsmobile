package pms.com.mobile.push;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import pms.com.mobile.Main;
import pms.com.mobile.R;
import pms.com.mobile.globalclass.log.RbLog;

public class GcmIntentService extends IntentService
{
    public static final int NOTIFICATION_ID = 1;
    public GcmIntentService()
    {
        super("GcmIntentService");
    }

    Bundle extras;


    @Override
    protected void onHandleIntent(Intent intent)
    {
        extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty())
        { // has effect of unparcelling Bundle
      /*
       * Filter messages based on message type. Since it is likely that GCM will
       * be extended in the future with new message types, just ignore any
       * message types you're not interested in, or that you don't recognize.
       */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                sendNotification("Send error: " + extras.toString());

                RbLog.dd("1111GcmIntentService.java | onHandleIntent", "Received: " + extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
            {
                sendNotification("Deleted messages on server: " + extras.toString());

                RbLog.dd("2222GcmIntentService.java | onHandleIntent", "Received: " + extras.toString());
                // If it's a regular GCM message, do some work.
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                String msg = intent.getStringExtra("msg");
                // Post notification of received message.
//            sendNotification("Received: " + extras.toString());
                sendNotification("Received: " + msg);
                RbLog.dd("33333GcmIntentService.java | onHandleIntent", "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg)
    {

//        RbLog.ee("intentservice","show me the");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent =new Intent(this,Main.class);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(extras.getString("title"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText("gcmtest"))
                .setContentText(extras.getString("content"))
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500});

        Log.d("jdh", "1 " + intent);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}