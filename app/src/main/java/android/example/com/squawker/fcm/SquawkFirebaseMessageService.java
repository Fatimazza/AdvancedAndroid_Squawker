package android.example.com.squawker.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Map;

/**
 * Created by fatimazza on 6/12/18.
 */

/**
  * Listens for squawk FCM messages both in the background and the foreground and responds
  * appropriately
  * depending on type of message
  */

public class SquawkFirebaseMessageService extends FirebaseMessagingService {

    private static final String JSON_KEY_AUTHOR = SquawkContract.COLUMN_AUTHOR;
    private static final String JSON_KEY_AUTHOR_KEY = SquawkContract.COLUMN_AUTHOR_KEY;
    private static final String JSON_KEY_MESSAGE = SquawkContract.COLUMN_MESSAGE;
    private static final String JSON_KEY_DATE = SquawkContract.COLUMN_DATE;

    private static final int NOTIFICATION_MAX_CHARACTERS = 30;

    private static String LOG_TAG = SquawkFirebaseMessageService.class.getSimpleName();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

         /* There are two types of messages data messages and notification messages.

            Data messages are handled here in onMessageReceived
            whether the app is in the foreground or background.
            Data messages are the type traditionally used with FCM.

            Notification messages are only received here in onMessageReceived
            when the app is in the foreground.
            When the app is in the background an automatically generated notification is displayed.

            When the user taps on the notification they are returned to the app.
            Messages containing both notification and data payloads are treated as notification messages.

            The Firebase console always sends notification messages.
            For more see: https://firebase.google.com/docs/cloud-messaging/concept-options\
         */

         /* The Squawk server always sends just *data* messages, meaning that onMessageReceived when
            the app is both in the foreground AND the background
          */

        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0) {
            Log.d(LOG_TAG, "Message data payload: " + data);

            //send notification that you got a new messages
            sendNotification(data);
            insertSquawk(data);
        }
    }


    /**
     * Create and show a simple notification containing the received FCM message
     *
     * @param data Map which has the message data in it
     */
    private void sendNotification(Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0 /* request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String author = data.get(JSON_KEY_AUTHOR);
        String message = data.get(JSON_KEY_MESSAGE);

        // If the message is longer than the max number of characters we want in our
        // notification, truncate it and add the unicode character for ellipsis
        if (message.length() > NOTIFICATION_MAX_CHARACTERS) {
            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
            new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_duck)
            .setContentTitle(String.format(getString(R.string.notification_message), author))
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notif */, notificationBuilder.build());
    }


    /**
     * Inserts a single squawk into the database;
     *
     * @param data Map which has the message data in it
     */
    private void insertSquawk(final Map<String, String> data) {

    }
}
