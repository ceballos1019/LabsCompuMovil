package co.edu.udea.compumovil.gr8.lab2apprun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sergio on 08/03/2016.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String KEY_USER = "Username";
    public final static String KEY_PASS = "Password";

    //Variables globales
    private EditText username, password, email, emailConfirmation;
    private TextView passwordMessage;
    private Button btnSave;
    private ConexionBD dbConexion;
    private boolean enabledButton;  //Variable para controlar que todos los campos sean llenados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Registro");
        setContentView(R.layout.register);

        //Capturar los views como objetos
        username = (EditText) findViewById(R.id.txtUserName);
        password=(EditText) findViewById(R.id.txtPasswordRegister);
        email=(EditText) findViewById(R.id.txtEmail);
        emailConfirmation= (EditText) findViewById(R.id.txtPasswordConfirmation);
        btnSave = (Button) findViewById(R.id.btnSave);
        passwordMessage = (TextView) findViewById(R.id.txtPassMessage);

        //Se añaden los listeners
        emailConfirmation.setOnEditorActionListener(editorActionListener);
        emailConfirmation.addTextChangedListener(textWatcher);

        //Se crea la conexión a la base de datos
        dbConexion = new ConexionBD(this);
        dbConexion.abrirConexion();
}

    public void onClick(View v) {

        switch (v.getId()) {
            //Insertar datos en la base de datos
            case R.id.btnSave:
                String name = username.getText().toString();
                String password=this.password.getText().toString();
                String email = this.email.getText().toString();
                dbConexion.insert(name, password, email);
                dbConexion.cerrarConexion();
                finish();
                break;

            //Validar que no queden campos de texto vacios
            case R.id.txtUserName:
            case R.id.txtEmail:
            case R.id.txtPasswordRegister:
            case R.id.txtPasswordConfirmation:
                checkSaveButton();
                break;

            default:
                break;
        }
    }

    //Metodo para validar que se hayan llenado los  campos
    private void checkSaveButton() {
        enabledButton = username.getText().toString().equals("") ||
                        email.getText().toString().equals("") ||
                        password.getText().toString().equals("") ||
                        password.getText().toString().equals("") ||
                        !passwordMessage.getText().toString().equals("");
        btnSave.setEnabled(!enabledButton);
    }

    //TextWatcher para verificar que ambas claves ingresadas coinciden
    private final TextWatcher textWatcher = new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

       }

       @Override
       public void onTextChanged(CharSequence s, int start, int before, int count) {

       }

       @Override
       public void afterTextChanged(Editable s) {
           if(password.getText().toString().equals(s.toString())){
               passwordMessage.setText("");
           }else{
               passwordMessage.setText("Las contraseñas no coinciden");
               btnSave.setEnabled(false);
           }


       }
   };


    //Validar que cuando se termine de llenar el ultimo EditText, se hayan llenado los demás
    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId==EditorInfo.IME_ACTION_DONE) {
                checkSaveButton();
                return false;
            }
            return true;
        }
    };


    @Override
    public void finish() {

        //Preparar los datos a retornar a la actividad principal
        Intent loginData = new Intent();
        loginData.putExtra(KEY_USER,username.getText().toString());
        loginData.putExtra(KEY_PASS, password.getText().toString());

        //Finalizar la actividad y retornar los datos
        setResult(RESULT_OK,loginData);
        super.finish();
    }
}
