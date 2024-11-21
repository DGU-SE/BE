package com.twofullmoon.howmuchmarket.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Integer productId;
    private String userId;
    private String content;
    private boolean isSecret;
    
    public String getUserId() {
        return userId;
    }

    public Integer getProductId() {
        return productId;
    }
}