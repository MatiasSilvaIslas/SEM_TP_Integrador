package frgp.utn.edu.com.ui.electrodomesticos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;

public class ConsejosFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consejos, container, false);
        //((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
        return view;
    }
}

// ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });