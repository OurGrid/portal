package org.ourgrid.portal.server.mailer;

public class MailFactory {
	
	private static String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private static final String NO_REPLY = LINE_SEPARATOR + LINE_SEPARATOR 
		+ "This is a notification message. Please do not reply to this email. Your message will not be received.";
	
	private static final String GREETINGS = LINE_SEPARATOR + LINE_SEPARATOR
		+ "Thanks, " + LINE_SEPARATOR + "The OurGridPortal Team";
	
	public static EmailNotification buildJobFinishMail(String email, String label){
		
		String subject = "Job " + label + " has been finished.";
		String message = "Job " + label + " final state was FINISHED." +
						 LINE_SEPARATOR + "Access OurgridPortal site to download the job results." + NO_REPLY + GREETINGS;							;
			
		return new EmailNotification(email, subject, message);
		
	}
	
	public static EmailNotification buildAccountWaitConfirmationMail(String email, String login){
		
		String subject = "Your pending account confirmation";
		String message = "Hello, " + LINE_SEPARATOR + LINE_SEPARATOR 
			+ "Your account (login: " + login + ") awaits confirmation of the administrators. "
			+ "If this is *not* you, please disregard this message." + NO_REPLY + GREETINGS;
		
		return new EmailNotification(email, subject, message);
		
	}
	
	public static EmailNotification buildNewAccountEmail(String email, String login){
		
		String subject = "New account pending confirmation";
		String message = "A new account (login: " + login + ") arrived, and awaits confirmation. "
			+ "Please access OurGridPortal site to accept or decline the confirmation." + NO_REPLY + GREETINGS;
		
		return new EmailNotification(email, subject, message);
		
	}
	
	public static EmailNotification buildApprovedAcountEmail(String email, String login){
		
		String subject = "Your account was just confirmed";
		String message = "Hello " + login + "," + LINE_SEPARATOR + LINE_SEPARATOR
			+ "Your account was just confirmed. Thanks for registering with OurGridPortal. "
			+ "Now you can submit and monitor your jobs on OurGrid. " + NO_REPLY + GREETINGS;
		
		return new EmailNotification(email, subject, message);
		
	}
	
	public static EmailNotification buildNewPasswordEmail(String email, String login, String newPasswd) {
		
		String subject = "New Password for your OurGridPortal Account";
		String message = "Hello " + login + "," + LINE_SEPARATOR + LINE_SEPARATOR
			+ "You have just requested to send you the password to this e-mail address. since we store your password encrypted "
			+ "we cannot retrieve it back due to security restrictions." + LINE_SEPARATOR + LINE_SEPARATOR
			+ "We have changed your password, and now your login information is:" + LINE_SEPARATOR + LINE_SEPARATOR
			+ "Login: " + login + LINE_SEPARATOR + "Your New (Changed) Password: " + newPasswd + LINE_SEPARATOR + LINE_SEPARATOR
			+ "We suggest you change your password to something more personal."
			+ "It's a simple process and will make it easier to remember." + LINE_SEPARATOR + LINE_SEPARATOR
			+ "If this is *not* you, please disregard this message." + NO_REPLY + GREETINGS;
		
		return new EmailNotification(email, subject, message);
		
	}
}