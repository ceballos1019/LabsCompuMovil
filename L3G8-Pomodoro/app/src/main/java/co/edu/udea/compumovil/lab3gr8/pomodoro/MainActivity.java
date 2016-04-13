package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    PomodoroService mService;
    public static final String COUNTER_TICK_ACTION = "COUNTER_TICK";
    public static final String COUNTER_FINISH_ACTION = "COUNTER_FINISH";
    private  IntentFilter mIntentFilter;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(COUNTER_TICK_ACTION);
        mIntentFilter.addAction(COUNTER_FINISH_ACTION);

        tvTime = (TextView)findViewById(R.id.tv_time);

    }

    @Override
    protected void onResume() {
        registerReceiver(mTimeReceiver, mIntentFilter);
        super.onResume();
    }

    private BroadcastReceiver mTimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case COUNTER_TICK_ACTION:
                    tvTime.setText(mService.getRemainingTime());
                    break;
                case COUNTER_FINISH_ACTION:
                    tvTime.setText("25:00");
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        unregisterReceiver(mTimeReceiver);
        super.onPause();
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
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

}
