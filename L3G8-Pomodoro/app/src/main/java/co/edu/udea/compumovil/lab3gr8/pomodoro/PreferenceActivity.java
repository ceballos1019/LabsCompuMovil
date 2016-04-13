package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.util.Set;

public class PreferenceActivity extends AppCompatActivity {

    private SeekBar volumeBar;
    private CheckBox vibration;
    private Spinner spShortBreak;
    private Spinner spLongBreak;
    private CheckBox debugMode;
    private Settings settings;
    DBAdapter dbAdapter;
    private ArrayAdapter<String> adapterShort, adapaterLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Configuración");
        setContentView(R.layout.activity_preference);

        settings = Settings.getInstance();
        dbAdapter = new DBAdapter(getApplicationContext());

        volumeBar = (SeekBar) findViewById(R.id.volume_bar);
        vibration= (CheckBox) findViewById(R.id.vibration);
        spShortBreak = (Spinner) findViewById(R.id.sp_short);
        spLongBreak = (Spinner) findViewById(R.id.sp_long);
        debugMode = (CheckBox) findViewById(R.id.debug_mode);

        String shortBreak[]= getResources().getStringArray(R.array.short_break);
        String longBreak[]= getResources().getStringArray(R.array.long_break);

        adapterShort= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,shortBreak);
        adapaterLong = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,longBreak);

        spShortBreak.setAdapter(adapterShort);
        spLongBreak.setAdapter(adapaterLong);

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                settings.setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        spShortBreak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setShortBreak(position + 3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                settings.setShortBreak(3);
            }
        });

        spLongBreak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setLongBreak(10 + (position * 5));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadConfiguration();
        Log.d("TAG", String.valueOf(settings.getVolume()));
        Log.d("TAG",String.valueOf(settings.getVibration()));
        Log.d("TAG",String.valueOf(settings.getShortBreak()));
        Log.d("TAG",String.valueOf(settings.getLongBreak()));
        Log.d("TAG",String.valueOf(settings.getDebugMode()));

    }

    private void loadConfiguration() {
        dbAdapter.open();
        settings=dbAdapter.getSettings();
        dbAdapter.close();

        if(settings!=null) {

            volumeBar.setProgress(settings.getVolume());

            int checked = settings.getVibration();
            if (checked == 1) {
                vibration.setChecked(true);
            } else {
                vibration.setChecked(false);
            }

            spShortBreak.setSelection(settings.getShortBreak() - 3);
            spLongBreak.setSelection((settings.getLongBreak() - 10) / 5);

            checked = settings.getDebugMode();
            if (checked == 1) {
                debugMode.setChecked(true);
            } else {
                debugMode.setChecked(false);
            }
        }else{
            settings = Settings.getInstance();
        }
    }

    public void onClick(View v){
        int id = v.getId();
        boolean isChecked;
        switch (id) {
            case R.id.vibration:
                isChecked = vibration.isChecked();
                if (isChecked) {
                    Log.d("TAG","CHULEADO");
                    settings.setVibration(1);
                } else{
                    settings.setVibration(0);
                    Log.d("TAG", "DESCHULEADO");
                }
                break;
            case R.id.debug_mode:
                isChecked = debugMode.isChecked();
                if (isChecked) {
                    settings.setDebugMode(1);
                } else{
                    settings.setDebugMode(0);
                }
                break;
            case R.id.btn_save:
                dbAdapter.open();
                dbAdapter.deleteSettings();
                dbAdapter.updateSettings(settings);
                dbAdapter.close();
                finish();
        }

    }
}
