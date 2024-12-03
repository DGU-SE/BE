package com.twofullmoon.howmuchmarket.service;

import com.twofullmoon.howmuchmarket.dto.CommentDTO;
import com.twofullmoon.howmuchmarket.entity.Comment;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.entity.User;
import com.twofullmoon.howmuchmarket.mapper.CommentMapper;
import com.twofullmoon.howmuchmarket.repository.CommentRepository;
import com.twofullmoon.howmuchmarket.repository.ProductRepository;
import com.twofullmoon.howmuchmarket.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, UserRepository userRepository, ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Comment createComment(CommentDTO commentDTO) {
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Product product = productRepository.findById(commentDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        Comment comment = commentMapper.toEntity(commentDTO, user, product);

        return commentRepository.save(comment);
    }

    public List<CommentDTO> getCommentsByProduct(String userId, int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        String sellerId = product.getUser().getId();

        return commentRepository.findByProductId(productId).stream().map(comment -> {
            if (comment.isSecret() && !userId.equals(sellerId) && !userId.equals(comment.getUser().getId())) {
                return CommentDTO.builder()
                        .content("비밀 댓글입니다.")
                        .secret(true)
                        .userId(comment.getUser().getId())
                        .productId(productId)
                        .build();
            } else {
                return commentMapper.toDTO(comment);
            }
        }).toList();
    }
}
