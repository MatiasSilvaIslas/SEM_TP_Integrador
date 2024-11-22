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
import android.widget.Switch;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import frgp.utn.edu.com.R;

public class NotificacionesActivity extends AppCompatActivity {
    Intent mServiceIntent;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                Toast.makeText(NotificacionesActivity.this, "Permisos otorgados para publicar notificaciones", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NotificacionesActivity.this, "No ha concedido permisos para publicar notificaciones", Toast.LENGTH_SHORT).show();
            }
        }
    });

    // Inicialización de los Switches
    private Switch switchConsejosDiarios;
    private Switch[] switches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);

        // Inicializa el Intent para el servicio de notificaciones
        mServiceIntent = new Intent(this, NotificationService.class);

        // Inicializa los Switches
        switchConsejosDiarios = findViewById(R.id.switch_consejos_diarios);
        switches = new Switch[]{
                findViewById(R.id.switch_lamparas),
                findViewById(R.id.switch_lavarropas),
                findViewById(R.id.switch_heladera),
                findViewById(R.id.switch_motores),
                findViewById(R.id.switch_planchas),
                findViewById(R.id.switch_tv),
                findViewById(R.id.switch_Computadora),
                findViewById(R.id.switch_acc)
        };

        // Configurar el comportamiento para activar/desactivar todos los switches
        switchConsejosDiarios.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (Switch s : switches) {
                s.setChecked(isChecked);
            }
        });

        // Configurar cada switch para que si se desactiva, el principal también se desactive
        for (Switch s : switches) {
            s.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isChecked) {
                    switchConsejosDiarios.setChecked(false);
                }
            });
        }

        // Configuración del botón para enviar notificaciones según los switches activados
        Button postNotification = findViewById(R.id.btn_notificacion);
        postNotification.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(NotificacionesActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = getString(R.string.app_name);
                    String description = "Ejemplo de Notification";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("test", name, importance);
                    channel.setDescription(description);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }
                // Verificar los switches y enviar las notificaciones correspondientes
                enviarNotificacionSiHabilitada(switches[0], "Lámparas LED", "Usá lámparas LED y mantenelas limpias.");
                enviarNotificacionSiHabilitada(switches[1], "Lavarropas", "Lavá la ropa con programa económico y agua fría.");
                enviarNotificacionSiHabilitada(switches[2], "Heladera", "Abrí la heladera la menor cantidad de tiempo posible.");
                enviarNotificacionSiHabilitada(switches[3], "Motores y Bombas", "Revisa motores y bombas eléctricas regularmente.");
                enviarNotificacionSiHabilitada(switches[4], "Planchas", "Plancha la mayor cantidad de ropa en una sola sesión.");
                enviarNotificacionSiHabilitada(switches[5], "TV y Sistemas de Audio/Video", "Evita dejar los dispositivos en modo standby.");
                enviarNotificacionSiHabilitada(switches[6], "Computadoras", "Apaga la computadora al terminar de usarla.");
                enviarNotificacionSiHabilitada(switches[7], "Aire Acondicionado", "Configura el aire a 24°C en verano y limpia los filtros.");
            }
        });
    }

    // Método para enviar una notificación si el switch está activado
    private void enviarNotificacionSiHabilitada(Switch switchButton, String title, String content) {
        if (switchButton.isChecked()) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "test")
                    .setSmallIcon(R.drawable.ic_add)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    public void createNotification(View v) {
        int seconds = 25;
        String message = "Mensaje de la notificación!";
        // Asegúrate de que mServiceIntent esté inicializado
        mServiceIntent.putExtra(CommonConstants.EXTRA_MESSAGE, message);
        mServiceIntent.setAction(CommonConstants.ACTION_NOTIFY);
        Toast.makeText(this, R.string.timer_start, Toast.LENGTH_SHORT).show();

        int milliseconds = (seconds * 1000);
        mServiceIntent.putExtra(CommonConstants.EXTRA_TIMER, milliseconds);

        // Asegúrate de que el servicio esté definido y no sea null
        if (mServiceIntent != null) {
            startService(mServiceIntent);
        }
    }
}
