package com.thanhvh.shopmanagement.modules.mail.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SendMailModel {

    @Builder.Default
    private Set<String> ccEmails = new HashSet<>();

    @Builder.Default
    private Set<String> bccEmails = new HashSet<>();

    private String subject;

    private boolean htmlContent;

    private String content;
    @Builder.Default
    private Set<String> targets = new HashSet<>();

    @Builder.Default
    private List<Attachment> attachments = new ArrayList<>();
}
