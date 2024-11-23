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
import androidx.work.OneTimeWorkRequest;
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


        //OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificacionWorker.class).build();

// Enqueue the work request with WorkManager
        //WorkManager.getInstance(getActivity()).enqueue(workRequest);

        return view;
    }



    private NotificationManager getSystemService(Class<NotificationManager> notificationManagerClass) {
        return null;
    }
}
