package br.com.lecom.mailsender;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mail")
public class MailConfiguration {

	private Smtp smtp;
	private Transport transport;
	private String user;
	private String pass;

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * @return the transport
	 */
	public Transport getTransport() {
		return transport;
	}

	/**
	 * @param transport the transport to set
	 */
	public void setTransport(Transport transport) {
		this.transport = transport;
	}

	/**
	 * @return the smtp
	 */
	public Smtp getSmtp() {
		return smtp;
	}

	/**
	 * @param smtp the smtp to set
	 */
	public void setSmtp(Smtp smtp) {
		this.smtp = smtp;
	}

	public static class Smtp {
		private int port;
		private String host;
		private boolean auth;
		private Starttls starttls;
		private Ssl ssl;
		private String socketFactoryClass = "javax.net.ssl.SSLSocketFactory";

		/**
		 * @return the auth
		 */
		public boolean isAuth() {
			return auth;
		}

		/**
		 * @param auth the auth to set
		 */
		public void setAuth(boolean auth) {
			this.auth = auth;
		}

		/**
		 * @return the port
		 */
		public int getPort() {
			return port;
		}

		/**
		 * @param port the port to set
		 */
		public void setPort(int port) {
			this.port = port;
		}

		/**
		 * @return the host
		 */
		public String getHost() {
			return host;
		}

		/**
		 * @param host the host to set
		 */
		public void setHost(String host) {
			this.host = host;
		}

		/**
		 * @return the starttls
		 */
		public Starttls getStarttls() {
			return starttls;
		}

		/**
		 * @param starttls the starttls to set
		 */
		public void setStarttls(Starttls starttls) {
			this.starttls = starttls;
		}

		/**
		 * @return the ssl
		 */
		public Ssl getSsl() {
			return ssl;
		}

		/**
		 * @param ssl the ssl to set
		 */
		public void setSsl(Ssl ssl) {
			this.ssl = ssl;
		}

		/**
		 * @return the socketFactoryClass
		 */
		public String getSocketFactoryClass() {
			return socketFactoryClass;
		}

		/**
		 * @param socketFactoryClass the socketFactoryClass to set
		 */
		public void setSocketFactoryClass(String socketFactoryClass) {
			this.socketFactoryClass = socketFactoryClass;
		}

		public static class Starttls {
			private boolean enable;

			/**
			 * @return the enable
			 */
			public boolean isEnable() {
				return enable;
			}

			/**
			 * @param enable the enable to set
			 */
			public void setEnable(boolean enable) {
				this.enable = enable;
			}

			@Override
			public String toString() {
				return " mail.smtp.starttls.enable=" + enable;
			}

		}

		public static class Ssl {
			private boolean enable;
			private String trust;

			/**
			 * @return the enable
			 */
			public boolean isEnable() {
				return enable;
			}

			/**
			 * @param enable the enable to set
			 */
			public void setEnable(boolean enable) {
				this.enable = enable;
			}

			/**
			 * @return the trust
			 */
			public String getTrust() {
				return trust;
			}

			/**
			 * @param trust the trust to set
			 */
			public void setTrust(String trust) {
				this.trust = trust;
			}

			@Override
			public String toString() {
				return "mail.smtp.ssl.enable=" + enable + "\n mail.smtp.ssl.trust=" + trust;
			}

		}

		@Override
		public String toString() {
			return "\n mail.smtp.port=" + port + "\n mail.smtp.host=" + host + "\n mail.smtp.auth=" + auth + "\n"
					+ starttls + "\n " + ssl + "\n mail.smtp.socketFactory.class=" + socketFactoryClass;
		}

	}

	public static class Transport {
		private String protocol = "smtp";

		/**
		 * @return the protocol
		 */
		public String getProtocol() {
			return protocol;
		}

		/**
		 * @param protocol the protocol to set
		 */
		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

		@Override
		public String toString() {
			return "mail.transport.protocol=" + protocol;
		}

	}

	@Override
	public String toString() {
		return "MailConfiguration" + smtp + "\n " + transport + "\n mail.user=" + user + "\n mail.pass=" + pass;
	}

	public Authenticator getAuthentication() {
		return new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				if (smtp.isAuth()) {
					return new PasswordAuthentication(user, pass);
				} else {
					return new PasswordAuthentication("", "");
				}
			}

		};
	}

}
