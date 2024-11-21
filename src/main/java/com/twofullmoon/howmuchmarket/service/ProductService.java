package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.dto.*;
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
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public ProductDTO getProductDTO(Product product, boolean includeProductPictures) {
        List<ProductPictureDTO> productPictureDTOs = includeProductPictures ? product.getProductPictures().stream()
                .map(productPictureMapper::toDTO)
                .toList() : null;
        return productMapper.toDTO(product, productPictureDTOs);
    }

    public List<ProductDTO> getProductsByUserId(String userId) {
        return productRepository.findByUserId(userId).stream().map(product -> {
            return getProductDTO(product, true);
        }).toList();
    }

    // 상품 검색 기능	
    public List<ProductDTO> searchProducts(ProductSearchCriteriaDTO criteria) {
        Specification<Product> spec = Specification.where(null);

        if (criteria.getKeyword() != null && !criteria.getKeyword().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + criteria.getKeyword() + "%"));
        }

        if (criteria.getLatitude() != null && criteria.getLongitude() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Product, Location> locationJoin = root.join("location");

                // Haversine formula calculation using database function
                Expression<Double> distance = criteriaBuilder.function(
                        "calculate_distance",
                        Double.class,
                        locationJoin.get("latitude"),
                        locationJoin.get("longitude"),
                        criteriaBuilder.literal(criteria.getLatitude()),
                        criteriaBuilder.literal(criteria.getLongitude())
                );

                // Radius condition
                return criteriaBuilder.lessThanOrEqualTo(distance, 30.0);
            });
        }

        if (criteria.getLowBound() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), criteria.getLowBound()));
        }

        if (criteria.getUpBound() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), criteria.getUpBound()));
        }

        if (criteria.getProductStatus() != null && !criteria.getProductStatus().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("productStatus"), criteria.getProductStatus()));
        }

        return productRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "regTime")).stream()
                .map(product -> getProductDTO(product, true))
                .toList();
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


