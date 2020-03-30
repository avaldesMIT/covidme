package me.covid.cov_idme;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides static information about the COV-ID.me application
 */
public class CovidMeApplication extends Application {

    private static String uniqueID = null;

    private static Integer riskScore = null;

    private static final String UNIQUE_ID_PREFERENCE_NAME = "UUID";

    private static final String RISK_SCORE_PREFERENCE_NAME = "SCORE";

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

    /**
     * Gets a value representing the user's infection risk score
     *
     * @return a value between 0 and 100 inclusive where 0 is no risk of being infected and 100 is certainty of
     * infection
     */
    public static Integer getRiskScore() {
        return riskScore;
    }

    /**
     * Updates the risk score in the shared preferences so that it is available the next time the application
     * is loaded
     *
     * @param score - the updated score
     */
    public synchronized void updateRiskScore(Integer score) {
        CovidMeApplication.riskScore = score;
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(RISK_SCORE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(RISK_SCORE_PREFERENCE_NAME, String.valueOf(CovidMeApplication.riskScore));
        editor.commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Context applicationContext = getApplicationContext();
        setUniqueId(applicationContext);
        setRiskScore(applicationContext);
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

    /**
     * Retrieves the user's risk score from shared preference.
     *
     * @param context - the context to use in reading the application's shared preferences
     */
    private synchronized static void setRiskScore(Context context) {
        if (riskScore == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(RISK_SCORE_PREFERENCE_NAME, Context.MODE_PRIVATE);
            String score = sharedPrefs.getString(RISK_SCORE_PREFERENCE_NAME, null);
            if (score != null) {
                riskScore = Integer.valueOf(score);
            }
        }
    }
}
