package com.meetmeup.helper;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
//    static final String SERVER_URL = "http://10.0.2.2/gcm_server_php/register.php"; 

    // Google project id
    public static final String SENDER_ID = "169512048615";//"915871196467"; 

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "GCM";

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.meetmeup.activity.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
