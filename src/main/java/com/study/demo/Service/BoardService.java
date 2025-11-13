package com.study.demo.Service;

import java.util.List;

import com.study.demo.DTO.BoardRequest;
import com.study.demo.DTO.BoardResponse;
import com.study.demo.DTO.BoardSearchRequest;

public interface BoardService {
    Long savePost(BoardRequest params);

    BoardResponse findPostById(Long id);

    Long updatePost(BoardRequest params);

    Long deletePost(Long id);

    List<BoardResponse> findAllPosts(BoardSearchRequest params);

    int countPosts(BoardSearchRequest params);
} 