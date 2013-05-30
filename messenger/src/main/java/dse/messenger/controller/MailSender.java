package at.ac.tuwien.dse.messenger.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	private Session session;
	private final String fromMailAddress, fromName;
	
	public MailSender(String host, String user, String pass, String fromMailAddress, String fromName) {
		Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        MailAuthenticator auth = new MailAuthenticator(user, pass);
        session = Session.getDefaultInstance(properties, auth);
        this.fromMailAddress = fromMailAddress;
        this.fromName = fromName;
	}
	
	public boolean sendMail(String toMailAddress, String toName, String subject, String message) {
        Message msg = new MimeMessage(session);

        try {
			msg.setFrom(new InternetAddress(fromMailAddress, fromName));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toMailAddress, toName));
	        msg.setSubject(subject);
	        msg.setSentDate(new Date());
	        msg.setText(message);
	        msg.saveChanges();
	        Transport.send(msg);
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			//e.printStackTrace();
			return false;
		}
        return true;
	}
	
	class MailAuthenticator extends Authenticator {
		private final String user;
		private final String pass;
		
		public MailAuthenticator(String user, String pass) {
			this.user = user;
			this.pass = pass;
		}
		
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.user, this.pass);
        }
	}
}
