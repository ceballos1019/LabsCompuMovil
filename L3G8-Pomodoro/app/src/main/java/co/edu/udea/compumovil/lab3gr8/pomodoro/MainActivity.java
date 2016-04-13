package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    PomodoroService mService;
    public static final String COUNTER_TICK_ACTION = "COUNTER_TICK";
    public static final String COUNTER_FINISH_ACTION = "COUNTER_FINISH";

    private static final String TITLE_START= "Iniciar";
    private static final String TITLE_STOP= "Parar";

    private int currentTime;
    private DBAdapter dbAdapter;

    private boolean mode = true;

    private  IntentFilter mIntentFilter;
    private TextView tvTime, tvBreak;
    private Button btnStart, btnShortBreak, btnLongBreak;


    private boolean option = true;
    private boolean onFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this,PreferenceActivity.class);
        startActivity(intent);

        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.open();
        Settings setup = Settings.getInstance();
        setup =dbAdapter.getSettings();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(COUNTER_TICK_ACTION);
        mIntentFilter.addAction(COUNTER_FINISH_ACTION);

        tvTime = (TextView)findViewById(R.id.tv_time);
        tvBreak = (TextView)findViewById(R.id.tv_break);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnShortBreak = (Button) findViewById(R.id.btn_short_break);
        btnLongBreak = (Button) findViewById(R.id.btn_long_break);

        currentTime= 10000;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(mConnection!=null && !onFinish) {
            mode = !mode;
            unbindService(mConnection);
            setMode();

        }

    }

    public void setMode(){
        if(mode){
            btnStart.setVisibility(Button.VISIBLE);
            btnStart.setText(TITLE_START);
            tvTime.setVisibility(TextView.VISIBLE);
            tvTime.setText("25:00");
            btnShortBreak.setVisibility(Button.INVISIBLE);
            btnLongBreak.setVisibility(Button.INVISIBLE);
            tvBreak.setVisibility(TextView.INVISIBLE);
        }else{
            btnStart.setVisibility(Button.INVISIBLE);
            tvTime.setVisibility(TextView.INVISIBLE);
            btnShortBreak.setVisibility(Button.VISIBLE);
            btnLongBreak.setVisibility(Button.VISIBLE);
            tvBreak.setVisibility(TextView.VISIBLE);
            tvBreak.setText("");
        }
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
                    tvBreak.setText(mService.getRemainingTime());
                    break;
                case COUNTER_FINISH_ACTION:
                    mode = !mode;
                    setMode();
                    unbindService(mConnection);
                    onFinish=true;
                    option=true;
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
                if(option) {
                    intent = new Intent(this, PomodoroService.class);
                    bindService(intent, mConnection, BIND_AUTO_CREATE);
                    btnStart.setText(TITLE_STOP);
                    onFinish=false;
                    option=false;
                }else{
                    unbindService(mConnection);
                    btnStart.setText(TITLE_START);
                    tvTime.setText("25:00");
                    option=true;
                }
                break;
            case R.id.btn_short_break:
                intent = new Intent(this, PomodoroService.class);
                currentTime=360000;
                bindService(intent, mConnection, BIND_AUTO_CREATE);
                tvBreak.setText("3:00");
                break;
            case R.id.btn_long_break:
                intent = new Intent(this, PomodoroService.class);
                currentTime=10000;
                bindService(intent, mConnection, BIND_AUTO_CREATE);
                tvBreak.setText("10:00");
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
            mService = binder.getService(currentTime);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            option=true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,PreferenceActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
