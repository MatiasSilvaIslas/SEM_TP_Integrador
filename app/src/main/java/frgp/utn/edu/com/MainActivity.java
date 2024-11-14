package frgp.utn.edu.com;



import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import frgp.utn.edu.com.ui.Login.LoginFragment;
import frgp.utn.edu.com.ui.articulos.ArticuloFragment;
import frgp.utn.edu.com.ui.usuario.PantallaPrincipalFragment;
import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int FIRST_FRAGMENT = 0;
    private final int SECOND_FRAGMENT = 1;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.

    private ActionBarDrawerToggle drawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity","oncreate-log");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutmains);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_viewf);
        navigationView.setNavigationItemSelectedListener(this);

        // Sets the first item on drawer as selected
        navigationView.getMenu().getItem(FIRST_FRAGMENT).setChecked(true);

        // Switch to that fragment
    //    initFragment();
        switchFragment(FIRST_FRAGMENT);

    }
    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        // Whenever we change the action bar, we must restore the default behaviour of the drawer
        restoreDrawer();
    }

    /*private void initFragment(){
        // abrir login fragment, main activity tiene un framelayout y login es un fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgment_frame, new LoginFragment());
        fragmentTransaction.commit();
    }*/



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutmains);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handler the drawer toggle press
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_articulos) {
            switchFragment(FIRST_FRAGMENT);
        } else if (id == R.id.nav_parking) {
            switchFragment(SECOND_FRAGMENT);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutmains);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void switchFragment(int fragment){
        Fragment newFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (fragment){
            case FIRST_FRAGMENT:
                newFragment = new LoginFragment();
                break;
            case SECOND_FRAGMENT:
                newFragment = new ArticuloFragment();
                break;
            default:
                newFragment = new LoginFragment();
        }

        fragmentManager.beginTransaction().replace(R.id.frgment_frame, newFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutmains);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void restoreDrawer() {
        // Restores the behaviour of action bar and ActionBarDrawerToggle
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.nav_header_title,
                R.string.nav_header_subtitle
        );

        // Synchronize the state of the drawer indicator with the DrawerLayout
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }



   }
