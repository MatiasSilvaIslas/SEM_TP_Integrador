package frgp.utn.edu.com.ui.articulos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArticuloViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ArticuloViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}