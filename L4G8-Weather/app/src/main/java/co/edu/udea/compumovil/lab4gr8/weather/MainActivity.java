package co.edu.udea.compumovil.lab4gr8.weather;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView tvSearch;
    private TextView tvCity, tvTemperature, tvHumidity, tvDescription;
    private ImageView ivWeather;
    private List<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Capturar las vistas
        tvSearch = (AutoCompleteTextView) findViewById(R.id.tv_search);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvTemperature = (TextView) findViewById(R.id.tv_temperature);
        tvHumidity = (TextView) findViewById(R.id.tv_humidity);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        ivWeather = (ImageView) findViewById(R.id.iv_weather_image);

        //Asignamos un adapter con la lista de las ciudades capitales al textview para la funcion de autocompletar
        cities = Arrays.asList(getResources().getStringArray(R.array.capital_cities));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,cities);
        tvSearch.setAdapter(adapter);
        tvSearch.setThreshold(2);
    }

    public void onClick(View v){
        String city = tvSearch.getText().toString();
        if(checkConnection()) {
            new HttpGetTask().execute(city);
        }else{
            Toast.makeText(this,"Verifique su conexión a internet",Toast.LENGTH_LONG).show();
        }
    }

    private class HttpGetTask extends AsyncTask<String, Void, City> {

        private static final String TAG = "HttpGetTask";
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        private String data;

        @Override
        protected void onPreExecute() {
            //Start Progress Dialog (Message)
            Dialog.setMessage("Cargando información del clima");
            Dialog.show();
        }

        @Override
        protected City doInBackground(String... params) {
            HttpClient client = new HttpClient();
            String cityName = params[0];
            String language = getResources().getString(R.string.idioma);
            data = client.getJSONData(cityName,language);
            Log.d(TAG,"Data:"+data);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Weather.class,new WeatherDeserializer());
            Gson gson = gsonBuilder.create();
            Weather currentWeather=null;
            try {
                currentWeather = gson.fromJson(data, Weather.class);
            }catch(com.google.gson.JsonSyntaxException ex){
                return null;
            }
            if(currentWeather==null){
                //FALTA MANEJAR ESTE CASO
                Log.d(TAG,"ES NULOO");
            }else {
                byte[] b = client.downloadImage(currentWeather.getIconCode());
                currentWeather.setImageWeather(b);
                City city = new City();
                city.setName(cityName);
                city.setWeather(currentWeather);
                return city;
            }

            return null;
        }

        @Override
        protected void onPostExecute(City city) {

            // Close progress dialog
            Dialog.dismiss();
            if(city!=null) {
                Weather weather = city.getWeather();
                tvCity.setText(city.getName());
                tvTemperature.setText(String.valueOf(weather.getTemperature()));
                tvHumidity.setText(String.valueOf(weather.getHumidity()));
                tvDescription.setText(weather.getDescription());

                byte[] imgWeather = weather.getImageWeather();
                Bitmap bitmapWeather = BitmapFactory.decodeByteArray(imgWeather, 0, imgWeather.length);
                ivWeather.setImageBitmap(bitmapWeather);
            }else{

                //FALTA MANEJAR ESTE CASO
            }

        }
    }

    private boolean checkConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable()) {
            return true;
        }
        return false;
    }

}
