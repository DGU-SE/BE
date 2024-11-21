package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.FraudReportDTO;
import com.twofullmoon.howmuchmarket.entity.FraudReport;
import com.twofullmoon.howmuchmarket.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class FraudReportMapper {
    public FraudReport toEntity(FraudReportDTO fraudReport, Product product) {
        return FraudReport.builder()
                .product(product)
                .description(fraudReport.getDescription())
                .build();
    }

    public FraudReportDTO toDTO(FraudReport fraudReport) {
        return FraudReportDTO.builder()
                .productId(fraudReport.getProduct().getId())
                .description(fraudReport.getDescription())
                .build();
    }
}
