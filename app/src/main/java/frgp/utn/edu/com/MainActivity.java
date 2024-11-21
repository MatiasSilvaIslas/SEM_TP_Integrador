package frgp.utn.edu.com;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;


import android.widget.EditText;
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
import frgp.utn.edu.com.notifications.CommonConstants;
import frgp.utn.edu.com.notifications.NotificationService;
import frgp.utn.edu.com.ui.Login.LoginFragment;

import frgp.utn.edu.com.ui.electrodomesticos.ConsejosFragment;
import frgp.utn.edu.com.ui.myaccount.MyAccountFragment;
import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;
import frgp.utn.edu.com.utils.SessionManager;
import frgp.utn.edu.com.viewmodel.LoginRegisterViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMainMenuNavigatorListener {
    Intent  mServiceIntent;

    private final int FIRST_FRAGMENT = 0;
    private String userEmail;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.

    private ActionBarDrawerToggle drawerToggle;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mServiceIntent = new Intent(getApplicationContext(), NotificationService.class);
        NavigationView navigationView = findViewById(R.id.navigation_viewf);
        mDrawerLayout = findViewById(R.id.drawer_layoutmains);
        navigationView.setNavigationItemSelectedListener(this);

        // Verificar si el usuario tiene sesión iniciada
        userEmail = SessionManager.getUserEmail(getApplicationContext());

        if (userEmail == null || userEmail.isEmpty()) {
            // Si no hay sesión iniciada, oculta el NavigationView y bloquea el Drawer
            navigationView.setVisibility(View.GONE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            switchFragment(1); // Mostrar LoginFragment
        } else {
            // Si hay sesión iniciada, muestra el NavigationView y habilita el Drawer
            navigationView.setVisibility(View.VISIBLE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            switchFragment(FIRST_FRAGMENT); // El fragmento inicial que se mostrará
        }
    }
    public void createNotification(View v) {
        int seconds;
        String message = "This is my awesome text for notification!";
        mServiceIntent.putExtra(CommonConstants.EXTRA_MESSAGE, message);
        mServiceIntent.setAction(CommonConstants.ACTION_NOTIFY);
        Toast.makeText(this, R.string.timer_start, Toast.LENGTH_SHORT).show();

        EditText editText = (EditText) findViewById(R.id.edit_seconds);
        String input = editText.getText().toString();

        if (input == null || input.trim().equals("")) {
            seconds = R.string.seconds_default;
        } else {
            seconds = Integer.parseInt(input);
        }
        int milliseconds = (seconds * 1000);
        mServiceIntent.putExtra(CommonConstants.EXTRA_TIMER, milliseconds);
        startService(mServiceIntent);
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
        int id = item.getItemId();
        Toast.makeText(this, "Item: " + item.toString(), Toast.LENGTH_SHORT).show();

        if (item.toString().equals("Articulos")) {
            switchFragment(2);
        } else if (item.toString().equals("Cuenta")) {
            switchFragment(3);
        } else if (item.toString().equals("Principal")) {
            switchFragment(4);
        } else if (item.toString().equals("Cerrar sesion")) {
            showLogoutConfirmationDialog();
        } else if (item.toString().equals("Consejos de Uso")) {
            switchFragment(6);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layoutmains);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lógica para cerrar sesión
                        LoginRegisterViewModel loginRegisterViewModel = new LoginRegisterViewModel(getApplication());
                        loginRegisterViewModel.logout(getApplicationContext());
                        switchFragment(1); // Cambiar al LoginFragment

                        NavigationView navigationView = findViewById(R.id.navigation_viewf);
                        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        navigationView.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void switchFragment(int fragment) {
        Fragment newFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        NavigationView navigationView = findViewById(R.id.navigation_viewf);
        mDrawerLayout = findViewById(R.id.drawer_layoutmains);

        // Verificar el estado de sesión del usuario dinámicamente
        String currentUserEmail = SessionManager.getUserEmail(getApplicationContext());

        if ((currentUserEmail == null || currentUserEmail.isEmpty()) && fragment != 1 && fragment != 5) {
            // Si el usuario no está logueado y el fragment no es Login ni Logout
            navigationView.setVisibility(View.GONE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            newFragment = new LoginFragment(); // Cargar el LoginFragment
        } else {
            // Manejo de los diferentes fragmentos
            switch (fragment) {
                case 1: // Login
                    newFragment = new LoginFragment();
                    break;
                case 2: // Mi Cuenta
                case 3:
                    newFragment = new MyAccountFragment();
                    break;
                case 4: // Pantalla Principal
                    newFragment = new PantallaPrincipalFragment();
                    break;
                case 5: // Logout
                    LoginRegisterViewModel loginRegisterViewModel = new LoginRegisterViewModel(getApplication());
                    loginRegisterViewModel.logout(getApplicationContext());
                    newFragment = new LoginFragment();
                    navigationView.setVisibility(View.GONE);
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    break;
                case 6: // Consejos
                    newFragment = new ConsejosFragment();
                    break;
                default: // Fallback al Login
                    newFragment = new LoginFragment();
            }

            // Si el usuario está logueado, muestra el NavigationView
            if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
                navigationView.setVisibility(View.VISIBLE);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }

        // Cambiar al fragmento correspondiente
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgment_frame, newFragment);
        fragmentTransaction.commit();

        // Cerrar el Drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
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