package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class PomodoroService extends Service {

    //Binder given to client
    private final IBinder mBinder = new LocalBinder();
    private Counter mCounter;

    @Override
    public IBinder onBind(Intent intent) {
        //Return the communication channel to the service.
        return mBinder;
    }

    public String getRemainingTime(){

        return "";
    }

    public class LocalBinder extends Binder{
        // Return this instance of PomodoroService so clients can call public methods
        PomodoroService getService(){
            return PomodoroService.this;
        }

    }



}
