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
                .secret(dto.isSecret())
                .build();
    }

    public CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .content(comment.getContent())
                .secret(comment.isSecret())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getName())
                .productId(comment.getProduct().getId())
                .build();
    }
}
