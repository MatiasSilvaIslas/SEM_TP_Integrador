package frgp.utn.edu.com.ui.electrodomesticos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.myaccount.fragmentMiPerfil;

public class ConsejosFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consejos, container, false);
        //((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
        //Acá me voy para el perfil del usurio
        ImageView btnProfile = view.findViewById(R.id.icon_user);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity() ).setnavigateToMainMenu(true);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frgment_frame, new fragmentMiPerfil());
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu_drawer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

// ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });