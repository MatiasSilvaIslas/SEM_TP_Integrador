package frgp.utn.edu.com;

import android.os.Bundle;
import android.util.Log;


import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import frgp.utn.edu.com.databinding.ActivityMainBinding;
import frgp.utn.edu.com.databinding.ActivityMainMenuBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import frgp.utn.edu.com.interfaces.OnMainMenuNavigatorListener;

public class MainMenuActivity extends AppCompatActivity  implements OnMainMenuNavigatorListener {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainMenuBinding binding;
    private DrawerLayout drawerLayout;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // Configura el AppBarConfiguration con los IDs de destino principales
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.loginFragment)
                .setOpenableLayout(drawerLayout)
                .build();

        // Configura la barra de acciones para funcionar con NavController y DrawerLayout
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configura el listener para cerrar el Drawer después de seleccionar un elemento
        navigationView.setNavigationItemSelectedListener(item -> {
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                drawerLayout.closeDrawers();  // Cierra el Drawer después de seleccionar un elemento
            }
            return handled;
        });

        // Verificar si el usuario está autenticado al iniciar la aplicación
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Redirigir al LoginFragment si no hay usuario autenticado
            navController.navigate(R.id.Fragmentlogin);
        }

        Log.d("MainMenuActivity","oncreate-log");
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("MainMenuActivity","onSupportNavigateUp-log");
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void setnavigateToMainMenu(boolean navigate) {
        Log.d("MainMenuActivity","setnavigateToMainMenu-log");
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.getMenu().findItem(R.id.navigation_view).setVisible(navigate);
    }
    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        //return navController.navigateUp() || super.onSupportNavigateUp();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }*/

    // Método para mostrar el menú después del inicio de sesión
    public void showNavigationDrawer() {
        Log.d("MainMenuActivity","showNavigationDrawer-log");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        navController.navigate(R.id.nav_parking);  // Fragmento inicial después del login
    }
}
