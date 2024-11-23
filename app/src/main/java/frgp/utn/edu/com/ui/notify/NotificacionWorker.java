package frgp.utn.edu.com.ui.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import frgp.utn.edu.com.R;

public class NotificacionWorker extends Worker {

    static final String CHANNEL_ID = "consumo_recomendaciones";

    public NotificacionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        enviarNotificacion();
        return Result.success();
    }

    private void enviarNotificacion() {
        Context context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Mensaje de recomendación
        String mensaje = "Reduce el uso de electrodomésticos en horas pico para ahorrar energía.";

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Consejo de Ahorro Energético")
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    }
}

