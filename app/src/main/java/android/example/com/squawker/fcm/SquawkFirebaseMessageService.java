package android.example.com.squawker.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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

    }


    /**
     * Inserts a single squawk into the database;
     *
     * @param data Map which has the message data in it
     */
    private void insertSquawk(final Map<String, String> data) {

    }
}
