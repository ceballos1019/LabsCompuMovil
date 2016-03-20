package co.edu.udea.compumovil.gr8.lab2apprun.Eventos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import co.edu.udea.compumovil.gr8.lab2apprun.Database.DBAdapter;
import co.edu.udea.compumovil.gr8.lab2apprun.R;

/**
 * Created by Sergio on 06/03/2016.
 */
public class EventActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EventListFragment.OnHeadlineSelectedListener {

    private String currentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currentTitle="Eventos";
        setTitle(currentTitle);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_event);
        if(findViewById(R.id.fragment_container) != null){

            Fragment listFragment = new EventListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, listFragment);
            transaction.commit();
        }


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment=null;

        int id = item.getItemId();

        //Saber que item del Navigation Drawer se escogió
        switch (id){
            case R.id.nav_event:
                fragment = new EventListFragment();
                currentTitle = "Eventos";
                break;
            case R.id.nav_perfil:
                fragment = new PerfilFragment();
                currentTitle = "Perfil";
                break;
            case R.id.nav_about:
                fragment = new AboutFragment();
                currentTitle="Acerca de";
                break;
            case R.id.nav_logout:
                backToLogin();
                break;

            default:
                break;
        }

        if(fragment!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            setTitle(currentTitle);
        }
        return true;
    }

    //Deslogearse de la aplicacion
    private void backToLogin() {
        DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.open();
        dbAdapter.deleteLogin();
        dbAdapter.close();
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(f instanceof EventCreationFragment || f instanceof  EventDetailsFragment){
                //Volver a los eventos
                Fragment fragment = new EventListFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                setTitle(currentTitle);
            }else {
                //No volver al login, sino minimizar la aplicacion
                moveTaskToBack(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArticleSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout

            Fragment newFragment = new EventDetailsFragment();
            Bundle args = new Bundle();
            args.putInt(EventDetailsFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back

            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }

    public void onClick(View v){

        int id = v.getId();
        switch (id){
            case R.id.fab:
                //Acción cuando de click en el boton flotante
                Fragment fragment = new EventCreationFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                setTitle("Registro de evento");
                break;
        }

    }

    }

