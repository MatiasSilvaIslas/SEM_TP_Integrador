package frgp.utn.edu.com.ui.informes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import frgp.utn.edu.com.R;
import frgp.utn.edu.com.ui.home.PantallaPrincipalFragment;

public class tabInformeFragment  extends Fragment {
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide_informe,container,false);
        //((AppCompatActivity)getActivity()).setSupportActionBar(view.findViewById(R.tp));


        viewPagerAdapter = new ViewPagerAdapter(getContext(), getActivity().getSupportFragmentManager()); //view pager adapter
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager = view.findViewById(R.id.view_pager_intro);
        viewPager.setAdapter(viewPagerAdapter);



        return view;
    }
}
