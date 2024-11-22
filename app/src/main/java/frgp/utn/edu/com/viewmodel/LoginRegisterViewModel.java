package frgp.utn.edu.com.viewmodel;


import frgp.utn.edu.com.utils.SessionManager;
import android.app.Application;
import android.content.Context;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import frgp.utn.edu.com.repository.AuthAppRepository;

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
    public void logout(Context context) {
        authAppRepository.logOut();
        FirebaseAuth.getInstance().signOut();
        SessionManager.limpiarSesion(context);
    }


    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}