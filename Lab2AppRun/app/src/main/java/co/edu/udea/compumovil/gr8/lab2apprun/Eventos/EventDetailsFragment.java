package co.edu.udea.compumovil.gr8.lab2apprun.Eventos;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import co.edu.udea.compumovil.gr8.lab2apprun.Database.DBAdapter;
import co.edu.udea.compumovil.gr8.lab2apprun.Modelo.Event;
import co.edu.udea.compumovil.gr8.lab2apprun.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment {


    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    private FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateArticleView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentPosition);
        }
    }

    public void updateArticleView(int position) {

        //Traer los eventos de la base de datos y mostrarlos en lista
        TextView article = (TextView) getActivity().findViewById(R.id.article);
        DBAdapter dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();
        ArrayList<Event> listEvents= dbAdapter.getEvents(getContext());
        String eventDetails = listResult(listEvents,position);
        dbAdapter.close();
        article.setText(eventDetails);
        mCurrentPosition = position;
    }

    private String listResult(ArrayList<Event> listEvents, int position) {

        //Convertir el arreglo de Eventos a un arreglo de String con la informacion detallada
        ArrayList<String> stringEvents = new ArrayList<>();
        String formatString;
        Event currentEvent= listEvents.get(position);
        formatString= currentEvent.getName()+"\n\n" +
                "Descripcion: " + currentEvent.getDescription()+
                "\nDistancia: " + currentEvent.getDistance() +
                "\nLugar: " + currentEvent.getPlace() +
                "\nFecha: " + currentEvent.getDate() +
                "\nDatos de contacto: " + currentEvent.getContact();

        return formatString;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

}
