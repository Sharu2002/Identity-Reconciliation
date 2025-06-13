package com.identity.reconciliation.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
public class ContactResponseDto {

    private ContactDTO contact;

    @Data
    @AllArgsConstructor
    public static class ContactDTO {
        private Integer primaryContatctId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Integer> secondaryContactIds;
    }
}