package co.edu.udea.compumovil.lab4gr8.weather;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView tvSearch;
    private TextView tvTest;
    private List<String> cities;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Capturar las vistas
        tvSearch = (AutoCompleteTextView) findViewById(R.id.tv_search);
        tvTest = (TextView) findViewById(R.id.tv_test);

        //Asignamos un adapter con la lista de las ciudades capitales al textview para la funcion de autocompletar
        cities = Arrays.asList(getResources().getStringArray(R.array.capital_cities));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,cities);
        tvSearch.setAdapter(adapter);
        tvSearch.setThreshold(2);
        //tvSearch.setOnItemClickListener(myClickListener);

    }

    public void onClick(View v){
        new HttpGetTask().execute();
    }

    private class HttpGetTask extends AsyncTask<Void, Void, String> {

        private static final String TAG = "HttpGetTask";
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        private String data;

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();

            //Start Progress Dialog (Message)
            Dialog.setMessage("Cargando información del clima");
            Dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            //String data = "";
            ArrayList<String> result = new ArrayList<>();
            HttpClient client = new HttpClient();
            data = client.getJSONData("Roma");


            /****************** Start Parse Response JSON Data *************/
            //String OutputData = "";
            //JSONObject responseObject;
            //try {
            /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                /*responseObject = new JSONObject(data);
                JSONArray earthquakes = responseObject.getJSONArray(EARTHQUAKE_TAG);

                // Iterate over earthquakes list
                for (int idx = 0; idx < earthquakes.length(); idx++) {

                    // Get single earthquake data - a Map
                    JSONObject earthquake = (JSONObject) earthquakes.get(idx);

                    // Summarize earthquake data as a string and add it to
                    // result
                    result.add(MAGNITUDE_TAG + ":"
                            + earthquake.get(MAGNITUDE_TAG) + ","
                            + LATITUDE_TAG + ":"
                            + earthquake.getString(LATITUDE_TAG) + ","
                            + LONGITUDE_TAG + ":"
                            + earthquake.get(LONGITUDE_TAG));

                }
                Log.v(TAG, "Result:" + result);
            } catch (JSONException e) {

                e.printStackTrace();
            }
            */
            return data;
        }

        @Override
        protected void onPostExecute(String dataList) {

            // Close progress dialog
            Dialog.dismiss();
            tvTest.setText(dataList);
            //ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, dataList);
            //listView.setAdapter(adapter);

        }
    }

}
