package frgp.utn.edu.com.ui.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import frgp.utn.edu.com.R;

import java.util.Random;

public class NotificacionWorker extends Worker {

    static final String CHANNEL_ID = "consumo_recomendaciones";
    private final String[] recomendaciones = {
            "Desenchufa los electrodomésticos que no uses para evitar el consumo fantasma.",
            "Utiliza bombillas LED en lugar de incandescentes, ahorran hasta un 80% de energía.",
            "Planifica el uso de electrodomésticos de alto consumo en horarios no pico.",
            "Apaga luces en habitaciones desocupadas para reducir el consumo eléctrico.",
            "Asegúrate de que tu nevera esté bien sellada para evitar fugas de frío.",
            "Usa agua fría para lavar la ropa siempre que sea posible.",
            "Carga tu lavadora y lavavajillas al máximo para optimizar su uso.",
            "Ajusta el termostato de tu calentador eléctrico a una temperatura eficiente (alrededor de 50°C).",
            "Descongela regularmente tu congelador para mantener su eficiencia.",
            "Aprovecha la luz natural durante el día para reducir el uso de iluminación eléctrica."
    };
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

        // Seleccionar una recomendación aleatoria
        String mensaje = obtenerRecomendacionAleatoria();

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Cambia el ícono según tu diseño
                .setContentTitle("Consejo de Ahorro Energético")
                .setContentText(mensaje)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(mensaje)) // Permite mensajes largos
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        if (notificationManager != null) {
            notificationManager.notify(new Random().nextInt(1000), notification); // ID único para cada notificación
        }
    }

    private String obtenerRecomendacionAleatoria() {
        Random random = new Random();
        int index = random.nextInt(recomendaciones.length);
        return recomendaciones[index];
    }
}