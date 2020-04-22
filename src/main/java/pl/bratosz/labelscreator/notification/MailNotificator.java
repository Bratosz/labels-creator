package pl.bratosz.labelscreator.notification;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class MailNotificator {
    private Properties properties;
    private Session session;
    private Message message;

    public MailNotificator() {
        configure();
        createSession();
    }

    public void create(String title, String content) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("marian.gombrowicz@gmail.com"));
            message.setSubject(title);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void createSession() {
        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("naklejkomat@gmail.com",
                        "Admin1234$");
            }
        });
    }

    private void configure() {
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
    }

    private String getActualDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy " +
                "HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

}
