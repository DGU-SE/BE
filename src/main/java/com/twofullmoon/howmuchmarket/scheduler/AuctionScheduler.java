package com.twofullmoon.howmuchmarket.scheduler;

import com.twofullmoon.howmuchmarket.service.AuctionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuctionScheduler {
    private final AuctionService auctionService;

    public AuctionScheduler(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @Scheduled(cron = "0 * * * * *") // 매 0초마다
    public void runAuctionCheck() {
        auctionService.checkAndCloseAuctions();
    }
}