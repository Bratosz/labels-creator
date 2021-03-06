package pl.bratosz.labelscreator.notification;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SSLMailNotificator {
    final String username = "naklejkomat@gmail.com";
    final String password = "Admin1234$";

    public void message(){
    Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.pop3s.ssl.trust","*");

    Session session = Session.getInstance(prop,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("naklejkomat@gmail.com"));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse("marian.gombrowicz@gmail.com")
        );
        message.setSubject("Odwiedziny strony");
        message.setText("Ktoś właśnie wszedł na stronę");

        Transport.send(message);

    } catch (MessagingException e) {
        e.printStackTrace();
    }
}
}
