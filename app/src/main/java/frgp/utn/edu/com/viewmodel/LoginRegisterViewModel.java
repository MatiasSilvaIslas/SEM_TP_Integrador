package frgp.utn.edu.com.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseUser;
import frgp.utn.edu.com.repository.AuthAppRepository;
import frgp.utn.edu.com.ui.Login.RegistrationCallback;

public class LoginRegisterViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;

    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
    }

    public void login(String email, String password) {
        authAppRepository.login(email, password);
    }
    public void logout() {
        authAppRepository.logOut();
    }

    /*public void register(String email, String password) {
        authAppRepository.register(email, password, new RegistrationCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
*/
    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}