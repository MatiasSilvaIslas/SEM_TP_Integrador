package frgp.utn.edu.com;


import android.os.Bundle;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import frgp.utn.edu.com.databinding.ActivityMainMenuBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


public class MainMenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainMenu.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navigationView;

        // Configuración de los fragmentos del menú
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_parking, R.id.nav_myaccount)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Verificar si el usuario está autenticado al iniciar la aplicación
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Redirigir al LoginFragment si no hay usuario autenticado
            navController.navigate(R.id.Fragmentlogin);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        //return navController.navigateUp() || super.onSupportNavigateUp();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    // Método para mostrar el menú después del inicio de sesión
    public void showNavigationDrawer() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        navController.navigate(R.id.nav_parking);  // Fragmento inicial después del login
    }
}
