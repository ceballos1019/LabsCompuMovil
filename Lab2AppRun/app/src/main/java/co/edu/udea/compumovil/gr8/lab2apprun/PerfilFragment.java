package co.edu.udea.compumovil.gr8.lab2apprun;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


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
            userName.setText(currentUser.getName());
            email.setText(currentUser.getEmail());
        }
        return view;
    }

}
