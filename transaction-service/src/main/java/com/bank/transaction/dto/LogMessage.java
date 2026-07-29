package com.bank.transaction.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogMessage {

    private String message;
    private String messageType;
    private String dateTime;

}