package frgp.utn.edu.com.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import frgp.utn.edu.com.MainActivity;
import frgp.utn.edu.com.R;


//Paso 1: Extendemos al servicio FirebaseMessagingService
public class MiFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "NOTICIAS";

    //Paso 2: Agregamos el metodo que hara que recepcione nuestro celular las notificaciones
    //Este metodo se generara luego del metodo onTokenRefresh y a su vez apenas nos llegue una notificacion
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Obtenemos el codigo del originante (Quien nos envia el mensaje)
        String from = remoteMessage.getFrom();
        //Se recomienda siempre crear notificaciones "LOG" internas del proyecto
        Log.d(TAG, "Mensaje recibido de: " + from);

        //Paso 3: accedemos al contenido del mensaje
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Contenido de Notificacion: " + remoteMessage);
            //Paso 4: creamos el metodo para agregar la visualizacion de la notificacion(notificationCompat)
            mostrarNotificacioN(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
        }

    }

    //Paso 4.1 Ahora estructuramos el cuerpo del NotificationCompat
    private void mostrarNotificacioN(String title, String body) {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //descargar sonido ringtone

        Intent x = new Intent(this, MainActivity.class); //Accion intent
        x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);     //Accion intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,x, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);  //Accion intent

        //estructura del NotificationCompat
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true) //de esta forma desaparecera cuando lo pulsemos
                .setSound(soundUri) // agregamos sonido de notificacion
                .setContentIntent(pendingIntent);

        //Ahora hacemos que el administrador de notificaciones del celular muestre nuestro notificacionCompat
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }
}
//HASTA ACA  YA LAS NOTIFICACIONES ESTARIA TERMINADO --> AHORA HAREMOS QUE EL MAINACTIVITY RECIBA EL MENSAJE DE NOTIFICACION