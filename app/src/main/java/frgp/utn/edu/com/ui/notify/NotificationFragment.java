package frgp.utn.edu.com.ui.notify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import frgp.utn.edu.com.R;

import java.util.concurrent.TimeUnit;

import static androidx.core.content.ContextCompat.getSystemService;
import static frgp.utn.edu.com.ui.notify.NotificacionWorker.CHANNEL_ID;

public class NotificationFragment extends Fragment {

    private EditText etReminder;
    private Button btnSetReminder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));

        crearCanalNotificaciones();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                NotificacionWorker.class,
                1, // Cada 15 minutos (mínimo permitido por Android)
                TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(getActivity()).enqueue(workRequest);

        return view;
    }

    private void crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Recomendaciones de Consumo";
            String description = "Notificaciones sobre consumo eléctrico y consejos de ahorro";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Registrar el canal
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private NotificationManager getSystemService(Class<NotificationManager> notificationManagerClass) {
        return null;
    }
}
