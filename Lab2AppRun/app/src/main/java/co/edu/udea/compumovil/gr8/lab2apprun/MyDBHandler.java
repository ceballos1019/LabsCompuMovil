package co.edu.udea.compumovil.gr8.lab2apprun;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Sergio on 08/03/2016.
 */
public class MyDBHandler extends SQLiteOpenHelper  {

    private static final String TAG = MyDBHandler.class.getSimpleName();



    //Información de la base de datos
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CarrerasDB.db";
    public static final String TABLA_USUARIOS = "usuarios";



    //Información de la tabla
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USUARIO = "usuario";
    public static final String COLUMN_CONTRASEÑA = "contraseña";
    public static final String COLUMN_EMAIL = "email";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String
                .format("create table %s (%s integer primary key autoincrement, %s text, %s text, %s text)",
                        MyDBHandler.TABLA_USUARIOS,
                        MyDBHandler.COLUMN_ID,
                        MyDBHandler.COLUMN_USUARIO,
                        MyDBHandler.COLUMN_CONTRASEÑA,
                        MyDBHandler.COLUMN_EMAIL);
        //Sentencia para crear tabla
        Log.d(TAG, "onCreate with SQL: " + sql);
        db.execSQL(sql); //Ejecución de la sentencia



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIOS);
        onCreate(db);


       }

}
