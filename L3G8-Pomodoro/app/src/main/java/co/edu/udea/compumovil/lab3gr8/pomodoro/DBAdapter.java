package co.edu.udea.compumovil.lab3gr8.pomodoro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

import java.util.ArrayList;



/**
 * Created by Sergio on 08/03/2016.
 */
public class DBAdapter {

    private static final String TAG = DBHandler.class.getSimpleName();

    //Información de la base de datos
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Preferencias.db";

    //Definicion de la tabla usuarios
    public static final String TABLA_PREFERENCIAS = "preferencias";


    public class Preferencia implements BaseColumns {
        public static final String COLUMN_ID= "id";
        public static final String COLUMN_VOLUME = "Volumen";
        public static final String COLUMN_VIBRATION = "Vibracion";
        public static final String COLUMN_SHORT_BREAK = "ShortBreak";
        public static final String COLUMN_LONG_BREAK = "LongBreak";
        public static final String COLUMN_DEBUG = "Debug";
    }



    //Sentencias para la creación de las tablas

    private static final String PREFERENCE_TABLE_CREATE = String
            .format("create table %s (%s integer primary key autoincrement, %s integer, %s integer, %s integer,%s integer, %s integer)",
                    TABLA_PREFERENCIAS,
                    Preferencia.COLUMN_ID,
                    Preferencia.COLUMN_VOLUME,
                    Preferencia.COLUMN_VIBRATION,
                    Preferencia.COLUMN_SHORT_BREAK,
                    Preferencia.COLUMN_LONG_BREAK,
                    Preferencia.COLUMN_DEBUG
            );


    private Context nContext;
    private SQLiteDatabase db;
    private DBHandler dbHandler; //Gestor de base de datos

    //Constructor de la Clase
    //Recibe como parámetro una variable de Tipo contexto
    // Esto debido a que Para acceder o crear la BD lo pide la Clase SQLiteOpenHelper
    public DBAdapter(Context context){
        this.nContext = context;
        this.dbHandler = new DBHandler(this.nContext);
    }


    //Abrir la conexion a la base de datos
    public void open() {
        this.db=dbHandler.getWritableDatabase();

    }

    //Cerrar la conexión a la base de datos
    public void close(){
        this.db.close();
    }

    private static class DBHandler extends SQLiteOpenHelper{

        DBHandler(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(PREFERENCE_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLA_PREFERENCIAS);
            onCreate(db);
        }
    }


    public void updateSettings(Settings settings){
        ContentValues values = new ContentValues();
        values.put(Preferencia.COLUMN_VOLUME,settings.getVolume());
        values.put(Preferencia.COLUMN_VIBRATION,settings.getVibration());
        values.put(Preferencia.COLUMN_SHORT_BREAK,settings.getShortBreak());
        values.put(Preferencia.COLUMN_LONG_BREAK,settings.getLongBreak());
        values.put(Preferencia.COLUMN_DEBUG,settings.getDebugMode());
        db.insert(TABLA_PREFERENCIAS,null,values);

    }

    //Retorna el usuario logeado actualmente
    public Settings getSettings(){
        String columns [] = {Preferencia.COLUMN_VOLUME,
                             Preferencia.COLUMN_VIBRATION,
                             Preferencia.COLUMN_SHORT_BREAK,
                             Preferencia.COLUMN_LONG_BREAK,
                             Preferencia.COLUMN_DEBUG};

        Cursor c1 = db.query(TABLA_PREFERENCIAS,columns,null,null,null,null,null);
        //Si el cursor esta vacio es porque no hay usuarios logeados
        Settings currentSettings = null;
        if(!c1.moveToFirst()){
            return null;
        }

        currentSettings = new Settings();
        currentSettings.setVolume(c1.getInt(0));
        currentSettings.setVibration(c1.getInt(1));
        currentSettings.setShortBreak(c1.getInt(2));
        currentSettings.setLongBreak(c1.getInt(3));
        currentSettings.setDebugMode(c1.getInt(4));
        c1.close();
        return currentSettings;
    }


       //Elimina el registro de la tabla login (Implementado para deslogearse)
    public void deleteSettings() {
        db.delete(TABLA_PREFERENCIAS,null,null);
    }

}


