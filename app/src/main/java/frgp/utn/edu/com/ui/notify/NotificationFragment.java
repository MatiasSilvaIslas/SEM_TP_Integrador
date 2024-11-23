package frgp.utn.edu.com.ui.notify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import frgp.utn.edu.com.R;

public class NotificationFragment extends Fragment {

    private EditText etReminder;
    private Button btnSetReminder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));

        etReminder = view.findViewById(R.id.etReminder);
        btnSetReminder = view.findViewById(R.id.btnSetReminder);

        btnSetReminder.setOnClickListener(v -> {
            String reminderText = etReminder.getText().toString();
            // Iniciar el servicio de notificación
            Intent intent = new Intent(getContext(), NotificationServicelocal.class);
            intent.putExtra("reminder", reminderText);
            getContext().startService(intent);
        });
        return view;
    }
}
