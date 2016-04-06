package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by telematica on 6/04/16.
 */
public class Counter extends CountDownTimer {

    private long minutes,seconds;

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
    }

    @Override
    public void onFinish() {
    }
}
