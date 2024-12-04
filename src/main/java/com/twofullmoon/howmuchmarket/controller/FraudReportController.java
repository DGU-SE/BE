package com.twofullmoon.howmuchmarket.controller;

import com.twofullmoon.howmuchmarket.dto.FraudReportDTO;
import com.twofullmoon.howmuchmarket.entity.FraudReport;
import com.twofullmoon.howmuchmarket.mapper.FraudReportMapper;
import com.twofullmoon.howmuchmarket.repository.FraudReportRepository;
import com.twofullmoon.howmuchmarket.repository.ProductRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fraud-report")
public class FraudReportController {
    private final FraudReportRepository fraudReportRepository;
    private final FraudReportMapper fraudReportMapper;
    private final ProductRepository productRepository;

    public FraudReportController(FraudReportRepository fraudReportRepository, FraudReportMapper fraudReportMapper, ProductRepository productRepository) {
        this.fraudReportRepository = fraudReportRepository;
        this.fraudReportMapper = fraudReportMapper;
        this.productRepository = productRepository;
    }

    @PostMapping
    public FraudReportDTO reportFraud(@RequestBody FraudReportDTO fraudReportDTO) {

        if (fraudReportDTO.getProductId() == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }

        return fraudReportMapper.toDTO(fraudReportRepository.save(fraudReportMapper.toEntity(fraudReportDTO, productRepository.findById(fraudReportDTO.getProductId()).orElseThrow(() -> new IllegalArgumentException("Invalid product ID")))));
    }

}
