package com.twofullmoon.howmuchmarket.controller;

import com.twofullmoon.howmuchmarket.dto.CommentDTO;
import com.twofullmoon.howmuchmarket.entity.Comment;
import com.twofullmoon.howmuchmarket.mapper.CommentMapper;
import com.twofullmoon.howmuchmarket.service.CommentService;
import com.twofullmoon.howmuchmarket.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final JwtUtil jwtUtil;

    public CommentController(CommentService commentService, CommentMapper commentMapper, JwtUtil jwtUtil) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        Comment comment = commentService.createComment(commentDTO);
        CommentDTO createdCommentDTO = commentMapper.toDTO(comment);

        return ResponseEntity.ok(createdCommentDTO);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<CommentDTO>> getCommentByProduct(@PathVariable(name = "productId") int productId, @RequestHeader("Authorization") String authorizationHeader) {
        String tokenUserId = jwtUtil.extractUserId(authorizationHeader.substring(7));
        List<CommentDTO> comments = commentService.getCommentsByProduct(tokenUserId, productId);

        return ResponseEntity.ok(comments);
    }
}
