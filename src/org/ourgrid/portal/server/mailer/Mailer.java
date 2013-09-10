package org.ourgrid.portal.server.mailer;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class Mailer {
	
	public static void send(EmailNotification emailNotification){
		OurgridPortalProperties props = OurgridPortalProperties.getInstance();
		try {
			String userName = props.getProperty(OurgridPortalProperties.EMAIL_USERNAME);
			String password = props.getProperty(OurgridPortalProperties.EMAIL_PASSWORD);
			String hostName = props.getProperty(OurgridPortalProperties.EMAIL_HOST_NAME);
			String name = props.getProperty(OurgridPortalProperties.EMAIL_FROM_NAME);
			String from = props.getProperty(OurgridPortalProperties.EMAIL_FROM);
			
			SimpleEmail e = new SimpleEmail();
			e.setAuthentication(userName, password);
			e.setFrom(from, name);
			e.setHostName(hostName);
			e.addTo(emailNotification.getEmail());
			e.setSubject(emailNotification.getSubject());
			
			e.setMsg(emailNotification.getMessage());
			
			e.send();
		} catch (EmailException e1) {
			e1.printStackTrace();
		}
	}
}
