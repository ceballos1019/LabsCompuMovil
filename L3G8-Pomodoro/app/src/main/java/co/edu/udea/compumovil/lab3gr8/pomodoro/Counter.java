package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by telematica on 6/04/16.
 */
public class Counter extends CountDownTimer {

    private long minutes,seconds;
    private Intent broadcastIntent;
    private Context context;
    DBAdapter dbAdapter;

    public Counter(long millisInFuture, long countDownInterval){
        super(millisInFuture,countDownInterval);
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
        seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutes);
        broadcastIntent = new Intent(MainActivity.COUNTER_TICK_ACTION);
        context.sendBroadcast(broadcastIntent);
    }



    @Override
    public void onFinish() {
        broadcastIntent=new Intent(MainActivity.COUNTER_FINISH_ACTION);
        context.sendBroadcast(broadcastIntent);
        minutes=25;
        seconds=0;
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.btn_star_big_on)
                        .setContentTitle("Time out!")
                        .setContentText("El tiempo ha finalizado")
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setContentIntent(contentIntent);

        int mNotificationId = 001;
        Notification notification = mBuilder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;

        dbAdapter = new DBAdapter(context);
        dbAdapter.open();
        Settings settings = dbAdapter.getSettings();
        dbAdapter.close();
        if(settings.getVibration()==1) {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, notification);

        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;


    }

    public void setContext(Context context) {
        this.context = context;
    }
}
