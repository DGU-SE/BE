package com.twofullmoon.howmuchmarket.controller;

import com.twofullmoon.howmuchmarket.dto.AuctionDTO;
import com.twofullmoon.howmuchmarket.dto.ProductDTO;
import com.twofullmoon.howmuchmarket.dto.ProductRequestDTO;
import com.twofullmoon.howmuchmarket.dto.ProductSearchCriteriaDTO;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.mapper.ProductMapper;
import com.twofullmoon.howmuchmarket.service.AuctionService;
import com.twofullmoon.howmuchmarket.service.LocationService;
import com.twofullmoon.howmuchmarket.service.ProductService;
import com.twofullmoon.howmuchmarket.service.UserService;
import com.twofullmoon.howmuchmarket.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;
    private final ProductMapper productMapper;
    private final AuctionService auctionService;

    public ProductController(ProductService productService, JwtUtil jwtUtil, ProductMapper productMapper, AuctionService auctionService) {
        this.productService = productService;
        this.jwtUtil = jwtUtil;
        this.productMapper = productMapper;
        this.auctionService = auctionService;
    }

    // 올린 상품 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<ProductDTO>> getMyProducts(@RequestHeader("Authorization") String token) {
        // JWT에서 userId 추출
        String userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        List<ProductDTO> products = productService.getProductsByUserId(userId);
        return ResponseEntity.ok(products);
    }

    // 상품 검색
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@ModelAttribute ProductSearchCriteriaDTO criteria) {
        List<ProductDTO> products = productService.searchProducts(criteria);
        return ResponseEntity.ok(products);
    }

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestHeader("Authorization") String token,
                                                    @RequestBody ProductRequestDTO request) {
        String userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));

        if (!userId.equals(request.getUserId())) {
            throw new IllegalArgumentException("userId in token and request body are not same");
        }

        Product product = productService.createProduct(request);
        ProductDTO productDTO = productMapper.toDTO(product);

        if (request.getOnAuction()) {
            AuctionDTO auctionDTO = request.getAuctionDTO();
            auctionDTO.setProductId(product.getId());
            auctionService.createAuction(auctionDTO);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<ProductDTO> uploadImage(@PathVariable(name = "id") int productId, @RequestParam(name = "images") List<MultipartFile> images) throws IOException {
        ProductDTO productDTO = productService.saveProductImages(productId, images);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping("/image/{blobUrl}")
    public ResponseEntity<byte[]> getImage(@PathVariable(name = "blobUrl") String blobUrl) {
        byte[] fileData = productService.getImage(blobUrl);

        String contentType = URLConnection.guessContentTypeFromName(blobUrl);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable(name = "id") int productId) {
        ProductDTO productDTO = productService.getProduct(productId);
        return ResponseEntity.ok(productDTO);
    }
    
    // 상품 수정
    @PutMapping("/alter/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable("id") Integer id,
            @RequestBody ProductRequestDTO productRequestDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productRequestDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    // 삭제 기능 추가
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }

    
}
