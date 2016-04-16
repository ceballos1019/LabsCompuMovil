package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class PomodoroService extends Service {

    //Binder given to client
    private final IBinder mBinder = new LocalBinder();
    private int totalTime;
    private Counter mCounter;
    private static final String FORMAT = "%02d:%02d";
    private String remainingTime;


    @Override
    public IBinder onBind(Intent intent) {
        //Return the communication channel to the service.
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getRemainingTime(){
        remainingTime = String.format(FORMAT,mCounter.getMinutes(),mCounter.getSeconds());
        return remainingTime;
    }

    public void setTotalTime(int totalTime){
        this.totalTime = totalTime;
    }

    @Override
    public void onDestroy() {
        mCounter.cancel();
        super.onDestroy();
    }

    public class LocalBinder extends Binder{
        // Return this instance of PomodoroService so clients can call public methods
        PomodoroService getService(long totalTime){
            mCounter = new Counter(totalTime,1000);

            mCounter.setContext(getApplicationContext());
            mCounter.start();
            return PomodoroService.this;
        }

    }



}
