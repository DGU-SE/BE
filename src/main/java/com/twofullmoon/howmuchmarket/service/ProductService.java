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
import com.twofullmoon.howmuchmarket.util.DistanceUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final EntityManager entityManager;

    public ProductService(ProductRepository productRepository, ProductPictureRepository productPictureRepository, ProductMapper productMapper, ProductPictureMapper productPictureMapper, LocationRepository locationRepository, LocationMapper locationMapper, UserRepository userRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.productPictureRepository = productPictureRepository;
        this.productMapper = productMapper;
        this.productPictureMapper = productPictureMapper;
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
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

    public ProductDTO getProductDTO(Product product, boolean includeProductPictures, double distanceKiloMeter) {
        List<ProductPictureDTO> productPictureDTOs = includeProductPictures ? product.getProductPictures().stream()
                .map(productPictureMapper::toDTO)
                .toList() : null;
        return productMapper.toDTO(product, productPictureDTOs, distanceKiloMeter);
    }

    public List<ProductDTO> getProductsByUserId(String userId) {
        return productRepository.findByUserId(userId).stream().map(product -> {
            return getProductDTO(product, true);
        }).toList();
    }

    // 상품 검색 기능	
    public List<ProductDTO> searchProducts(ProductSearchCriteriaDTO criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        Join<Product, Location> locationJoin = null;

        if (criteria.getKeyword() != null && !criteria.getKeyword().isEmpty()) {
            predicates.add(cb.like(root.get("name"), "%" + criteria.getKeyword() + "%"));
        }

        Expression<Double> distance = null;
        if (criteria.getLatitude() != null && criteria.getLongitude() != null) {
            locationJoin = root.join("location");
            distance = cb.function(
                    "calculate_distance",
                    Double.class,
                    locationJoin.get("latitude"),
                    locationJoin.get("longitude"),
                    cb.literal(criteria.getLatitude()),
                    cb.literal(criteria.getLongitude())
            );
            predicates.add(cb.lessThanOrEqualTo(distance, 30.0));
        }

        if (criteria.getLowBound() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.getLowBound()));
        }
        if (criteria.getUpBound() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.getUpBound()));
        }

        if (criteria.getProductStatus() != null && !criteria.getProductStatus().isEmpty()) {
            predicates.add(cb.equal(root.get("productStatus"), criteria.getProductStatus()));
        }

        if (distance != null) {
            query.multiselect(root, distance.alias("distance"));
        } else {
            query.multiselect(root, cb.literal(null).alias("distance"));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.distinct(true);
        query.orderBy(cb.desc(root.get("regTime")));

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        List<Tuple> results = typedQuery.getResultList();

        return results.stream()
                .map(tuple -> {
                    Product product = tuple.get(root);
                    Double dist = tuple.get("distance", Double.class);
                    if (dist == null) {
                        dist = -1.0;
                    }
                    return getProductDTO(product, true, dist);
                })
                .collect(Collectors.toList());
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

    public ProductDTO getProduct(int productId, Double longitude, Double latitude) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        double distanceKiloMeter = DistanceUtil.calculateDistance(
                latitude,
                longitude,
                product.getLocation().getLatitude(),
                product.getLocation().getLongitude()
        );

        return productMapper.toDTO(
                product,
                product.getProductPictures().stream().map(productPictureMapper::toDTO).toList(),
                distanceKiloMeter
        );
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

    // 상품 수정
    public ProductDTO updateProduct(Integer id, ProductRequestDTO productRequestDTO) {
        // 데이터베이스에서 기존 Product 찾기
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        if (existingProduct.getOnAuction() && !productRequestDTO.getPrice().equals(existingProduct.getPrice())) {
            throw new IllegalArgumentException("Cannot change price of product on auction");
        }

        existingProduct.setName(productRequestDTO.getName());
        existingProduct.setProductDetail(productRequestDTO.getProductDetail());
        existingProduct.setPrice(productRequestDTO.getPrice());

        // 업데이트된 Product 저장
        Product updatedProduct = productRepository.save(existingProduct);

        // DTO로 변환하여 반환
        return productMapper.toDTO(updatedProduct);
    }

    // 삭제 기능
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        productRepository.delete(product);
    }


}


