package com.camvels.infrastructure.adapter.out.mail;

import com.camvels.domain.model.EmailMessage;
import com.camvels.domain.model.EmailSendOutcome;
import com.camvels.domain.port.out.EmailPort;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.util.Properties;

public class SmtpEmailAdapter implements EmailPort {

    private final String smtpHost;
    private final String smtpPort;
    private final String smtpUser;
    private final String smtpPassword;

    public SmtpEmailAdapter() {
        this(
                envOrDefault("SMTP_HOST", "smtp.gmail.com"),
                envOrDefault("SMTP_PORT", "587"),
                envOrDefault("SMTP_USER", ""),
                envOrDefault("SMTP_PASSWORD", "")
        );
    }

    public SmtpEmailAdapter(String smtpHost, String smtpPort, String smtpUser, String smtpPassword) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpUser = smtpUser;
        this.smtpPassword = smtpPassword;
    }

    @Override
    public EmailSendOutcome enviar(EmailMessage message) {
        try {
            if (message.getDestinatario() == null || message.getDestinatario().trim().isEmpty()) {
                return EmailSendOutcome.fallo("El correo destinatario no puede estar vacío");
            }
            if (!message.getDestinatario().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                return EmailSendOutcome.fallo("Formato de correo destinatario inválido: " + message.getDestinatario());
            }

            Session session = Session.getInstance(buildProperties(), new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUser, smtpPassword);
                }
            });

            Message mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(smtpUser, "Camvels Minimarket"));
            mail.setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.getDestinatario()));
            mail.setSubject(message.getAsunto());
            mail.setSentDate(new java.util.Date());
            mail.setHeader("X-Mailer", "Camvels Inventario System");
            mail.setHeader("X-Priority", "3");

            byte[] adjunto = message.getArchivoAdjunto();
            String nombreAdjunto = message.getNombreArchivo();
            if (adjunto != null && adjunto.length > 0 && nombreAdjunto != null && !nombreAdjunto.trim().isEmpty()) {
                Multipart multipart = new MimeMultipart();
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(message.getCuerpo());
                multipart.addBodyPart(textPart);

                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.setFileName(nombreAdjunto);
                attachmentPart.setContent(adjunto, determinarTipoMIME(nombreAdjunto));
                attachmentPart.setDisposition(MimeBodyPart.ATTACHMENT);
                multipart.addBodyPart(attachmentPart);
                mail.setContent(multipart);
            } else {
                mail.setText(message.getCuerpo());
            }

            Transport transport = session.getTransport("smtp");
            try {
                transport.connect(smtpHost, Integer.parseInt(smtpPort), smtpUser, smtpPassword);
                transport.sendMessage(mail, mail.getAllRecipients());
                return EmailSendOutcome.exito();
            } finally {
                transport.close();
            }
        } catch (MessagingException e) {
            String errorMsg = "ERROR AL ENVIAR CORREO: " + e.getMessage();
            if (e.getNextException() != null) {
                errorMsg += " Causa: " + e.getNextException().getMessage();
            }
            return EmailSendOutcome.fallo(errorMsg);
        } catch (Exception e) {
            return EmailSendOutcome.fallo("ERROR INESPERADO: " + e.getMessage());
        }
    }

    private Properties buildProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.trust", smtpHost);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.connectiontimeout", "15000");
        props.put("mail.smtp.timeout", "15000");
        props.put("mail.smtp.writetimeout", "15000");
        props.put("mail.smtp.ssl.enable", "false");
        props.put("mail.smtp.ssl.checkserveridentity", "true");
        props.put("mail.debug", "false");
        return props;
    }

    private static String envOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null && !value.isBlank() ? value : defaultValue;
    }

    private static String determinarTipoMIME(String nombreArchivo) {
        if (nombreArchivo == null) {
            return "application/octet-stream";
        }
        String extension = nombreArchivo.toLowerCase();
        if (extension.endsWith(".pdf")) {
            return "application/pdf";
        } else if (extension.endsWith(".doc") || extension.endsWith(".docx")) {
            return "application/msword";
        } else if (extension.endsWith(".xls") || extension.endsWith(".xlsx")) {
            return "application/vnd.ms-excel";
        } else if (extension.endsWith(".txt")) {
            return "text/plain";
        } else if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (extension.endsWith(".png")) {
            return "image/png";
        } else if (extension.endsWith(".gif")) {
            return "image/gif";
        } else if (extension.endsWith(".zip")) {
            return "application/zip";
        }
        return "application/octet-stream";
    }
}
