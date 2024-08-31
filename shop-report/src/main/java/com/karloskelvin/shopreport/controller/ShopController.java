package com.karloskelvin.shopreport.controller;

import com.karloskelvin.shopreport.dto.ShopReportDTO;
import com.karloskelvin.shopreport.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("shop_report")
@RequiredArgsConstructor
public class ShopController {

    private final ReportRepository reportRepository;

    @GetMapping
    public List<ShopReportDTO> getShopReport() {
        return reportRepository.findAll()
                .stream()
                .map(ShopReportDTO::convert)
                .collect(Collectors.toList());
    }
}
