package frgp.utn.edu.com.servicio;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
    private static final String TAG = "EmailService";

    // Configuración del servidor de correo
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_FROM = "soporteappelectricidad91@gmail.com";
    private static final String EMAIL_PASSWORD = "aqtniudnswhyjsdt";
    private static final String[] EMAIL_TO = {
            "diegotorresmauricio@gmail.com",
            "emilianocicarelli@gmail.com",
            "matiassilvaislas@gmail.com",
            "nicoriver099@gmail.com"
    };

    public interface EmailCallback {
        void onSuccess();
        void onError(String error);
    }

    public static void enviarNotificacionNuevoCaso(String resumen, String detalle,
                                                   String emailUsuario, EmailCallback callback) {
        new SendEmailTask(resumen, detalle, emailUsuario, callback).execute();
    }

    private static class SendEmailTask extends AsyncTask<Void, Void, Boolean> {
        private final String resumen;
        private final String detalle;
        private final String emailUsuario;
        private final EmailCallback callback;
        private String errorMessage;

        public SendEmailTask(String resumen, String detalle, String emailUsuario, EmailCallback callback) {
            this.resumen = resumen;
            this.detalle = detalle;
            this.emailUsuario = emailUsuario;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.host", SMTP_HOST);
                props.put("mail.smtp.port", SMTP_PORT);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL_FROM));

                // Agregar múltiples destinatarios
                for (String email : EMAIL_TO) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                }

                // Asunto del correo
                message.setSubject("Nuevo Caso de Soporte - " + resumen);

                // Cuerpo del correo en formato HTML
                String htmlContent = String.format(
                        "<html><body>" +
                                "<h2>Nuevo Caso de Soporte</h2>" +
                                "<p><strong>Usuario:</strong> %s</p>" +
                                "<p><strong>Resumen:</strong> %s</p>" +
                                "<p><strong>Detalle:</strong></p>" +
                                "<p>%s</p>" +
                                "<hr>" +
                                "<p><em>Este es un correo automático, por favor no responder.</em></p>" +
                                "</body></html>",
                        emailUsuario,
                        resumen,
                        detalle.replace("\n", "<br>")
                );

                message.setContent(htmlContent, "text/html; charset=utf-8");

                // Enviar el mensaje
                Transport.send(message);
                return true;

            } catch (MessagingException e) {
                Log.e(TAG, "Error enviando email: " + e.getMessage());
                errorMessage = "Error al enviar la notificación por correo: " + e.getMessage();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                callback.onSuccess();
            } else {
                callback.onError(errorMessage);
            }
        }
    }
}