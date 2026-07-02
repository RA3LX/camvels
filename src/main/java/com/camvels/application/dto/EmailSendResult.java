package com.camvels.application.dto;

public class EmailSendResult {

    private final boolean exito;
    private final String mensaje;
    private final String emailDestinatario;

    private EmailSendResult(boolean exito, String mensaje, String emailDestinatario) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.emailDestinatario = emailDestinatario;
    }

    public static EmailSendResult exito(String emailDestinatario) {
        return new EmailSendResult(true, null, emailDestinatario);
    }

    public static EmailSendResult error(String mensaje) {
        return new EmailSendResult(false, mensaje, null);
    }

    public static EmailSendResult errorEnvio(String emailDestinatario, String mensaje) {
        return new EmailSendResult(false, mensaje, emailDestinatario);
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getEmailDestinatario() {
        return emailDestinatario;
    }
}
