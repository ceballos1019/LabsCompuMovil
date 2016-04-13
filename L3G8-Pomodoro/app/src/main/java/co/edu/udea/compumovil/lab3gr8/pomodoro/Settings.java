package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.util.Log;

/**
 * Created by user on 13/04/2016.
 */
public class Settings {

    private static Settings settings;

    public static Settings getInstance(){
        if(settings==null){
            settings = new Settings();
        }
        return settings;
    }

    private int volume;
    private int vibration;
    private int shortBreak;
    private int longBreak;
    private int debugMode;

    public int getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(int debugMode) {
        this.debugMode = debugMode;
    }

    public int getLongBreak() {
        return longBreak;
    }

    public void setLongBreak(int longBreak) {
        this.longBreak = longBreak;
    }

    public int getShortBreak() {
        return shortBreak;
    }

    public void setShortBreak(int shortBreak) {
        this.shortBreak = shortBreak;
    }

    public int getVibration() {
        return vibration;
    }

    public void setVibration(int vibration) {
        this.vibration = vibration;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
