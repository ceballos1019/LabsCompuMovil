package co.edu.udea.compumovil.gr8.lab1ui;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private AutoCompleteTextView autoComplete;
    private ArrayAdapter<String> adapterPais;
    private ArrayAdapter<String> adapterHobbies;
    private Spinner listaHobbies;
    private Button btnMostrar;
    private TextView texto;
    private EditText nombre,apellido,telefono,direccion,email;
    private TextView fechaNacimiento;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int currentYear, currentMonth,currentDay;
    private CheckBox favorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentYear = getCurrentYear();
        currentMonth = getCurrentMonth();
        currentDay=getCurrentDay();
        establecerUI();
    }

    public void establecerUI(){

        //Se obtiene el string usando getresources()
        String pais[]= getResources().getStringArray(R.array.Pais);
        String hobbies[]= getResources().getStringArray(R.array.hobbies);

        //Crea adaptador
        adapterPais=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,pais);
        adapterHobbies= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,hobbies);

        //Registrar autocomplete
        autoComplete= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        listaHobbies=(Spinner) findViewById(R.id.spinner);
        btnMostrar=(Button) findViewById(R.id.button);
        texto=(TextView) findViewById(R.id.textView4);
        favorito=(CheckBox) findViewById(R.id.checkBox);

        //Identificar los demas views
        nombre=(EditText) findViewById(R.id.editText);
        apellido=(EditText) findViewById(R.id.editText2);
        telefono=(EditText)findViewById(R.id.editText3);
        direccion=(EditText)findViewById(R.id.editText4);
        email=(EditText)findViewById(R.id.editText5);
        fechaNacimiento = (TextView) findViewById(R.id.textView3);

        //Configurar adaptador para el autocomplete y el spinner
        autoComplete.setAdapter(adapterPais);
        autoComplete.setThreshold(1);
        listaHobbies.setAdapter(adapterHobbies);

    }

    public void mostrarInfoContacto(View v){

        //Capturamos los datos ingresados
        String txtNombre = nombre.getText().toString();
        String txtApellido = apellido.getText().toString();
        String txtPais = autoComplete.getText().toString();
        String txtTelefono = telefono.getText().toString();
        String txtDireccion = direccion.getText().toString();
        String txtEmail = email.getText().toString();

        //Validamos que todos los campos sean llenados
        boolean infoCompleta = true;
        RadioGroup grupo= (RadioGroup) findViewById(R.id.grupo);
        RadioButton radioChecked;
        int selectedRadio=grupo.getCheckedRadioButtonId();
        if(selectedRadio==-1){
            infoCompleta=false;
        }
        if(TextUtils.isEmpty(txtNombre))infoCompleta=false;
        if(TextUtils.isEmpty(txtApellido)) infoCompleta=false;
        if(TextUtils.isEmpty(txtPais)) infoCompleta=false;
        if(TextUtils.isEmpty(txtTelefono)) infoCompleta=false;
        if(TextUtils.isEmpty(txtDireccion)) infoCompleta=false;
        if(TextUtils.isEmpty(txtEmail)) infoCompleta=false;
        if(mYear==0) infoCompleta=false;
        if(!infoCompleta) {
            Toast.makeText(this,"Revise que todos los campos esten completos",Toast.LENGTH_SHORT).show();
            return;
        }else if(mYear>currentYear){
            Toast.makeText(this,"Ingrese una fecha de nacimiento valida",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(mMonth>currentMonth){
            Toast.makeText(this,"Ingrese una fecha de nacimiento valida",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(mDay>currentDay){
            Toast.makeText(this,"Ingrese una fecha de nacimiento valida",Toast.LENGTH_SHORT).show();
            return;
        }
        radioChecked = (RadioButton) findViewById(selectedRadio);
        String txtSexo = radioChecked.getText().toString();

        //Desplegamos la informacion en el EditText
        texto.setText("");
        texto.append(getString(R.string.nombre, txtNombre));
        texto.append(getString(R.string.apellido, txtApellido));
        texto.append(getString(R.string.sexo, txtSexo));
        texto.append(getString(R.string.pais, txtPais));
        texto.append(getString(R.string.telefono, txtTelefono));
        texto.append(getString(R.string.direccion, txtDireccion));
        texto.append(getString(R.string.email, txtEmail));
        texto.append("\n"+listaHobbies.getSelectedItem().toString());
        texto.append(getString(R.string.fecha_nacimiento,mDay,mMonth+1,mYear));
        if(favorito.isChecked()){
            texto.append("\nContacto favorito");
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        updateDisplay();
    }

    public int getCurrentYear(){
        Calendar c = GregorianCalendar.getInstance();
        Date actualDate= c.getTime();
        c.setTime(actualDate);
        int year=c.get(Calendar.YEAR);
        return year;
    }
    public int getCurrentMonth(){
        Calendar c = GregorianCalendar.getInstance();
        Date actualDate= c.getTime();
        c.setTime(actualDate);
        int month=c.get(Calendar.MONTH);
        return month;
    }
    public int getCurrentDay(){
        Calendar c = GregorianCalendar.getInstance();
        Date actualDate= c.getTime();
        c.setTime(actualDate);
        int day=c.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public void onClick(View v){
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    private void updateDisplay() {
        fechaNacimiento.setText(new StringBuilder().append("Fecha de nacimiento: ")
                // Month is 0 based so add 1
                .append(mDay).append("/").append(mMonth+1).append("/")
                .append(mYear).append(" "));
    }
}
