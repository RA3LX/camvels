package com.camvels.application.usecase;

import com.camvels.application.dto.EmailSendResult;
import com.camvels.application.port.in.SendEmailPort;
import com.camvels.domain.model.EmailMessage;
import com.camvels.domain.model.EmailSendOutcome;
import com.camvels.domain.model.Proveedor;
import com.camvels.domain.port.out.EmailPort;
import com.camvels.domain.port.out.ProveedorRepository;

public class SendEmailUseCase implements SendEmailPort {

    private final ProveedorRepository proveedorRepository;
    private final EmailPort emailPort;

    public SendEmailUseCase(ProveedorRepository proveedorRepository, EmailPort emailPort) {
        this.proveedorRepository = proveedorRepository;
        this.emailPort = emailPort;
    }

    @Override
    public EmailSendResult enviar(int proveedorId, String emailDestinatario, String asunto,
                                  String mensaje, byte[] archivoAdjunto, String nombreArchivo) {
        if (emailDestinatario == null || emailDestinatario.trim().isEmpty()
                || asunto == null || asunto.trim().isEmpty()
                || mensaje == null || mensaje.trim().isEmpty()) {
            return EmailSendResult.error("Todos los campos son obligatorios.");
        }

        var proveedorOpt = proveedorRepository.buscarPorId(proveedorId);
        if (proveedorOpt.isEmpty()) {
            return EmailSendResult.error("Proveedor no encontrado.");
        }

        Proveedor proveedor = proveedorOpt.get();
        String destinatario = emailDestinatario.trim();
        if (destinatario.isEmpty() && proveedor.getEmail() != null && !proveedor.getEmail().trim().isEmpty()) {
            destinatario = proveedor.getEmail().trim();
        }

        if (destinatario.isEmpty()) {
            return EmailSendResult.error(
                    "El proveedor no tiene un correo electrónico configurado. Por favor, ingrese un correo válido.");
        }

        EmailMessage message = new EmailMessage(destinatario, asunto.trim(), mensaje.trim(),
                archivoAdjunto, nombreArchivo);
        EmailSendOutcome outcome = emailPort.enviar(message);

        if (outcome.isExito()) {
            return EmailSendResult.exito(destinatario);
        }

        String errorDetallado = outcome.getError();
        String mensajeError = "Error al enviar el correo a " + destinatario + ". ";
        if (errorDetallado != null && !errorDetallado.isEmpty()) {
            mensajeError += errorDetallado;
        } else {
            mensajeError += "Posibles causas: credenciales incorrectas, cuenta bloqueada o problema de conexión.";
        }
        return EmailSendResult.errorEnvio(destinatario, mensajeError);
    }
}
