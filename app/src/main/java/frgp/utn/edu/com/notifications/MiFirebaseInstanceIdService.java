package frgp.utn.edu.com.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import frgp.utn.edu.com.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MiFirebaseInstanceIdService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        // Extract message data
        String title = message.getData().get("title");
        String body = message.getData().get("body");
        String imageUrl = message.getData().get("imageUrl");
        String clickAction = message.getData().get("click_action");

        // Create intent for notification click action
        Intent intent = new Intent(clickAction);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Build pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Build notification
        Notification.Builder builder = new Notification.Builder(this, "channel_id")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_info)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH);

        // Load image if available
        /*if (imageUrl != null) {
            Uri imageUri = Uri.parse(imageUrl);
            byte[] imageBytes = new byte[0];
            try (InputStream inputStream = imageUri.openStream()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                imageBytes = bos.toByteArray();
            } catch (IOException e) {
                Log.e("Image", "Error loading image", e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Create large icon
            if (!imageBytes.) {
                builder.setLargeIcon(BitmapFactory.decodeStream(new ByteArrayInputStream(imageBytes)));
                builder.setStyle(new NotificationCompat.BigPictureStyle)));
            }
        }*/

        // Show notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}