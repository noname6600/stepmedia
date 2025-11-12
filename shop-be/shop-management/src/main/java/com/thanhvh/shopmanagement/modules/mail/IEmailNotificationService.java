package com.thanhvh.shopmanagement.modules.mail;

import com.thanhvh.shopmanagement.modules.mail.model.SendMailModel;

public interface IEmailNotificationService {

    void handle(SendMailModel request);
}
