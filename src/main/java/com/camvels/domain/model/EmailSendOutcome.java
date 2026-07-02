package com.camvels.domain.model;

public class EmailSendOutcome {

    private final boolean exito;
    private final String error;

    private EmailSendOutcome(boolean exito, String error) {
        this.exito = exito;
        this.error = error;
    }

    public static EmailSendOutcome exito() {
        return new EmailSendOutcome(true, null);
    }

    public static EmailSendOutcome fallo(String error) {
        return new EmailSendOutcome(false, error);
    }

    public boolean isExito() {
        return exito;
    }

    public String getError() {
        return error;
    }
}
