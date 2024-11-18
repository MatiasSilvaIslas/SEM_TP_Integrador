package frgp.utn.edu.com.viewmodel;

import static frgp.utn.edu.com.utils.SessionManager.getUserEmail;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
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

        SharedPreferences prefs = context.getSharedPreferences(getUserEmail(context), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

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