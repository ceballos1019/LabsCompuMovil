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
    /*
    //Insertar usuario en la base de datos
    public void insertUser(String name, String password, String email){
        ContentValues values= new ContentValues();
        values.put(Preferencia.COLUMN_USER,name);
        values.put(Preferencia.COLUMN_PASSWORD,password);
        values.put(Preferencia.COLUMN_EMAIL,email);
        db.insert(TABLA_USUARIOS, null, values);
        }

    //Obtener un usuario de la base de datos
    public boolean getUser(Context context,String username,String pass){
        //Validar que los datos de login ingresados sean correctos
        String columns [] = {Preferencia.COLUMN_USER, Preferencia.COLUMN_PASSWORD};
        String selection = "Preferencia=? and Contraseña=?";
        String selectionArgs[] = {username,pass};

        Cursor c1 = db.query(TABLA_USUARIOS,columns,selection,selectionArgs,null,null,null);
        //Cursor c1 = db.query(TABLA_USUARIOS,columns,null,null,null,null,null);

        //Si el cursor esta vacio es porque el usuario o la contraseña es incorrecta
        if(!c1.moveToFirst()){
            Toast.makeText(context,"Preferencia o contraseña incorrectas",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //Insertar un evento en la base de datos
    public void insertEvent(Event e){
        ContentValues values= new ContentValues();
        values.put(Evento.EVENT_NAME,e.getName());
        values.put(Evento.EVENT_DISTANCE,e.getDistance());
        values.put(Evento.EVENT_DATE,e.getDate());
        values.put(Evento.EVENT_DESCRIPTION,e.getDescription());
        values.put(Evento.EVENT_PLACE,e.getPlace());
        values.put(Evento.EVENT_CONTACT,e.getContact());
        db.insert(TABLA_EVENTOS, null, values);
    }


    //Traer todos los eventos de la base de datos
    public ArrayList<Event> getEvents(Context context){
        //Validar que los datos de login ingresados sean correctos
        String columns [] =    {Evento.EVENT_NAME,
                                Evento.EVENT_DISTANCE,
                                Evento.EVENT_DATE,
                                Evento.EVENT_DESCRIPTION,
                                Evento.EVENT_PLACE,
                                Evento.EVENT_CONTACT };

        //Cursor c1 = db.query(TABLA_USUARIOS,columns,selection,selectionArgs,null,null,null);
        Cursor c1 = db.query(TABLA_EVENTOS,columns,null,null,null,null,null);
        ArrayList<Event> listEvents = new ArrayList<>();


        //Si el cursor esta vacio es porque no hay eventos

        if(c1.moveToFirst()){
            do{
                Event currentEvent = new Event();
                currentEvent.setName(c1.getString(0));
                currentEvent.setDistance(c1.getInt(1));
                currentEvent.setDate(c1.getString(2));
                currentEvent.setDescription(c1.getString(3));
                currentEvent.setPlace(c1.getString(4));
                currentEvent.setContact(c1.getString(5));
                listEvents.add(currentEvent);
            }while (c1.moveToNext());
        }
        if (!c1.isClosed()) {//Se cierra el cursor si no está cerrado ya
            c1.close();
        }
        return listEvents;
    }

    //Retorna el usuario logeado actualmente
    public User getCurrentUser(){
        String columns [] = {Login.LOGIN_CURRENT_USER_NAME};
        Cursor c1 = db.query(TABLA_LOGIN,columns,null,null,null,null,null);
        //Si el cursor esta vacio es porque no hay usuarios logeados
        User currentUser = null;
        if(!c1.moveToFirst()){
            return null;
        }
        String nameCurrentUser = c1.getString(0);
        c1.close();
        String columnsQuery[] = {Preferencia.COLUMN_USER, Preferencia.COLUMN_EMAIL};
        String selection = "Preferencia=?";
        String selectionArgs[] = {nameCurrentUser};
        Cursor c2 = db.query(TABLA_USUARIOS,columnsQuery,selection,selectionArgs,null,null,null);
        if(c2.moveToFirst()){
            currentUser = new User();
            currentUser.setName(c2.getString(0));
            currentUser.setEmail(c2.getString(1));
        }
        c2.close();
        return currentUser;
    }

    //Inserta el usuario actualmente logeado en la tabla Login
    public void setCurrentLogin(String currentUser){
        ContentValues values= new ContentValues();
        values.put(Login.LOGIN_CURRENT_USER_NAME, currentUser);
        db.insert(TABLA_LOGIN, null, values);

    }

    //Elimina el registro de la tabla login (Implementado para deslogearse)
    public void deleteLogin() {
        db.delete(TABLA_LOGIN,null,null);
    }

    */


}


