package co.edu.udea.compumovil.gr8.lab2apprun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Sergio on 08/03/2016.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    EditText nombre, password, email;
    Button btnGuardar;
    ConexionBD dbconeccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registar);
        nombre = (EditText) findViewById(R.id.nombre);
        password=(EditText) findViewById(R.id.password);
        email=(EditText) findViewById(R.id.email);
        btnGuardar = (Button) findViewById(R.id.btnAgregarId);

        dbconeccion = new ConexionBD(this);

        dbconeccion.abrirConexion();
        btnGuardar.setOnClickListener(this);
}

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAgregarId:
                String name = nombre.getText().toString();
                String password=this.password.getText().toString();
                String email = this.email.getText().toString();
                dbconeccion.insert(name, password, email);
                Intent main = new Intent(RegisterActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
                break;

            default:
                break;
        }
    }
}
