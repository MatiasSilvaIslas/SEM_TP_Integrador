package frgp.utn.edu.com;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class NotificacionesWorker extends Worker {

    public NotificacionesWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Carga las preferencias
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences("notificaciones_prefs", Context.MODE_PRIVATE);

        // Verifica cada Switch y envía notificaciones si están activados
        enviarNotificacionSiHabilitada("lamparas_enabled", "Lámparas LED", "Usá lámparas LED y mantenelas limpias.");
        enviarNotificacionSiHabilitada("lavarropas_enabled", "Lavarropas", "Lavá la ropa con programa económico y agua fría.");
        enviarNotificacionSiHabilitada("heladera_enabled", "Heladera", "Abrí la heladera la menor cantidad de tiempo posible.");
        enviarNotificacionSiHabilitada("motores_enabled", "Motores y Bombas", "Revisa motores y bombas eléctricas regularmente.");
        enviarNotificacionSiHabilitada("planchas_enabled", "Planchas", "Plancha la mayor cantidad de ropa en una sola sesión.");
        enviarNotificacionSiHabilitada("tv_enabled", "TV y Sistemas de Audio/Video", "Evita dejar los dispositivos en modo standby.");
        enviarNotificacionSiHabilitada("computadora_enabled", "Computadoras", "Apaga la computadora al terminar de usarla.");
        enviarNotificacionSiHabilitada("acc_enabled", "Aire Acondicionado", "Configura el aire a 24°C en verano y limpia los filtros.");

        return Result.success();
    }

    private void enviarNotificacionSiHabilitada(String preferenceKey, String title, String message) {
        // Carga las preferencias
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences("notificaciones_prefs", Context.MODE_PRIVATE);

        // Verifica si la notificación está habilitada
        boolean isEnabled = preferences.getBoolean(preferenceKey, false);
        if (isEnabled) {
            Object NotificationHelper = null;
            NotificationHelper.createNotification(getApplicationContext(), title, message);
        }
    }
}
