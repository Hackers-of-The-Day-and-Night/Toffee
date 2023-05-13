import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.MessagingException;

public class MailService {
  public void sendEmail(String recEmail, String subject, String body) {
    String from = "ahmedlearningscience@gmail.com";
    String password = "enomkdrcrvqsmfrk";
    Properties props = System.getProperties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.debug", "true");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.ssl.enable", "true");
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(from, password);
      }
    });
//    session.setDebug(true);
    try {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(recEmail));
      message.setSubject(subject);
      message.setText(body);
      Transport.send(message);
      System.out.println("Mail successfully sent");
    }
    catch (MessagingException mex) {
      mex.printStackTrace();
    }
  }

  public String sendOTP(String recEmail, String subject) {
    // Generate random 4-digit number and send it to recEmail as OTP
    int x = (int)(Math.random() * 9000 + 1000);
    String otp = toString().valueOf(x);
    String body = "Your OTP is: " + otp;
    System.out.println("Sending OTP to your email...");
    sendEmail(recEmail, subject, body);
    return otp;
  }

  public static void main(String[] args) {
    MailService mailService = new MailService();
    mailService.sendEmail("ahmedaldaramlly2003@gmail.com", "Check Integrity", "It will work InShaaAllah!");
  }

}
