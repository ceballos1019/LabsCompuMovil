package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * Created by telematica on 6/04/16.
 */
public class Counter extends CountDownTimer {

    private long minutes,seconds;
    private Intent broadcastIntent=new Intent(MainActivity.COUNTER_TICK_ACTION);
    private Context context;

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
        context.sendBroadcast(broadcastIntent);
    }



    @Override
    public void onFinish() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.btn_star_big_on)
                        .setContentTitle("Alarma")
                        .setContentText("Se ha completado el tiempo");

        int mNotificationId = 001;
        Notification notification = mBuilder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, notification);

    }

    public void setContext(Context context) {
        this.context = context;
    }
}
