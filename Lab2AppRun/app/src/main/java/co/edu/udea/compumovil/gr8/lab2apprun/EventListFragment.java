package co.edu.udea.compumovil.gr8.lab2apprun;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends ListFragment {


    OnHeadlineSelectedListener mCallback;
    private FloatingActionButton fab;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadlineSelectedListener {
        /** Called by EventListFragment when a list item is selected */
         void onArticleSelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        // Create an array adapter for the list view, using the Ipsum headlines array
        DBAdapter dbAdapter = new DBAdapter(getContext());
        dbAdapter.open();
        ArrayList<Event> listEvents= dbAdapter.getEvents(getContext());
        ArrayList<String> listEventsStrings = listResult(listEvents);
        dbAdapter.close();
        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, listEventsStrings));


    }

    private ArrayList<String> listResult(ArrayList<Event> listEvents) {
        ArrayList<String> stringEvents = new ArrayList<>();
        String formatString;
        Event currentEvent;
        for(int i=0;i<listEvents.size();i++){
            currentEvent=listEvents.get(i);
            formatString=currentEvent.getName()+" - "+currentEvent.getDistance()+" - "+currentEvent.getDate();
            stringEvents.add(formatString);
        }
        return stringEvents;
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
       /* if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onArticleSelected(position);

        // Set the item as checked to be highlighted when in two-pane layout
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //return super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_event_list,null);
    }



}
