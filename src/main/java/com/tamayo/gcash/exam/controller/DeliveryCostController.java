package com.tamayo.gcash.exam.controller;

import com.tamayo.gcash.exam.dto.DeliveryCostRequestDTO;
import com.tamayo.gcash.exam.dto.DeliveryCostResponseDTO;
import com.tamayo.gcash.exam.service.DeliveryCostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/delivery")
@RequiredArgsConstructor
public class DeliveryCostController {

    private final DeliveryCostService deliveryCostService;

    @GetMapping("/cost")
    public DeliveryCostResponseDTO calculateCost(@Valid DeliveryCostRequestDTO requestDto) {
        return deliveryCostService.calculateCost(requestDto);
    }
}
