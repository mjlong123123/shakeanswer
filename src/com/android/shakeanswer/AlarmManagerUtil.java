
package com.android.shakeanswer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * @author funzhang This class provides access to the system alarm services.
 *         These allow you to schedule your application to be run at some point
 *         in the future.
 */
public class AlarmManagerUtil {

    public static final int CLASS_TYPE_BROADCAST = 0;
    public static final int CLASS_TYPE_SERVICE = 1;
    public static final int CLASS_TYPE_ACTIVITY = 2;

    public static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * @param context context
     * @param cls class
     * @param classType
     *            CLASS_TYPE_BROADCAST/CLASS_TYPE_SERVICE/CLASS_TYPE_ACTIVITY
     * @param action service onstartcommand
     * @param alarmType One of ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP}, RTC
     *            or RTC_WAKEUP
     * @param triggerAtTime Time the alarm should first go off, using the
     *            appropriate clock (depending on the alarm type).
     * @param interval Interval between subsequent repeats of the alarm.
     */

    public static void setAlarm(Context context, Class<?> cls, int classType, String action,
            int alarmType, long triggerAtTime, long interval) {
        AlarmManager alarmManager = getAlarmManager(context);
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//        intent.setAction(action);
        PendingIntent operation;
        switch (classType) {
            case CLASS_TYPE_BROADCAST:
                operation = PendingIntent.getBroadcast(context, 0, intent, 0);
                break;
            case CLASS_TYPE_SERVICE:
                operation = PendingIntent.getService(context, 0, intent, 0);
                break;
            case CLASS_TYPE_ACTIVITY:
                operation = PendingIntent.getActivity(context, 0, intent, 0);
                break;
            default:
                operation = PendingIntent.getBroadcast(context, 0, intent, 0);
                break;
        }
        if (interval != 0) {
            alarmManager.setRepeating(alarmType, triggerAtTime, interval, operation);
        } else {
            alarmManager.set(alarmType, triggerAtTime, operation);
        }

    }

    public static void cancelAlarm(Context context, Class<?> cls, String action, int classType) {
        AlarmManager alarmManager = getAlarmManager(context);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent operation;
        switch (classType) {
            case 0:
                operation = PendingIntent.getBroadcast(context, 0, intent, 0);
                break;
            case 1:
                operation = PendingIntent.getService(context, 0, intent, 0);
                break;
            case 2:
                operation = PendingIntent.getActivity(context, 0, intent, 0);
                break;
            default:
                operation = PendingIntent.getBroadcast(context, 0, intent, 0);
                break;
        }
        alarmManager.cancel(operation);
    }

}
