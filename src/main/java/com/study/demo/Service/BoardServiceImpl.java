package com.study.demo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.demo.DTO.BoardRequest;
import com.study.demo.DTO.BoardResponse;
import com.study.demo.DTO.BoardSearchRequest;
import com.study.demo.Domain.Board;
import com.study.demo.mapper.BoardMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardMapper boardMapper;
    
    @Override
        public Long savePost(BoardRequest params) {
            boardMapper.save(params);
            return params.getId();
        }

    @Override
    @Transactional
    public Long updatePost(BoardRequest params) {
        boardMapper.update(params);
        return params.getId();
    }

    @Override
    @Transactional
    public Long deletePost(Long id) {
        boardMapper.deleteById(id);
        return id;
    }

    @Transactional
    @Override
    public BoardResponse findPostById(Long id) {
        boardMapper.updateHit(id);
        
        Board board = boardMapper.findById(id);

        return new BoardResponse(
            board.getId(),
            board.getTitle(),
            board.getContent(),
            board.getWriter(),
            board.getHit(),
            board.getCreatedDate(),
            board.getModifiedDate()
        );
    }

    @Override
    public List<BoardResponse> findAllPosts(final BoardSearchRequest params) {
    List<Board> list = boardMapper.findAll(params);
        return list.stream()
        .map(board -> new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getWriter(),
                board.getHit(),
                board.getCreatedDate(),
                board.getModifiedDate()))
        .collect(Collectors.toList());
    }

    @Override
    public int countPosts(final BoardSearchRequest params) {    
        return boardMapper.count(params);
    }

}
