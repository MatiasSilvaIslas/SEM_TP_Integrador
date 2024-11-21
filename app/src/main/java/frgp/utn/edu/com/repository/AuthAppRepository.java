package frgp.utn.edu.com.repository;

import android.app.Application;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import frgp.utn.edu.com.ui.Login.RegistrationCallback;

public class AuthAppRepository {
    private Application application;


    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    public AuthAppRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }

    public void changePassword(String email, String oldPassword, String newPassword, ChangePasswordCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, oldPassword)
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(application.getMainExecutor(), task1 -> {
                                        if (task1.isSuccessful()) {
                                            // Cerrar sesión después de cambiar la contraseña
                                            firebaseAuth.signOut();
                                            callback.onSuccess();
                                        } else {
                                            callback.onFailure(task1.getException().getMessage());
                                        }
                                    });
                        }
                    } else {
                        callback.onFailure("Error al reautenticar: " + task.getException().getMessage());
                    }
                });
    }


    public interface ChangePasswordCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userLiveData.postValue(firebaseAuth.getCurrentUser());
                            Toast.makeText(application.getApplicationContext(), "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                        }
                        else { String errorMessage = task.getException().getMessage();
                            if (errorMessage.contains("The supplied auth credential is incorrect, malformed or has expired.")) {
                                errorMessage = "La credencial de autenticación proporcionada es incorrecta.";
                            } else if (errorMessage.contains("The email address is badly formatted.")) {
                                errorMessage = "Por favor, ingresa una dirección de correo electrónico válida.";
                            } else if (errorMessage.contains("The password is invalid or the user does not have a password")) {
                                errorMessage = "La contraseña es inválida o el usuario no tiene una contraseña.";
                            }
                            // Mostrar el mensaje de error traducido
                            Toast.makeText(application.getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void register(String email, String password, RegistrationCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userLiveData.postValue(firebaseAuth.getCurrentUser());
                            callback.onSuccess();
                        } else {
                            Toast.makeText(application.getApplicationContext(), "Ya existe una cuenta registrada con este correo electrónico", Toast.LENGTH_SHORT).show();
                            callback.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }


    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
        FirebaseAuth.getInstance().signOut();
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
