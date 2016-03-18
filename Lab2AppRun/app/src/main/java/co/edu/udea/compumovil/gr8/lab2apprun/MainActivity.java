package co.edu.udea.compumovil.gr8.lab2apprun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_CODE = 25;

    //Variables globales
    private EditText user,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_main);

        //Capturamos los views
        user =(EditText)findViewById(R.id.txtUserLogin);
        password=(EditText)findViewById(R.id.txtPasswordLogin);

        }


        public void onClick(View v){

            //Identificamos el boton seleccionado y lanzamos la respectiva actividad
            switch (v.getId()) {
                case R.id.btnLogin:
                    if(validateLogin()) {
                        //Login correcto - Se lanza la otra actividad
                        Intent events = new Intent(MainActivity.this,EventActivity.class);
                        startActivity(events);
                    }else{
                        //Logeo incorrecto - Se limpian los campos de texto
                        user.setText("");
                        password.setText("");
                    }
                    break;
                case R.id.btnRegister:
                    Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivityForResult(register, REQUEST_CODE);
                    break;
            }
        }

    private boolean validateLogin() {

        //Validamos el logeo
        boolean userEmpty, passwordEmpty,wrongData;
        userEmpty = user.getText().toString().equals("");
        passwordEmpty = password.getText().toString().equals("");

        //Validar que el campo usuario no este vacio
        if(userEmpty){
            Toast.makeText(getApplicationContext(),"Introduce tu nombre de usuario",Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validar que el campo contraseña no este vacio
        if(passwordEmpty){
            Toast.makeText(getApplicationContext(),"Introduce tu contraseña",Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validar que los datos ingresados esten correctos
        ConexionBD bd = new ConexionBD(this);
        bd.abrirConexion();
        wrongData=bd.getUser(getApplicationContext(),user.getText().toString(),password.getText().toString());
        bd.cerrarConexion();
        return wrongData;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            if (data.hasExtra(RegisterActivity.KEY_USER) && data.hasExtra(RegisterActivity.KEY_PASS)) {
                user.setText(data.getExtras().getString(RegisterActivity.KEY_USER));
                password.setText(data.getExtras().getString(RegisterActivity.KEY_PASS));
            }

        }
    }
}
