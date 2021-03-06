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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Currency;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    PomodoroService mService;
    public static final String COUNTER_TICK_ACTION = "COUNTER_TICK";
    public static final String COUNTER_FINISH_ACTION = "COUNTER_FINISH";
    private static final String FORMAT = "%02d:%02d";
    private final int TOP_VALUE=25;

    private static final String TITLE_START= "Iniciar";
    private static final String TITLE_STOP= "Parar";

    private long currentTime;
    private DBAdapter dbAdapter;
    private Settings settings;

    private boolean mode = true;

    private IntentFilter mIntentFilter;
    private TextView tvTime, tvBreak,tvTask;
    private EditText etTask;
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
        settings =dbAdapter.getSettings();
        if(settings==null){
            settings= Settings.getInstance();
        }
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(COUNTER_TICK_ACTION);
        mIntentFilter.addAction(COUNTER_FINISH_ACTION);

        etTask = (EditText)findViewById(R.id.et_task);
        tvTime = (TextView)findViewById(R.id.tv_time);
        tvBreak = (TextView)findViewById(R.id.tv_break);
        tvTask = (TextView)findViewById(R.id.tv_task);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnShortBreak = (Button) findViewById(R.id.btn_short_break);
        btnLongBreak = (Button) findViewById(R.id.btn_long_break);

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
            settings=dbAdapter.getSettings();
            if(settings.getDebugMode()==1){
                tvTime.setText("00:25");
            }else{
                tvTime.setText("25:00");
            }
            etTask.setText("");
            etTask.setVisibility(EditText.VISIBLE);
            btnShortBreak.setVisibility(Button.INVISIBLE);
            btnLongBreak.setVisibility(Button.INVISIBLE);
            tvBreak.setVisibility(TextView.INVISIBLE);
            tvTask.setVisibility(TextView.VISIBLE);
        }else{
            Toast.makeText(MainActivity.this,"Recuerde tomar una descanso largo cada cuatro pomodoros",Toast.LENGTH_LONG).show();
            btnStart.setVisibility(Button.INVISIBLE);
            tvTime.setVisibility(TextView.INVISIBLE);
            btnShortBreak.setVisibility(Button.VISIBLE);
            btnLongBreak.setVisibility(Button.VISIBLE);
            tvBreak.setVisibility(TextView.VISIBLE);
            tvBreak.setText("");
            tvTask.setVisibility(TextView.INVISIBLE);
            etTask.setVisibility(EditText.INVISIBLE);
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
        settings=dbAdapter.getSettings();
        Intent intent;
        switch(id){
            case R.id.btn_start:
                if(option) {
                    if(settings.getDebugMode()==1){
                        currentTime= TimeUnit.SECONDS.toMillis(TOP_VALUE);
                    }else{
                        currentTime = TimeUnit.MINUTES.toMillis(TOP_VALUE);
                    }
                    intent = new Intent(this, PomodoroService.class);
                    bindService(intent, mConnection, BIND_AUTO_CREATE);
                    btnStart.setText(TITLE_STOP);
                    onFinish=false;
                    option=false;
                }else{
                    unbindService(mConnection);
                    btnStart.setText(TITLE_START);
                    if(settings.getDebugMode()==1){
                        tvTime.setText("00:25");
                    }else {
                        tvTime.setText("25:00");
                    }
                    option=true;
                }
                break;
            case R.id.btn_short_break:
                intent = new Intent(this, PomodoroService.class);
                if(settings.getDebugMode()==1){
                    currentTime= TimeUnit.SECONDS.toMillis(settings.getShortBreak());
                }else{
                    currentTime = TimeUnit.MINUTES.toMillis(settings.getShortBreak());
                }
                bindService(intent, mConnection, BIND_AUTO_CREATE);
                tvBreak.setText(String.format(FORMAT, settings.getShortBreak(), 0));
                btnShortBreak.setVisibility(Button.INVISIBLE);
                btnLongBreak.setVisibility(Button.INVISIBLE);
                break;
            case R.id.btn_long_break:
                intent = new Intent(this, PomodoroService.class);
                if(settings.getDebugMode()==1){
                    currentTime= TimeUnit.SECONDS.toMillis(settings.getLongBreak());
                }else{
                    currentTime = TimeUnit.MINUTES.toMillis(settings.getShortBreak());
                }
                bindService(intent, mConnection, BIND_AUTO_CREATE);
                tvBreak.setText(String.format(FORMAT, settings.getLongBreak(), 0));
                btnShortBreak.setVisibility(Button.INVISIBLE);
                btnLongBreak.setVisibility(Button.INVISIBLE);
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
