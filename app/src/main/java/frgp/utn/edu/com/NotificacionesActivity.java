package frgp.utn.edu.com;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class NotificacionesActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        // Inicializa las preferencias
        preferences = getSharedPreferences("notificaciones_prefs", MODE_PRIVATE);

        // Configura cada Switch y registra el Worker
        configurarSwitch(R.id.switch_lamparas, "lamparas_enabled");
        configurarSwitch(R.id.switch_lavarropas, "lavarropas_enabled");
        configurarSwitch(R.id.switch_heladera, "heladera_enabled");
        configurarSwitch(R.id.switch_motores, "motores_enabled");
        configurarSwitch(R.id.switch_planchas, "planchas_enabled");
        configurarSwitch(R.id.switch_tv, "tv_enabled");
        configurarSwitch(R.id.switch_Computadora, "computadora_enabled");
        configurarSwitch(R.id.switch_acc, "acc_enabled");

        // Inicia el Worker para verificar notificaciones diarias
        iniciarWorkerDiario();
    }

    private void configurarSwitch(int switchId, String preferenceKey) {
        SwitchCompat switchCompat = findViewById(switchId);

        // Cargar el estado inicial desde las preferencias
        boolean isEnabled = preferences.getBoolean(preferenceKey, false);
        switchCompat.setChecked(isEnabled);

        // Actualizar las preferencias cuando el usuario cambia el estado del Switch
        switchCompat.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(preferenceKey, isChecked);
            editor.apply();
        });
    }

    private void iniciarWorkerDiario() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                NotificacionesWorker.class,
                1, TimeUnit.DAYS // Se ejecuta una vez al día
        ).build();

        WorkManager.getInstance(this).enqueue(workRequest);
    }
}
