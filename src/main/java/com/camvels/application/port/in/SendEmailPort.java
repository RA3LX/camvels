package com.camvels.application.port.in;

import com.camvels.application.dto.EmailSendResult;

public interface SendEmailPort {

    EmailSendResult enviar(int proveedorId, String emailDestinatario, String asunto,
                           String mensaje, byte[] archivoAdjunto, String nombreArchivo);
}
