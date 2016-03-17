package co.edu.udea.compumovil.gr8.lab2apprun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Sergio on 08/03/2016.
 */
public class ConexionBD {

    private SQLiteDatabase db;
    private Context nContext;
    private MyDBHandler handler;


    //Constructor de la Clase
    //Recibe como parámetro una variable de Tipo contexto
    // Esto debido a que Para acceder o crear la BD lo pide la Clase SQLiteOpenHelper

    public ConexionBD(Context c){
        nContext=c;

    }

    public void abrirConexion() {
        handler =new MyDBHandler(nContext);
        db=handler.getWritableDatabase();

    }

    public void cerrarConexion(){
        handler.close();
    }

    public void insert(String nombre, String contraseña,String email){
        ContentValues values= new ContentValues();
        values.put(handler.COLUMN_USUARIO,nombre);
        values.put(handler.COLUMN_CONTRASEÑA,contraseña);
        values.put(handler.COLUMN_EMAIL,email);
        db.insert(handler.TABLA_USUARIOS, null, values);
        }

    public boolean getUser(Context context,String username,String pass){

        //Validar que los datos de login ingresados sean correctos
        String columns [] = {MyDBHandler.COLUMN_USUARIO,MyDBHandler.COLUMN_CONTRASEÑA};
        String selection = "usuario=? and contraseña=?";
        String selectionArgs[] = {username,pass};
        Cursor c1 = db.query(MyDBHandler.TABLA_USUARIOS,columns,selection,selectionArgs,null,null,null);
        //Cursor c1 = db.query(MyDBHandler.TABLA_USUARIOS,columns,null,null,null,null,null);
        //Si el cursor esta vacio es porque el usuario o la contraseña es incorrecta
        if(!c1.moveToFirst()){
            Toast.makeText(context,"Usuario o contraseña incorrectas",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}


