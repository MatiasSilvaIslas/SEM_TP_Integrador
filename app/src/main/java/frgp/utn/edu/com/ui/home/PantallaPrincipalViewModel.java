package frgp.utn.edu.com.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PantallaPrincipalViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PantallaPrincipalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}