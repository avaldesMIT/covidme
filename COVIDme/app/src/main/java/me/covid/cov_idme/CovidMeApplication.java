package me.covid.cov_idme;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Provides static information about the COV-ID.me application
 */
public class CovidMeApplication extends Application {

    private static String uniqueID = null;

    private static final String UNIQUE_ID_PREFERENCE_NAME = "UUID";

    /**
     * Gets the pseudo-randomly generated universal unique identifier for this application. The identifier will
     * remain constant for the full lifecycle of this installation, unless the user manually deletes the
     * application's data
     *
     * @return the application's unique id
     */
    public static String getUniqueID() {
        return uniqueID;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUniqueId(getApplicationContext());
    }

    /**
     * Retrieves the universal unique identifier for this application from shared preferences. If there is no
     * identifier already set, this method will generate a new identifier and store it in preferences so that
     * it is available the next time the application is started.
     *
     * @param context - The context to use in reading the application's shared preferences
     */
    private synchronized static void setUniqueId(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(UNIQUE_ID_PREFERENCE_NAME, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(UNIQUE_ID_PREFERENCE_NAME, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(UNIQUE_ID_PREFERENCE_NAME, uniqueID);
                editor.commit();
            }
        }
    }
}
