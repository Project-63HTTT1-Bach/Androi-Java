package com.example.quizapp.Auth.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GmailSender {

    private static final String EMAIL = "quizapp4756@gmail.com"; // Thay bằng email của bạn
    private static final String PASSWORD = "ofad izzy ozcq fgnq"; // Thay bằng mật khẩu email của bạn

    public static void sendEmail(String toEmail, String subject, String body) {
        new SendEmailTask().execute(toEmail, subject, body);
    }

    private static class SendEmailTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String toEmail = params[0];
            String subject = params[1];
            String body = params[2];

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL, PASSWORD);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);
                Log.d("GmailSender", "Email sent successfully");
            } catch (MessagingException e) {
                Log.e("GmailSender", "Error sending email", e);
            }

            return null;
        }
    }
}
