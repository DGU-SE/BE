package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.dto.LocationDTO;
import com.twofullmoon.howmuchmarket.dto.ProductDTO;
import com.twofullmoon.howmuchmarket.dto.ProductPictureDTO;
import com.twofullmoon.howmuchmarket.dto.ProductRequestDTO;
import com.twofullmoon.howmuchmarket.entity.Location;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.entity.ProductPicture;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.mapper.LocationMapper;
import com.twofullmoon.howmuchmarket.mapper.ProductMapper;
import com.twofullmoon.howmuchmarket.mapper.ProductPictureMapper;
import com.twofullmoon.howmuchmarket.repository.LocationRepository;
import com.twofullmoon.howmuchmarket.repository.ProductPictureRepository;
import com.twofullmoon.howmuchmarket.repository.ProductRepository;
import com.twofullmoon.howmuchmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductService {

    private static final String UPLOAD_DIR = "uploads/";

    private final ProductRepository productRepository;
    private final ProductPictureRepository productPictureRepository;
    private final ProductMapper productMapper;
    private final ProductPictureMapper productPictureMapper;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, ProductPictureRepository productPictureRepository, ProductMapper productMapper, ProductPictureMapper productPictureMapper, LocationRepository locationRepository, LocationMapper locationMapper, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productPictureRepository = productPictureRepository;
        this.productMapper = productMapper;
        this.productPictureMapper = productPictureMapper;
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.userRepository = userRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product createProduct(ProductRequestDTO productRequestDTO) {
        LocationDTO locationDTO = productRequestDTO.getLocationDTO();
        Location location = locationMapper.toEntity(locationDTO);

        locationRepository.save(location);

        User seller = userRepository.findById(productRequestDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productMapper.toEntity(productRequestDTO, seller, location);

        return productRepository.save(product);
    }


    // 특정 사용자의 상품 목록 조회
    public List<Product> getProductsByUserId(String userId) {
        return productRepository.findByUserId(userId);
    }

    // 상품 검색 기능	
    public List<Product> searchProducts(String keyword, Double latitude, Double longitude, Integer lowBound, Integer upBound, String productStatus) {
        return productRepository.searchProducts(keyword, latitude, longitude, lowBound, upBound, productStatus);
    }

    @Transactional
    public ProductDTO saveProductImages(int productId, List<MultipartFile> images) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        productPictureRepository.deleteAllByProductId(productId);
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                String fileName = productId + "_" + image.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }

                Files.write(filePath, image.getBytes());

                ProductPicture productPicture = ProductPicture.builder()
                        .product(product)
                        .blobUrl(fileName)
                        .build();
                productPictureRepository.save(productPicture);
            }
        }

        List<ProductPictureDTO> productPictureDTOs = productPictureRepository.findByProductId(productId).stream()
                .map(productPictureMapper::toDTO)
                .toList();

        return productMapper.toDTO(product, productPictureDTOs);
    }

    public ProductDTO getProduct(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        return productMapper.toDTO(product, product.getProductPictures().stream().map(productPictureMapper::toDTO).toList());
    }

    public byte[] getImage(String fileName) {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);

        if (Files.exists(filePath)) {
            try {
                return Files.readAllBytes(filePath);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to read file");
            }
        } else {
            throw new IllegalArgumentException("File not found");
        }
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }
}


