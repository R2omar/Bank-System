package com.bank.bff.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private UUID userId;

    private String username;
    private String email;
    private String firstName;
    private String lastName;

    private List<DashboardAccountResponse> accounts;
}
