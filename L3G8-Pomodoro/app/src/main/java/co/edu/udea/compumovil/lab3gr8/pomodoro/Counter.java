package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.os.CountDownTimer;

import java.util.concurrent.TimeUnit;

/**
 * Created by telematica on 6/04/16.
 */
public class Counter extends CountDownTimer {

    private final String FORMAT = "%02d:%02d";
    private long minutes,seconds;
    private String time;

    public Counter(long millisInFuture, long countDownInterval){
        super(millisInFuture,countDownInterval);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        /*time=""+String.format(FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                        */
        minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
        seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutes);
    }

    @Override
    public void onFinish() {
        time = "Finalizado";
    }
}
