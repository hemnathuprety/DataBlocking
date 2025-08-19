package eu.faircode.netguard;

// Anjul Khanal

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.Date;

public class WidgetAdmin extends ReceiverAutostart {
    private static final String TAG = "NetGuard.Widget";

    public static final String INTENT_ON = "eu.faircode.netguard.ON";
    public static final String INTENT_OFF = "eu.faircode.netguard.OFF";

    public static final String INTENT_LOCKDOWN_ON = "eu.faircode.netguard.LOCKDOWN_ON";
    public static final String INTENT_LOCKDOWN_OFF = "eu.faircode.netguard.LOCKDOWN_OFF";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.i(TAG, "Received " + intent);
        Util.logExtras(intent);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // Cancel set alarm
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(INTENT_ON);
        i.setPackage(context.getPackageName());


        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT | getPendingIntentFlag(PendingIntent.FLAG_MUTABLE));
        if (INTENT_ON.equals(intent.getAction()) || INTENT_OFF.equals(intent.getAction()))
            am.cancel(pi);

        // Vibrate
        Vibrator vs = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vs.hasVibrator())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                vs.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            else
                vs.vibrate(50);

        try {
            if (INTENT_ON.equals(intent.getAction()) || INTENT_OFF.equals(intent.getAction())) {
                boolean enabled = INTENT_ON.equals(intent.getAction());
                prefs.edit().putBoolean("enabled", enabled).apply();
                if (enabled)
                    ServiceSinkhole.start("widget", context);
                else
                    ServiceSinkhole.stop("widget", context, false);

                // Auto enable
                int auto = Integer.parseInt(prefs.getString("auto_enable", "0"));
                if (!enabled && auto > 0) {
                    Log.i(TAG, "Scheduling enabled after minutes=" + auto);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                        am.set(AlarmManager.RTC_WAKEUP, new Date().getTime() + auto * 60 * 1000L, pi);
                    else
                        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, new Date().getTime() + auto * 60 * 1000L, pi);
                }

            } else if (INTENT_LOCKDOWN_ON.equals(intent.getAction()) || INTENT_LOCKDOWN_OFF.equals(intent.getAction())) {
                boolean lockdown = INTENT_LOCKDOWN_ON.equals(intent.getAction());
                prefs.edit().putBoolean("lockdown", lockdown).apply();
                ServiceSinkhole.reload("widget", context, false);
                WidgetLockdown.updateWidgets(context);
            }
        } catch (Throwable ex) {
            Log.e(TAG, ex.toString() + "\n" + Log.getStackTraceString(ex));
        }
    }

    private int getPendingIntentFlag(int defaultFalg) {
        if (Build.VERSION.SDK_INT >= 23) {
            return PendingIntent.FLAG_MUTABLE;
        }
        return defaultFalg;
    }
}
