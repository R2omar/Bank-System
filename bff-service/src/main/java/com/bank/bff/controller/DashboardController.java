package com.bank.bff.controller;

import com.bank.bff.dto.DashboardResponse;
import com.bank.bff.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/bff")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard/{userId}")
    public Mono<DashboardResponse> getDashboard(@PathVariable UUID userId) {
        return dashboardService.getDashboard(userId);
    }
}
