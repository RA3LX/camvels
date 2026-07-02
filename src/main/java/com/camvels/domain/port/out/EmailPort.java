package com.camvels.domain.port.out;

import com.camvels.domain.model.EmailMessage;
import com.camvels.domain.model.EmailSendOutcome;

public interface EmailPort {

    EmailSendOutcome enviar(EmailMessage message);
}
