package com.camvels.domain.model;

public class EmailMessage {

    private final String destinatario;
    private final String asunto;
    private final String cuerpo;
    private final byte[] archivoAdjunto;
    private final String nombreArchivo;

    public EmailMessage(String destinatario, String asunto, String cuerpo,
                        byte[] archivoAdjunto, String nombreArchivo) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.archivoAdjunto = archivoAdjunto;
        this.nombreArchivo = nombreArchivo;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public byte[] getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }
}
