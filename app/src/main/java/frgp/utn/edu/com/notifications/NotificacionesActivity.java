package frgp.utn.edu.com.notifications;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;


import frgp.utn.edu.com.R;

public class NotificacionesActivity extends AppCompatActivity{
    Intent  mServiceIntent;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                Toast.makeText(NotificacionesActivity.this, "Post notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NotificacionesActivity.this, "Post notification permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);
        //mServiceIntent = new Intent(getApplicationContext(), NotificationService.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "test")
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Example Notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        Button postNotification = findViewById(R.id.ping_button);
        postNotification.getBackground();
        postNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(NotificacionesActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = getString(R.string.app_name);
                        String description = "Example Notification";
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel("test", name, importance);
                        channel.setDescription(description);
                        notificationManager.createNotificationChannel(channel);

                        notificationManager.notify(10, builder.build());
                    }
                }
            }
        });

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



    /*private SharedPreferences preferences;

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
    }*/
}
