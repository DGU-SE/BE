package com.twofullmoon.howmuchmarket.controller;

import com.twofullmoon.howmuchmarket.dto.AuctionDTO;
import com.twofullmoon.howmuchmarket.dto.BidDTO;
import com.twofullmoon.howmuchmarket.dto.BidRequestDTO;
import com.twofullmoon.howmuchmarket.entity.Bid;
import com.twofullmoon.howmuchmarket.service.AuctionService;
import com.twofullmoon.howmuchmarket.service.BidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auction")
public class AuctionController {
    private final AuctionService auctionService;
    private final BidService bidService;

    public AuctionController(AuctionService auctionService, BidService bidService) {
        this.auctionService = auctionService;
        this.bidService = bidService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<AuctionDTO> getAuctionByProductId(@PathVariable(name = "productId") int productId) {
        return ResponseEntity.ok(auctionService.getAuctionByProductId(productId));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAuction(@RequestBody AuctionDTO auctionDTO) {
        auctionService.createAuction(auctionDTO);

        return ResponseEntity.ok("Auction created successfully");
    }

    @PostMapping("/bid")
    public ResponseEntity<BidDTO> bid(@RequestBody BidRequestDTO bidDTO) {
        BidDTO bidResultDTO = bidService.createBid(bidDTO.getUserId(), bidDTO.getProductId(), bidDTO.getAmount());

        return ResponseEntity.ok(bidResultDTO);
    }

    @GetMapping("/bid/{userId}")
    public ResponseEntity<List<BidDTO>> getBidsByUserId(@PathVariable(name = "userId") String userId) {
        return ResponseEntity.ok(bidService.getMergedBidsByHighestAmount(userId));
    }
}
