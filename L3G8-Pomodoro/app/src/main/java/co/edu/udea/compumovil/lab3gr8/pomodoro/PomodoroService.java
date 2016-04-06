package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class PomodoroService extends Service {

    //Binder given to client
    private final IBinder mBinder = new LocalBinder();
    private Counter mCounter;
    private String remainingTime;
    private final String FORMAT = "%02d:%02d";


    @Override
    public IBinder onBind(Intent intent) {
        //Return the communication channel to the service.
        return mBinder;
    }

    public String getRemainingTime(){
        remainingTime = String.format(FORMAT,mCounter.getMinutes(),mCounter.getSeconds());
        return remainingTime;

    }

    public class LocalBinder extends Binder{
        // Return this instance of PomodoroService so clients can call public methods
        PomodoroService getService(){
            return PomodoroService.this;
        }

    }



}
