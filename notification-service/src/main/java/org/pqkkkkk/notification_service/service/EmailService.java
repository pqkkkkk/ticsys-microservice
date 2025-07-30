package org.pqkkkkk.notification_service.service;

import org.pqkkkkk.notification_service.entity.EmailDetails;

public interface EmailService {
    public boolean sendSimpleMail(EmailDetails emailDetails);
}
