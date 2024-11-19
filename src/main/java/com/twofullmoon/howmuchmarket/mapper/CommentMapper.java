package com.twofullmoon.howmuchmarket.mapper;

import com.twofullmoon.howmuchmarket.dto.CommentDTO;
import com.twofullmoon.howmuchmarket.entity.Comment;
import com.twofullmoon.howmuchmarket.entity.Product;
import com.twofullmoon.howmuchmarket.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public Comment toEntity(CommentDTO dto, User user, Product product) {
        return Comment.builder()
                .product(product)
                .user(user)
                .content(dto.getContent())
                .isSecret(dto.isSecret())
                .build();
    }

    public CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .content(comment.getContent())
                .isSecret(comment.isSecret())
                .userId(comment.getUser().getId())
                .productId(comment.getProduct().getId())
                .build();
    }
}
