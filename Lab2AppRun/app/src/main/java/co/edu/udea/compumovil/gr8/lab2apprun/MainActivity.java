package co.edu.udea.compumovil.gr8.lab2apprun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    //Variables globales
    private EditText user,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Capturamos los views
        user =(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);


        }


        public void onClick(View v){

            //Identificamos el boton seleccionado y lanzamos la respectiva actividad
            switch (v.getId()) {
                case R.id.button2:
                    Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(register);
                    break;
                case R.id.button:
                    if(validateLogin()) {
                        Intent events = new Intent(MainActivity.this,EventActivity.class);
                        startActivity(events);
                    }else{
                        user.setText("");
                        password.setText("");
                    }
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
        return wrongData;
    }


}
