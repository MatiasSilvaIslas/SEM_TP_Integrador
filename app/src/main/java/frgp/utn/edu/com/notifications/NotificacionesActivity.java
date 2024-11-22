package frgp.utn.edu.com.notifications;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;


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

        // Crear el ColorStateList para los tintes del track
        ColorStateList trackColorStateList = ContextCompat.getColorStateList(this, R.color.switch_track_color);

        // Configurar el comportamiento para activar/desactivar todos los switches
        switchConsejosDiarios.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (Switch s : switches) {
                s.setChecked(isChecked);
                s.setTrackTintList(trackColorStateList);
            }
            // Cambiar el color del trackTint del switchConsejosDiarios
            switchConsejosDiarios.setTrackTintList(trackColorStateList);
        });

        // Configurar cada switch para que si se desactiva, el principal también se desactive
        for (Switch s : switches) {
            s.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isChecked) {
                    switchConsejosDiarios.setChecked(false);
                }
                // Cambiar el color del trackTint del switch actual
                s.setTrackTintList(trackColorStateList);
            });
        }

        // Configuración del botón para enviar notificaciones según los switches activados
        Button postNotification = findViewById(R.id.btn_notificacion);
        postNotification.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(NotificacionesActivity.this);
            builder.setTitle("Activar Notificaciones");
            builder.setMessage("Sus notificaciones serán activadas. ¿Desea continuar?");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(NotificacionesActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            CharSequence name = getString(R.string.app_name);
                            String description = "Notification String";
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
                    // Volver al fragment_pantalla_principal.xml
                    Intent intent = new Intent(NotificacionesActivity.this, MainActivity.class);
                    intent.putExtra("fragmentToLoad", "pantalla_principal");
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(NotificacionesActivity.this, "Notificaciones canceladas", Toast.LENGTH_SHORT).show();
                }
            });

            builder.show();
        });
    }

    // Método para enviar una notificación si el switch está activado con un delay
    private void enviarNotificacionSiHabilitada(Switch switchButton, String title, String content) {
        if (switchButton.isChecked()) {
            // Crear un Handler para programar el delay
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "test")
                        .setSmallIcon(R.drawable.energia_sustentable_2)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.notify((int) System.currentTimeMillis(), builder.build());
            }, 2 * 60 * 1000); // 2 minutos en milisegundos
        }
    }


    public void createNotification(View v) {
        int seconds = 60;
        String message = "Mensaje de la notificación!";
        // Asegúrate de que mServiceIntent esté inicializado
        mServiceIntent.putExtra(CommonConstants.EXTRA_MESSAGE, message);
        mServiceIntent.setAction(CommonConstants.ACTION_NOTIFY);
        Toast.makeText(this, R.string.timer_start, Toast.LENGTH_SHORT).show();

        int milliseconds = (seconds * 1000);
        mServiceIntent.putExtra(CommonConstants.EXTRA_TIMER, milliseconds);

        // Asegúrase bien de que el servicio esté definido y no sea null -> para que haga el push
        if (mServiceIntent != null) {
            startService(mServiceIntent);
        }
    }
}
