package frgp.utn.edu.com;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import android.widget.Toast;
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
import frgp.utn.edu.com.interfaces.OnMainMenuNavigatorListener;
import frgp.utn.edu.com.ui.Login.LoginFragment;

import frgp.utn.edu.com.ui.electrodomesticos.ConsejosFragment;
import frgp.utn.edu.com.ui.myaccount.MyAccountFragment;
import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMainMenuNavigatorListener {

    private final int FIRST_FRAGMENT = 0;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.

    private ActionBarDrawerToggle drawerToggle;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity","oncreate-log");
        NavigationView navigationView = findViewById(R.id.navigation_viewf);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutmains);

        navigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationView.getMenu().getItem(FIRST_FRAGMENT).setChecked(false);

        switchFragment(FIRST_FRAGMENT);

    }
    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        // Whenever we change the action bar, we must restore the default behaviour of the drawer
        restoreDrawer();
    }

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
        Toast.makeText(this, "Item: " + item.toString(), Toast.LENGTH_SHORT).show();

        if (item.toString().equals( "Articulos")){
            switchFragment(2);
        } else if (item.toString().equals("Cuenta")){
            switchFragment(3);
        } else if (item.toString().equals("Principal")){
            switchFragment(4);
        } else if (item.toString().equals("Cerrar sesion")){
            switchFragment(5);
        } else if (item.toString().equals("Consejos de Uso")){
            switchFragment(6);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutmains);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchFragment(int fragment){
        Fragment newFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (fragment){
            case 1:
                newFragment = new LoginFragment();
                break;
            case 2:
                newFragment = new MyAccountFragment();
                break;
            case 3:
                newFragment = new MyAccountFragment();
                break;
            case 4:
                newFragment = new PantallaPrincipalFragment();
                break;
            case 5:
                LoginRegisterViewModel loginRegisterViewModel = new LoginRegisterViewModel(getApplication());
                loginRegisterViewModel.logout(getApplicationContext());
                newFragment = new LoginFragment();
                break;
            case 6:
                newFragment = new ConsejosFragment();
                break;
            default:
                newFragment = new LoginFragment();
        }

        ((MainActivity) this ).setnavigateToMainMenu(true);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgment_frame, newFragment);
        fragmentTransaction.commit();
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

    @Override
    public void setnavigateToMainMenu(boolean navigate) {
        if (!navigate){
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

    }
}
