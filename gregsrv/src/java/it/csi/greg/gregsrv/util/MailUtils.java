/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;




/**
 * <p>Title: Servizi per Back-Office</p>
 * <p>Description: Servizi per Back-Office </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: CSI</p>
 * *.
 *
 * @author S.Pucci
 * @version 1.0
 */
public final class MailUtils {

    /**
     * Checks if is valid mail.
     *
     * @param mail the mail
     *
     * @return true, if is valid mail
     */
    public static boolean isValidMail(String mail) {
        int i = 0;
        if (mail != null) {
            if ((i = mail.indexOf("@")) >= 0) {
                final String seconda = mail.substring(i);
                if (seconda.length() > 0) {
                    if (seconda.indexOf(".") >= 0) {
                        try {
                            //Testing isValid Mail Address
                            InternetAddress iaSender = new InternetAddress(mail);
                            if(iaSender != null) {
                                iaSender = null;
                            }
                            return true;
                        } catch (AddressException ex) {}
                    }
                }
            }
        }
        return false;
    }

    public static void sendMail(String mailServer, String sender, String receiver, String subject, String body) throws MessagingException, IOException {
    	sendMail(mailServer, sender, receiver, null, subject, body);
    }
    public static void sendMail(String mailServer, String sender, String receiver, String receiverCC, String subject, String body) throws MessagingException, IOException {
    	sendMail(mailServer, sender, receiver, receiverCC, subject, body,null,null,null);
    }
    public static void sendMail(final String mailServer, final String sender, final String receiver, final String subject, final String body, final byte[] attachArgs, final String fileName, final String contentType) throws MessagingException, IOException {
    	sendMail(mailServer, sender, receiver, null, subject, body,attachArgs,fileName,contentType);
    }

    public static void sendMail(final String mailServer, final String sender, final String receiver, final String receiverCC, final String subject, final String body, final byte[] attachArgs, final String fileName, final String contentType) throws MessagingException, IOException {

		final Properties props = new Properties();
		props.put("mail.smtp.host", mailServer);

		final Session s = Session.getDefaultInstance(props, null);
		final Message msg = new MimeMessage(s);

		// Mittente
		msg.setFrom(new InternetAddress(sender));

		// destinatari
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));

		if (receiverCC != null) {
			msg.addRecipient(Message.RecipientType.CC, new InternetAddress(receiverCC));
		}

		// Oggetto
		msg.setSubject(subject);

		// Corpo
		msg.setText(body);

		final MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setText(body);

		if (attachArgs != null) {
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			final Multipart multipart = new MimeMultipart();
			messageBodyPart = new MimeBodyPart();

			final BytesSourceUtil source = new BytesSourceUtil(contentType);
			source.write(attachArgs);

			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(bodyPart);
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			msg.setContent(multipart);
		}

		// Invio mail
		Transport.send(msg);
	}


    /**
     * Instantiates a new mail utils.
     */
    private MailUtils() {
    }
}
