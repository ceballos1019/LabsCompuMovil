package co.edu.udea.compumovil.gr8.lab2apprun.Eventos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.edu.udea.compumovil.gr8.lab2apprun.Database.DBAdapter;
import co.edu.udea.compumovil.gr8.lab2apprun.Modelo.User;
import co.edu.udea.compumovil.gr8.lab2apprun.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private TextView userName, email;


    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        userName = (TextView) view.findViewById(R.id.us_perfil);
        email = (TextView) view.findViewById((R.id.us_email));

        DBAdapter dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();
        User currentUser = dbAdapter.getCurrentUser();
        if(currentUser!=null){
            userName.setText("Usuario: "+ currentUser.getName());
            email.setText("Email: "+ currentUser.getEmail());
        }
        return view;
    }

}
