package me.covid.cov_idme;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import me.covid.cov_idme.ui.camera.CameraFragment;

/**
 * Provides static information about the COV-ID.me application
 */
public class CovidMeApplication extends Application {

    private static String uniqueID = null;

    private static Integer riskScore = null;

    private static Set<String> trackers = new HashSet<>();

    private static final String UNIQUE_ID_PREFERENCE_NAME = "UUID";

    private static final String RISK_SCORE_PREFERENCE_NAME = "SCORE";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

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
        if (!isInfected()) {
            CovidMeApplication.riskScore = score;
            SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(RISK_SCORE_PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(RISK_SCORE_PREFERENCE_NAME, String.valueOf(CovidMeApplication.riskScore));
            editor.commit();
        }
    }

    /**
     * Determines whether or not the user is infected, based on his or her score
     *
     * @return true if and only if the user is infected
     */
    private static boolean isInfected() {
        return riskScore.compareTo(100) >= 0;
    }

    /**
     * Adds the contact to the set of contacts for the current day
     *
     * @param tracker - the contact to add
     * @return true if and only if the tracker was successfully added
     */
    public boolean addTracker(String tracker) {
        synchronized (trackers) {
            if (!trackers.contains(tracker)) {
                trackers.add(tracker);

                String dateString = DATE_FORMAT.format(new Date());
                new UpdateTrackerFile(dateString, tracker).execute();

                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Context applicationContext = getApplicationContext();
        setUniqueId(applicationContext);
        setRiskScore(applicationContext);
        loadTrackers();
    }

    /**
     * Retrieves the universal unique identifier for this application from shared preferences. If there is no
     * identifier already set, this method will generate a new identifier and store it in preferences so that
     * it is available the next time the application is started.
     *
     * @param context - The context to use in reading the application's shared preferences
     */
    private synchronized void setUniqueId(Context context) {
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
    private synchronized void setRiskScore(Context context) {
        if (riskScore == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(RISK_SCORE_PREFERENCE_NAME, Context.MODE_PRIVATE);
            String score = sharedPrefs.getString(RISK_SCORE_PREFERENCE_NAME, null);
            if (score != null) {
                riskScore = Integer.valueOf(score);
            }
        }
    }

    /**
     * Loads the list of trackers we've seen for today
     */
    private synchronized void loadTrackers() {
        String dateString = DATE_FORMAT.format(new Date());
        new LoadTrackers(dateString).execute();
    }

    /**
     * Adds the trackers provided to the set of existing trackers
     *
     * @param trackers - the trackers to add
     */
    private void addTrackers(Set<String> trackers) {
        if (trackers != null && !trackers.isEmpty()) {
            synchronized (CovidMeApplication.trackers) {
                CovidMeApplication.trackers.addAll(trackers);
            }
        }
    }

    private class LoadTrackers extends AsyncTask<Void, Void, Set<String>> {

        private String dateString;

        LoadTrackers(String dateString) {
            this.dateString = dateString;
        }

        @Override
        protected Set<String> doInBackground(Void... voids) {
            Set<String> lines = new HashSet<>();
            try (InputStream inputStream = openFileInput(dateString);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                    Log.d("loaded", line);
                }
            } catch(Exception e) {
                Log.e("setup", "loading trackers", e);
            }

            return lines;
        }

        @Override
        protected void onPostExecute(Set<String> result) {
            addTrackers(result);
        }
    }

    private class UpdateTrackerFile extends AsyncTask<Void, Void, Void> {

        private String dateString;

        private String tracker;

        UpdateTrackerFile(String dateString, String tracker) {
            this.dateString = dateString;
            this.tracker = tracker;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try (FileOutputStream outputStream = openFileOutput(dateString, Context.MODE_APPEND & Context.MODE_PRIVATE);
                 OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {

                writer.write(tracker);
                writer.flush();

                Log.d("tracking", "Persisted to file");
            } catch (Exception e) {
                Log.e("tracking", "could not save tracker", e);
            }
            return null;
        }
    }
}
