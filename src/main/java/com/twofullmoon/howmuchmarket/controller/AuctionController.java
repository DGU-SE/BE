package com.twofullmoon.howmuchmarket.controller;

import com.twofullmoon.howmuchmarket.dto.AuctionDTO;
import com.twofullmoon.howmuchmarket.dto.BidDTO;
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

    @PostMapping("/create")
    public ResponseEntity<String> createAuction(@RequestBody AuctionDTO auctionDTO) {
        auctionService.createAuction(auctionDTO);

        return ResponseEntity.ok("Auction created successfully");
    }

    @PostMapping("/bid")
    public ResponseEntity<String> bid(@RequestBody BidDTO bidDTO) {
        bidService.createBid(bidDTO.getUserId(), bidDTO.getAuctionId(), bidDTO.getAmount());

        return ResponseEntity.ok("Bid created successfully");
    }

    @GetMapping("/bid/{userId}")
    public ResponseEntity<List<BidDTO>> getBidsByUserId(@PathVariable(name = "userId") String userId) {
        List<Bid> bids = bidService.getBidsByUser(userId);

        return ResponseEntity.ok(bids.stream().map(bid -> bidService.getBidDTO(bid, true, true)).collect(Collectors.toList()));
    }
}
