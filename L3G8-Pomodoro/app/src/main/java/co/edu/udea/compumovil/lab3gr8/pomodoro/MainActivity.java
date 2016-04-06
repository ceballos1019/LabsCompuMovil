package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    PomodoroService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View v){
        int id = v.getId();
        Intent intent;
        switch(id){
            case R.id.btn_start:
                intent = new Intent(this, PomodoroService.class);
                bindService(intent,mConnection,BIND_AUTO_CREATE);
                break;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PomodoroService.LocalBinder binder = (PomodoroService.LocalBinder) service;
            mService = binder.getService();
            //mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            //mBound = false;
        }
    };

}
