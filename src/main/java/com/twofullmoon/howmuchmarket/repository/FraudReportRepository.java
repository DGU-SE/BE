package com.twofullmoon.howmuchmarket.repository;

import com.twofullmoon.howmuchmarket.entity.FraudReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FraudReportRepository extends JpaRepository<FraudReport, Integer> {
    List<FraudReport> findByProductId(int productId);
}
