package com.camvels.infrastructure.adapter.in.web;

import com.camvels.application.dto.EmailSendResult;
import com.camvels.application.port.in.SendEmailPort;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierEmailController {

    private final SendEmailPort sendEmailPort;

    public SupplierEmailController(SendEmailPort sendEmailPort) {
        this.sendEmailPort = sendEmailPort;
    }

    @PostMapping(value = "/email", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','ALMACEN')")
    public Map<String, String> sendEmail(
            @RequestParam int proveedorId,
            @RequestParam String proveedorEmail,
            @RequestParam String asunto,
            @RequestParam String mensaje,
            @RequestParam(required = false) MultipartFile archivoAdjunto) throws Exception {
        byte[] bytes = null;
        String nombre = null;
        if (archivoAdjunto != null && !archivoAdjunto.isEmpty()) {
            bytes = archivoAdjunto.getBytes();
            nombre = archivoAdjunto.getOriginalFilename();
        }
        EmailSendResult result = sendEmailPort.enviar(proveedorId, proveedorEmail, asunto, mensaje, bytes, nombre);
        if (!result.isExito()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getMensaje());
        }
        return Map.of("message", "Correo enviado a " + result.getEmailDestinatario());
    }
}
