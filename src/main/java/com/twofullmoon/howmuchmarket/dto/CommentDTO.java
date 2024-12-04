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
    private String userName;
    private String content;
    private boolean secret;
    
    public String getUserId() {
        return userId;
    }

    public Integer getProductId() {
        return productId;
    }
}
