package co.edu.udea.compumovil.gr8.lab2apprun.Eventos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.edu.udea.compumovil.gr8.lab2apprun.Database.DBAdapter;
import co.edu.udea.compumovil.gr8.lab2apprun.Modelo.Event;
import co.edu.udea.compumovil.gr8.lab2apprun.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventCreationFragment extends Fragment {

    private Button btnCrearEvento;
    private DBAdapter dbConexion;
    private EditText name,distance,date,description,place,phone;


    public EventCreationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_creation, container, false);

        //Capturar los views
        name = (EditText) view.findViewById(R.id.eventName);
        distance = (EditText) view.findViewById(R.id.eventDistance);
        date = (EditText) view.findViewById(R.id.eventDate);
        description = (EditText) view.findViewById(R.id.eventDescription);
        place = (EditText) view.findViewById(R.id.eventPlace);
        phone = (EditText) view.findViewById(R.id.eventPhone);
        btnCrearEvento = (Button) view.findViewById(R.id.btnCreateEvent);
        btnCrearEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEvent();

            }
        });
        dbConexion = new DBAdapter(getActivity().getApplicationContext());
        return view;
    }

    private void validateEvent() {

        //Validar que todos los campos contengan información
        String name,description,date,phone,place,d;
        int distance;
        name = this.name.getText().toString();
        description = this.description.getText().toString();
        date = this.date.getText().toString();
        d = this.distance.getText().toString();
        phone = this.phone.getText().toString();
        place = this.place.getText().toString();
        boolean correctEvent;
        correctEvent = name.equals("") ||
                description.equals("") ||
                date.equals("") ||
                d.equals("") ||
                phone.equals("") ||
                place.equals("");

        //Almacenar el evento en la base de datos
        if(!correctEvent){
            Event e = new Event();
            e.setName(name);
            e.setDescription(description);
            distance = Integer.valueOf(d);
            e.setDistance(distance);
            e.setPlace(place);
            e.setContact(phone);
            e.setDate(date);
            dbConexion.open();
            dbConexion.insertEvent(e);
            dbConexion.close();

            //Volver a la lista de eventos
            Fragment fragment = new EventListFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        }
        else{
            //Mensaje de validación
            Toast.makeText(getActivity().getApplicationContext(),"Todos los campos son obligatorios",Toast.LENGTH_LONG).show();
        }
    }


}
