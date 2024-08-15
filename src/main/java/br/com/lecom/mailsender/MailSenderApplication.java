package br.com.lecom.mailsender;

import java.util.Arrays;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.lecom.workflow.email.EmailConfig;
import br.com.lecom.workflow.email.EmailMessage;
import br.com.lecom.workflow.email.SecureConnectionType;
import br.com.lecom.workflow.email.SendEmail;
import br.com.lecom.workflow.email.exception.SendEmailException;

@SpringBootApplication
public class MailSenderApplication implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(MailSenderApplication.class);

	@Autowired
	private MailConfiguration mailConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(MailSenderApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info(mailConfiguration.toString());
		this.sendBySendMailLecom();
		this.sendByJavaMail();
	}

	private void sendByJavaMail() {
		
		System.out.println("==============================================================");
		System.out.println("SEND BY JAVA MAIL");

		final Properties properties = System.getProperties();
		if (mailConfiguration.getSmtp().getStarttls().isEnable()) {
			properties.put("mail.smtp.starttls.enable", mailConfiguration.getSmtp().getStarttls().isEnable());
			properties.put("mail.smtp.ssl.trust", mailConfiguration.getSmtp().getHost());
		} else if (mailConfiguration.getSmtp().getSsl().isEnable()) {
			properties.put("mail.smtp.ssl.enable", mailConfiguration.getSmtp().getSsl().isEnable());
			properties.put("mail.smtp.socketFactory.class", mailConfiguration.getSmtp().getSocketFactoryClass());
		}
		
		properties.put("mail.transport.protocol", mailConfiguration.getTransport().getProtocol());
		properties.put("mail.smtp.port", mailConfiguration.getSmtp().getPort());
		properties.put("mail.smtp.host", mailConfiguration.getSmtp().getHost());
		properties.put("mail.smtp.auth", mailConfiguration.getSmtp().isAuth());

		Session session = Session.getInstance(properties, mailConfiguration.getAuthentication());
		session.setDebug(true);

		try {
			final MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailConfiguration.getUser()));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("ricardo.camargo@lecom.com.br"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("felipe.jordao@lecom.com.br"));
			message.setSubject("TLS TEST - SEND BY JAVA MAIL - Server HML");
			message.setText("TLS TEST - SEND BY JAVA MAIL - Server HML");

			Transport.send(message);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
		System.out.println("==============================================================");
	}

	private void sendBySendMailLecom() throws SendEmailException {
		System.out.println("==============================================================");
		System.out.println("SEND BY MAIL LECOM");
		final EmailConfig emailConfig = new EmailConfig();
		emailConfig.setAuthentication(mailConfiguration.getSmtp().isAuth());
		if (mailConfiguration.getSmtp().getStarttls().isEnable()) {
			emailConfig.setConnectionSecurity(SecureConnectionType.STARTTLS);
		} else if (mailConfiguration.getSmtp().getSsl().isEnable()) {
			emailConfig.setConnectionSecurity(SecureConnectionType.SSL);
		}
		emailConfig.setHost(mailConfiguration.getSmtp().getHost());
		emailConfig.setPort(Integer.valueOf(mailConfiguration.getSmtp().getPort()).shortValue());
		emailConfig.setPass(mailConfiguration.getPass());
		emailConfig.setUser(mailConfiguration.getUser());
		final SendEmail sendEmail = new SendEmail(emailConfig);
		final EmailMessage emailMessage = new EmailMessage("TLS TEST - SEND BY LECOM - Server HML", "TLS TEST - SEND BY LECOM - Server HML",
				mailConfiguration.getUser(), Arrays.asList("ricardo.camargo@lecom.com.br", "felipe.jordao@lecom.com.br"), false);
		sendEmail.send(emailMessage);
		System.out.println("==============================================================");
	}

}
