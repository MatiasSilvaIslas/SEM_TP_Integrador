package frgp.utn.edu.com.ui.electrodomesticos;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.conexion.ElectrodomesticoDB;
import frgp.utn.edu.com.entidad.Electrodomestico;

import java.util.List;

public class CalculoConsumoViewModel extends ViewModel {
    private final MutableLiveData<List<Electrodomestico>> electrodomesticosLiveData = new MutableLiveData<>();
    private ElectrodomesticoDB electrodomesticoDB;

    public CalculoConsumoViewModel(Application application) {
        super();
        System.out.println("Electrodomesticos: " + electrodomesticosLiveData.getValue());
        electrodomesticoDB = new ElectrodomesticoDB(application.getApplicationContext());
        cargarElectrodomesticos();
    }

    public LiveData<List<Electrodomestico>> getElectrodomesticosLiveData() {
        return electrodomesticosLiveData;
    }

    private void cargarElectrodomesticos() {
        // Aquí se cargan los datos de forma asíncrona
        electrodomesticoDB.obtenerElectrodomesticosAsync(1, electrodomesticos -> {
            electrodomesticosLiveData.postValue(electrodomesticos);
        });
        //debug lista CONSOLE PRINT
        System.out.println("Electrodomesticos: " + electrodomesticosLiveData.getValue());

    }
}
