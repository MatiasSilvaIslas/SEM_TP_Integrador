package frgp.utn.edu.com.ui.informes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;

public class tabInformeFragment  extends Fragment {
    ViewPager viewPager=null;
    ViewPagerAdapter viewPagerAdapter = null;
    TabLayout tabLayout = null;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_slide_informe,container,false);

        ((AppCompatActivity) getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));

        //((MainActivity) getActivity()).getSupportActionBar().setTitle("Informes");
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        tabLayout = view.findViewById(R.id.tabs);


        viewPager = view.findViewById(R.id.view_pager_intro);
        if (tabLayout != null && viewPager != null && viewPagerAdapter != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Informes");




            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }else {
            //this.getParentFragmentManager().beginTransaction().replace(R.id.frgment_frame, new tabInformeFragment()).commit();
            Toast.makeText(getContext(), "Error al cargar la vista", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onDestroyView() {

        viewPager = null;
        viewPagerAdapter = null;
        tabLayout = null;

        super.onDestroyView();
    }

}
