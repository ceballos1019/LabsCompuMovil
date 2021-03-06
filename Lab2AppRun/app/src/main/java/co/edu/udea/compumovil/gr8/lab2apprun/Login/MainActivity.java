package co.edu.udea.compumovil.gr8.lab2apprun.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import co.edu.udea.compumovil.gr8.lab2apprun.Database.DBAdapter;
import co.edu.udea.compumovil.gr8.lab2apprun.Eventos.EventActivity;
import co.edu.udea.compumovil.gr8.lab2apprun.Modelo.User;
import co.edu.udea.compumovil.gr8.lab2apprun.R;
import co.edu.udea.compumovil.gr8.lab2apprun.Registro.RegisterActivity;

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
        DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.open();
        User isOn = dbAdapter.getCurrentUser();

        if(isOn!=null) {
            Intent events = new Intent(MainActivity.this, EventActivity.class);
            startActivity(events);
        }
        }


        public void onClick(View v){

            //Identificamos el boton seleccionado y lanzamos la respectiva actividad
            switch (v.getId()) {
                case R.id.btnLogin:
                    if(validateLogin()) {
                        //Se guarda el login en la base de datos
                        DBAdapter dbAdapter = new DBAdapter(this);
                        dbAdapter.open();
                        dbAdapter.setCurrentLogin(user.getText().toString());
                        dbAdapter.close();

                        //Login correcto - Se lanza la otra actividad
                        Intent events = new Intent(MainActivity.this,EventActivity.class);
                        startActivity(events);
                    }
                    //Se limpian los campos de texto
                    user.setText("");
                    password.setText("");
                    break;
                case R.id.btnRegister:
                    //Lanzar la actividad para registrarse
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
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        wrongData=dbAdapter.getUser(getApplicationContext(),user.getText().toString(),password.getText().toString());
        dbAdapter.close();
        return wrongData;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Tomar los datos de registro para el login
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            if (data.hasExtra(RegisterActivity.KEY_USER) && data.hasExtra(RegisterActivity.KEY_PASS)) {
                user.setText(data.getExtras().getString(RegisterActivity.KEY_USER));
                password.setText(data.getExtras().getString(RegisterActivity.KEY_PASS));
            }

        }
    }
}
